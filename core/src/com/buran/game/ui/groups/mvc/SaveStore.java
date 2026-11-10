package com.buran.game.ui.groups.mvc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.buran.game.ui.groups.mvc.global.GlobalDAO;
import com.buran.game.ui.groups.mvc.profiles.ProfileDAO;
import com.buran.game.ui.groups.mvc.profiles.SettingsDAO;
import com.buran.game.ui.groups.mvc.results.DetailedResultDAO;
import com.buran.game.ui.groups.mvc.results.ResultDAO;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Persists profiles and global flags through libGDX {@link Preferences}: a file in the user's
 * home on desktop, {@code localStorage} in the browser.
 * <p>
 * Serialization is written by hand on purpose. Reflection-based JSON needs explicit reflection
 * rules in the TeaVM web build, and the data model is small and stable.
 */
public final class SaveStore {
    private static final String PREFS_NAME = "buran19";
    private static final String KEY_PROFILES = "profiles";
    private static final String KEY_GLOBAL = "global";

    private SaveStore() {
    }

    private static Preferences prefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static boolean hasProfiles() {
        return prefs().contains(KEY_PROFILES);
    }

    public static boolean hasGlobal() {
        return prefs().contains(KEY_GLOBAL);
    }

    // ---------------------------------------------------------------- profiles

    /** @return saved profiles, or null when there is nothing saved or it cannot be parsed. */
    public static List<ProfileDAO> readProfiles() {
        String raw = prefs().getString(KEY_PROFILES, null);
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        try {
            JsonValue root = new JsonReader().parse(raw);
            List<ProfileDAO> list = new ArrayList<ProfileDAO>();
            for (JsonValue p = root.child; p != null; p = p.next) {
                list.add(readProfile(p));
            }
            return list;
        } catch (RuntimeException e) {
            Gdx.app.error("SaveStore", "Cannot parse saved profiles, starting fresh", e);
            return null;
        }
    }

    public static void writeProfiles(List<ProfileDAO> profiles) {
        StringWriter out = new StringWriter();
        JsonWriter w = new JsonWriter(out);
        w.setOutputType(JsonWriter.OutputType.json);
        try {
            w.array();
            for (ProfileDAO p : profiles) {
                writeProfile(w, p);
            }
            w.pop();
            w.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        Preferences prefs = prefs();
        prefs.putString(KEY_PROFILES, out.toString());
        prefs.flush();
    }

    private static ProfileDAO readProfile(JsonValue v) {
        ProfileDAO.Type type = ProfileDAO.Type.valueOf(v.getString("type", ProfileDAO.Type.DUMMY.name()));
        JsonValue prefs = v.get("preferences");
        SettingsDAO settings = prefs == null || prefs.isNull() ? null : readSettings(prefs);
        Set<ResultDAO> results = new HashSet<ResultDAO>();
        JsonValue rs = v.get("resultSet");
        if (rs != null) {
            for (JsonValue r = rs.child; r != null; r = r.next) {
                results.add(readResult(r));
            }
        }
        ProfileDAO p = new ProfileDAO(type, v.getString("username", ""), settings, results, v.getBoolean("active", false));
        String id = v.getString("id", null);
        if (id != null) {
            p.setId(id);
        }
        return p;
    }

    private static void writeProfile(JsonWriter w, ProfileDAO p) throws java.io.IOException {
        w.object();
        w.set("type", p.getType().name());
        w.set("username", p.getUsername());
        w.set("active", p.isActive());
        w.set("id", p.getId());
        if (p.getPreferences() != null) {
            w.name("preferences");
            writeSettings(w, p.getPreferences());
        }
        w.name("resultSet").array();
        if (p.getResultSet() != null) {
            for (ResultDAO r : p.getResultSet()) {
                writeResult(w, r);
            }
        }
        w.pop();
        w.pop();
    }

    private static SettingsDAO readSettings(JsonValue v) {
        SettingsDAO s = new SettingsDAO();
        s.setNotification(v.getBoolean("notification", s.isNotification()));
        s.setSoundVolume(v.getFloat("soundVolume", s.getSoundVolume()));
        s.setEffects(v.getBoolean("effects", s.isEffects()));
        s.setEffectsVolume(v.getFloat("effectsVolume", s.getEffectsVolume()));
        s.setMusic(v.getBoolean("music", s.isMusic()));
        s.setMusicVolume(v.getFloat("musicVolume", s.getMusicVolume()));
        s.setShader(v.getBoolean("shader", s.isShader()));
        s.setChild(v.getBoolean("child", s.isChild()));
        s.setLevel(v.getInt("level", s.getLevel()));
        s.setVibration(v.getInt("vibration", s.getVibration()));
        s.setJoystick(v.getBoolean("joystick", s.isJoystick()));
        s.setJoystick_mode(v.getInt("joystick_mode", s.getJoystick_mode()));
        s.setJoystickColor(v.getBoolean("joystickColor", s.isJoystickColor()));
        s.setRunTimes(v.getInt("runTimes", s.getRunTimes()));
        return s;
    }

    private static void writeSettings(JsonWriter w, SettingsDAO s) throws java.io.IOException {
        w.object();
        w.set("notification", s.isNotification());
        w.set("soundVolume", s.getSoundVolume());
        w.set("effects", s.isEffects());
        w.set("effectsVolume", s.getEffectsVolume());
        w.set("music", s.isMusic());
        w.set("musicVolume", s.getMusicVolume());
        w.set("shader", s.isShader());
        w.set("child", s.isChild());
        w.set("level", s.getLevel());
        w.set("vibration", s.getVibration());
        w.set("joystick", s.isJoystick());
        w.set("joystick_mode", s.getJoystick_mode());
        w.set("joystickColor", s.isJoystickColor());
        w.set("runTimes", s.getRunTimes());
        w.pop();
    }

    private static ResultDAO readResult(JsonValue v) {
        ResultDAO r = new ResultDAO();
        r.setDate(new Date(v.getLong("date", System.currentTimeMillis())));
        r.setLevel(v.getString("level", ""));
        r.setChild(v.getBoolean("child", false));
        JsonValue cur = v.get("current");
        r.setCurrent(cur == null || cur.isNull() ? null : readDetailed(cur));
        JsonValue prev = v.get("previous");
        r.setPrevious(prev == null || prev.isNull() ? null : readDetailed(prev));
        return r;
    }

    private static void writeResult(JsonWriter w, ResultDAO r) throws java.io.IOException {
        w.object();
        w.set("date", r.getDate().getTime());
        w.set("level", r.getLevel());
        w.set("child", r.isChild());
        if (r.getCurrent() != null) {
            w.name("current");
            writeDetailed(w, r.getCurrent());
        }
        if (r.getPrevious() != null) {
            w.name("previous");
            writeDetailed(w, r.getPrevious());
        }
        w.pop();
    }

    private static DetailedResultDAO readDetailed(JsonValue v) {
        return new DetailedResultDAO(
                v.getInt("effectiveness", 0),
                v.getLong("score", 0L),
                v.getInt("time", 0),
                v.getInt("deathModeTime", 0),
                v.getInt("dmgDone", 0),
                v.getInt("dmgReceived", 0),
                v.getInt("enemiesDestroyed", 0),
                v.getInt("minedAsteroids", 0),
                v.getInt("pickUpItems", 0),
                v.getInt("usedItems", 0),
                v.getInt("astronautsTaken", 0),
                v.getInt("levelAchieved", 0));
    }

    private static void writeDetailed(JsonWriter w, DetailedResultDAO d) throws java.io.IOException {
        w.object();
        w.set("effectiveness", d.getEffectiveness());
        w.set("score", d.getScore());
        w.set("time", d.getTime());
        w.set("deathModeTime", d.getDeathModeTime());
        w.set("dmgDone", d.getDmgDone());
        w.set("dmgReceived", d.getDmgReceived());
        w.set("enemiesDestroyed", d.getEnemiesDestroyed());
        w.set("minedAsteroids", d.getMinedAsteroids());
        w.set("pickUpItems", d.getPickUpItems());
        w.set("usedItems", d.getUsedItems());
        w.set("astronautsTaken", d.getAstronautsTaken());
        w.set("levelAchieved", d.getLevelAchieved());
        w.pop();
    }

    // ---------------------------------------------------------------- global

    public static GlobalDAO readGlobal() {
        String raw = prefs().getString(KEY_GLOBAL, null);
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        try {
            JsonValue v = new JsonReader().parse(raw);
            return new GlobalDAO(v.getBoolean("hasStarted", false));
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static void writeGlobal(GlobalDAO g) {
        Preferences prefs = prefs();
        prefs.putString(KEY_GLOBAL, "{\"hasStarted\":" + g.isHasStarted() + "}");
        prefs.flush();
    }
}
