package com.buran.game.ui.menu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.groups.mvc.profiles.ProfileDAO;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.profiles.SettingsDAO;
import com.buran.game.ui.groups.mvc.results.DetailedResultDAO;
import com.buran.game.ui.groups.mvc.results.ResultDAO;
import com.buran.game.utils.StickerResolver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The main menu: title block, a home list and panels (pilots, results, codex, settings,
 * about) in one telemetry style. Works with gamepad, keyboard, mouse and touch, in TV
 * landscape and phone portrait.
 */
public class MainMenu extends Group implements MenuNav.Target {
    private static final String[] CALLSIGNS = {
            "SOKOL", "BERKUT", "ALMAZ", "KEDR", "YASTREB", "ZARYA", "RUBIN", "DNEPR", "PAMIR", "URAL"
    };
    private static final String[] LEVELS = {"EASY", "NORMAL", "HARD"};
    private static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private enum Panel { NONE, PILOTS, RESULTS, CODEX, SETTINGS, ABOUT }

    private final Profiles profiles;
    private final BoomBox boomBox = new BoomBox();
    private final MenuList home = new MenuList();
    private final MenuList panel = new MenuList();
    private Panel open = Panel.NONE;
    private final BitmapFont titleFont;
    private final BitmapFont subFont;
    private final BitmapFont rowFont;
    private final BitmapFont headFont;
    private final BitmapFont smallFont;
    private final GlyphLayout layout = new GlyphLayout();
    private final StickerResolver stickers = new StickerResolver();
    private float panelAlpha;
    private Row.Action pilotRow;
    private boolean retireArmed;

    // Detail pane content (results / codex).
    private DetailedResultDAO detailResult;
    private Codex codex;
    private String detailTitle;
    private Sticker detailSticker;

    public MainMenu() {
        setTransform(false);
        profiles = new Profiles();
        profiles.init();
        boolean portrait = Theme.portrait();
        titleFont = Theme.font(portrait ? 128 : 175);
        subFont = Theme.font(portrait ? 44 : 54);
        rowFont = Theme.font(portrait ? 64 : 72);
        headFont = Theme.font(portrait ? 72 : 96);
        smallFont = Theme.font(44);

        buildHome();
        addActor(home);
        addActor(panel);
        panel.setVisible(false);
        layoutLists();
        home.focusFirst();
        MenuNav.set(this);
    }

    // ------------------------------------------------------------------ layout

    private float panelX() {
        return Theme.portrait() ? Rules.WORLD_WIDTH * 0.05f : Rules.WORLD_WIDTH * 0.36f;
    }

    private float panelW() {
        return Theme.portrait() ? Rules.WORLD_WIDTH * 0.90f : Rules.WORLD_WIDTH * 0.58f;
    }

    private float panelY() {
        return Theme.portrait() ? Rules.WORLD_HEIGHT * 0.06f : Rules.WORLD_HEIGHT * 0.09f;
    }

    private float panelH() {
        return Theme.portrait() ? Rules.WORLD_HEIGHT * 0.86f : Rules.WORLD_HEIGHT * 0.78f;
    }

    private boolean hasDetail() {
        return open == Panel.RESULTS || open == Panel.CODEX;
    }

    private void layoutLists() {
        float mx = Theme.marginX();
        if (Theme.portrait()) {
            home.setBox(mx - 36f, Rules.WORLD_HEIGHT * 0.07f, Rules.WORLD_WIDTH - 2 * mx + 72f, Rules.WORLD_HEIGHT * 0.36f);
        } else {
            home.setBox(mx - 36f, Rules.WORLD_HEIGHT * 0.10f, Rules.WORLD_WIDTH * 0.24f, Rules.WORLD_HEIGHT * 0.46f);
        }
        float head = 170f;
        float px = panelX() + 30f;
        float pw = panelW() - 60f;
        float py = panelY() + 40f;
        float ph = panelH() - head - 60f;
        if (hasDetail()) {
            if (Theme.portrait()) {
                float listH = ph * 0.40f;
                panel.setBox(px, py + ph - listH, pw, listH);
            } else {
                panel.setBox(px, py, pw * 0.5f, ph);
            }
        } else {
            panel.setBox(px, py, pw, ph);
        }
    }

    // ------------------------------------------------------------------ home

    private void buildHome() {
        home.clearRows();
        home.add(new Row.Action("PLAY", rowFont, this::play));
        pilotRow = home.add(new Row.Action("PILOT", rowFont, () -> openPanel(Panel.PILOTS)));
        home.add(new Row.Action("RESULTS", rowFont, () -> openPanel(Panel.RESULTS)));
        home.add(new Row.Action("CODEX", rowFont, () -> openPanel(Panel.CODEX)));
        home.add(new Row.Action("SETTINGS", rowFont, () -> openPanel(Panel.SETTINGS)));
        home.add(new Row.Action("ABOUT", rowFont, () -> openPanel(Panel.ABOUT)));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            home.add(new Row.Action("EXIT", rowFont, () -> Gdx.app.exit()));
        }
        refreshPilotRow();
    }

    private void refreshPilotRow() {
        ProfileDAO p = profiles.getActive();
        pilotRow.withValue(p == null || p.getType() != ProfileDAO.Type.PROFILE ? "NONE" : p.getUsername());
    }

    private ProfileDAO activePilot() {
        ProfileDAO p = profiles.getActive();
        return p != null && p.getType() == ProfileDAO.Type.PROFILE ? p : null;
    }

    private void play() {
        if (activePilot() == null) {
            openPanel(Panel.PILOTS);
            return;
        }
        profiles.write();
        MenuNav.clear(this);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
    }

    // ------------------------------------------------------------------ panels

    private void openPanel(Panel p) {
        open = p;
        retireArmed = false;
        detailResult = null;
        detailSticker = null;
        detailTitle = null;
        panel.clearRows();
        panel.onFocus(null);
        switch (p) {
            case PILOTS: buildPilots(); break;
            case RESULTS: buildResults(); break;
            case CODEX: buildCodex(); break;
            case SETTINGS: buildSettings(); break;
            case ABOUT: buildAbout(); break;
            default: break;
        }
        panel.onBack(this::closePanel);
        layoutLists();
        panel.setVisible(true);
        home.setVisible(!Theme.portrait());
        home.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        panel.focusFirst();
    }

    private void closePanel() {
        open = Panel.NONE;
        panel.setVisible(false);
        home.setVisible(true);
        home.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
        refreshPilotRow();
        home.focusFirst();
    }

    private String panelTitle() {
        switch (open) {
            case PILOTS: return "PILOTS";
            case RESULTS: return "FLIGHT LOG";
            case CODEX: return "CODEX";
            case SETTINGS: return "SETTINGS";
            case ABOUT: return "ABOUT";
            default: return "";
        }
    }

    private void buildPilots() {
        for (final ProfileDAO p : profiles.getList()) {
            if (p.getType() != ProfileDAO.Type.PROFILE) {
                continue;
            }
            Row.Action r = panel.add(new Row.Action(p.getUsername(), rowFont, () -> {
                profiles.activate(p);
                profiles.write();
                boomBox.playSound(MSConstants.UI_PROFILE_CHANGED);
                openPanel(Panel.PILOTS);
            }));
            if (p.isActive()) {
                r.withValue("ACTIVE");
            }
        }
        panel.add(new Row.Action("NEW PILOT", rowFont, this::newPilot));
        if (activePilot() != null) {
            final String name = activePilot().getUsername();
            panel.add(new Row.Action("RETIRE " + name, rowFont, null) {
                @Override
                protected boolean onNav(Nav nav) {
                    if (nav != Nav.ACCEPT) {
                        return false;
                    }
                    if (!retireArmed) {
                        retireArmed = true;
                        setLabel("PRESS AGAIN TO RETIRE " + name);
                        return true;
                    }
                    retirePilot();
                    return true;
                }
            });
        }
        panel.add(new Row.Action("BACK", rowFont, this::closePanel));
    }

    private void newPilot() {
        List<String> used = new ArrayList<String>();
        for (ProfileDAO p : profiles.getList()) {
            if (p.getType() == ProfileDAO.Type.PROFILE) {
                used.add(p.getUsername());
            }
        }
        String name = null;
        for (String c : CALLSIGNS) {
            if (!used.contains(c)) {
                name = c;
                break;
            }
        }
        if (name == null) {
            name = "PILOT " + (used.size() + 1);
        }
        ProfileDAO slot = null;
        for (ProfileDAO p : profiles.getList()) {
            if (p.getType() == ProfileDAO.Type.DUMMY) {
                slot = p;
                break;
            }
        }
        if (slot == null) {
            slot = profiles.addEmpty();
        } else {
            profiles.setEmpty(slot);
        }
        slot.setType(ProfileDAO.Type.PROFILE);
        slot.setUsername(name);
        slot.setPreferences(new SettingsDAO());
        profiles.activate(slot);
        profiles.write();
        boomBox.playSound(MSConstants.UI_PROFILE_SAVED);
        openPanel(Panel.PILOTS);
    }

    private void retirePilot() {
        ProfileDAO p = activePilot();
        if (p == null) {
            return;
        }
        profiles.remove(p);
        // Hand the active flag to a real pilot if any is left.
        ProfileDAO next = null;
        for (ProfileDAO q : profiles.getList()) {
            q.setActive(false);
            if (next == null && q.getType() == ProfileDAO.Type.PROFILE) {
                next = q;
            }
        }
        if (next != null) {
            next.setActive(true);
        }
        profiles.write();
        openPanel(Panel.PILOTS);
    }

    private void buildResults() {
        final List<ResultDAO> all = new ArrayList<ResultDAO>();
        ProfileDAO p = activePilot();
        if (p != null) {
            for (Rules.StartLevel l : Rules.StartLevel.values()) {
                all.addAll(profiles.getActiveProfileResultList(l.name()));
            }
        }
        Collections.sort(all, new Comparator<ResultDAO>() {
            @Override
            public int compare(ResultDAO a, ResultDAO b) {
                return b.getDate().compareTo(a.getDate());
            }
        });
        if (all.isEmpty()) {
            panel.add(new Row.Text("No flights logged yet. Every run is recorded here: score, time in "
                    + "the sector, crew rescued and how it ended.", smallFont));
        }
        for (final ResultDAO r : all) {
            String label = formatDate(r) + "  " + levelName(r.getLevel());
            panel.add(new Row.Action(label, smallFont, null).withValue(formatScore(r.getCurrent().getScore())));
        }
        panel.onFocus((row, index) -> {
            int i = index - (all.isEmpty() ? 1 : 0);
            if (i >= 0 && i < all.size()) {
                detailResult = all.get(i).getCurrent();
                detailTitle = formatDate(all.get(i)) + "  " + levelName(all.get(i).getLevel());
            }
        });
        panel.add(new Row.Action("BACK", rowFont, this::closePanel));
    }

    private void buildCodex() {
        if (codex == null) {
            codex = new Codex();
        }
        final List<Codex.Entry> byRow = new ArrayList<Codex.Entry>();
        String lastCategory = null;
        for (Codex.Entry e : codex.entries()) {
            String group = e.category.startsWith("ITEM") || e.category.equals("SYSTEM") ? "ITEMS"
                    : e.category.equals("PHENOMENON") ? "THREATS" : e.category + (e.category.equals("CREW") ? "" : "S");
            if (!group.equals(lastCategory)) {
                lastCategory = group;
                panel.add(new Row.Info("- " + group + " -", smallFont, null));
                byRow.add(null);
            }
            panel.add(new Row.Action(e.name, smallFont, null));
            byRow.add(e);
        }
        panel.onFocus((row, index) -> {
            if (index >= 0 && index < byRow.size() && byRow.get(index) != null) {
                codex.select(byRow.get(index));
            }
        });
        panel.add(new Row.Action("BACK", rowFont, this::closePanel));
    }

    private void buildSettings() {
        final SettingsDAO s = Profiles.getSettings();
        panel.add(new Row.Slider("SOUND", rowFont, s::getSoundVolume, v -> {
            s.setSoundVolume(v);
            applyAudio(s);
        }));
        panel.add(new Row.Slider("MUSIC", rowFont, () -> s.isMusic() ? s.getMusicVolume() : 0f, v -> {
            s.setMusic(v > 0f);
            s.setMusicVolume(Math.max(v, 0.1f));
            applyAudio(s);
        }));
        panel.add(new Row.Slider("EFFECTS", rowFont, () -> s.isEffects() ? s.getEffectsVolume() : 0f, v -> {
            s.setEffects(v > 0f);
            s.setEffectsVolume(Math.max(v, 0.1f));
            applyAudio(s);
        }));
        panel.add(new Row.Choice("DIFFICULTY", rowFont, LEVELS,
                () -> Math.min(s.getLevel(), LEVELS.length - 1),
                v -> {
                    s.setLevel(v);
                    save();
                }));
        panel.add(new Row.Toggle("RELAXED MODE", rowFont, s::isChild, v -> {
            s.setChild(v);
            save();
        }));
        panel.add(new Row.Toggle("SHADERS", rowFont, s::isShader, v -> {
            s.setShader(v);
            save();
        }));
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen)) {
            panel.add(new Row.Toggle("TOUCH JOYSTICK", rowFont, s::isJoystick, v -> {
                s.setJoystick(v);
                save();
            }));
        }
        panel.add(new Row.Action("BACK", rowFont, this::closePanel));
    }

    private void buildAbout() {
        panel.add(new Row.Text("1991. The Buran program was never cancelled. It went quiet. "
                + "Flight nineteen left for Object-19, a station parked around an anomaly no one could explain. "
                + "The experiment failed. The station broke apart, the crew drifted into the field, and the "
                + "rift opened in the middle of it all. Mission control stopped answering. The country that "
                + "sent you is gone. Bring your people home.", smallFont));
        panel.add(new Row.Info("LEFT STICK", smallFont, "FLY"));
        panel.add(new Row.Info("A / RT", smallFont, "USE ITEM"));
        panel.add(new Row.Info("LB / RB", smallFont, "SWITCH ITEM"));
        panel.add(new Row.Info("Y", smallFont, "EMP PULSE"));
        panel.add(new Row.Info("MENU", smallFont, "PAUSE"));
        panel.add(new Row.Text("Made by Vitaly Solovev with FlameXander, Pavel Guryanov and htfdream. "
                + "github.com/soloyes/OpenShuttle", smallFont));
        panel.add(new Row.Action("BACK", rowFont, this::closePanel));
    }

    private void applyAudio(SettingsDAO s) {
        boomBox.setAllMusicVolume(s.overallMusicVolume());
        if (s.isMusic()) {
            boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
        } else {
            boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
            boomBox.pauseAllMusic();
        }
        save();
    }

    private void save() {
        profiles.write();
    }

    // ------------------------------------------------------------------ input

    @Override
    public void nav(Nav nav) {
        if (open != Panel.NONE) {
            panel.nav(nav);
        } else {
            home.nav(nav);
        }
    }

    @Override
    public boolean remove() {
        MenuNav.clear(this);
        return super.remove();
    }

    // ------------------------------------------------------------------ draw

    @Override
    public void act(float delta) {
        super.act(delta);
        if (codex != null) {
            codex.update(delta);
        }
        float target = open != Panel.NONE ? 1f : 0f;
        panelAlpha += (target - panelAlpha) * Math.min(1f, delta * 10f);
        home.getColor().a = open != Panel.NONE && !Theme.portrait() ? 0.45f : 1f;
    }

    /** True while a panel covers the scene (the hero dims). */
    public boolean isPanelOpen() {
        return open != Panel.NONE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boolean portrait = Theme.portrait();
        float mx = Theme.marginX();
        float titleY = portrait ? Rules.WORLD_HEIGHT * 0.90f : Rules.WORLD_HEIGHT * 0.80f;
        if (!(portrait && open != Panel.NONE)) {
            drawText(batch, titleFont, "BURAN-19", mx, titleY, Theme.TEXT, parentAlpha);
            drawText(batch, subFont, "THE LAST FLIGHT  -  1991", mx + 6, titleY - titleFont.getLineHeight() + 10,
                    Theme.DIM, parentAlpha);
        }

        if (panelAlpha > 0.01f) {
            float a = parentAlpha * panelAlpha;
            Theme.frame(batch, panelX(), panelY(), panelW(), panelH(), a);
            drawText(batch, headFont, panelTitle(), panelX() + 66f, panelY() + panelH() - 50f, Theme.TEXT, a);
            if (hasDetail()) {
                drawDetail(batch, a);
            }
        }
        super.draw(batch, parentAlpha);
    }

    private void drawDetail(Batch batch, float a) {
        float head = 170f;
        float px = panelX() + 30f;
        float pw = panelW() - 60f;
        float py = panelY() + 40f;
        float ph = panelH() - head - 60f;
        float x;
        float y;
        float w;
        if (Theme.portrait()) {
            x = px + 36f;
            w = pw - 72f;
            y = py + ph * 0.40f;
        } else {
            x = px + pw * 0.5f + 60f;
            w = pw * 0.5f - 96f;
            y = py + ph;
        }
        Theme.rect(batch, Theme.portrait() ? px : px + pw * 0.5f + 20f, Theme.portrait() ? y + 24f : py,
                Theme.portrait() ? pw : 3f, Theme.portrait() ? 3f : ph, Theme.FAINT, a);

        if (open == Panel.RESULTS && detailResult != null) {
            DetailedResultDAO d = detailResult;
            y -= 20f;
            drawText(batch, smallFont, detailTitle, x, y, Theme.TEXT, a);
            y -= 90f;
            String[][] lines = {
                    {"SCORE", formatScore(d.getScore())},
                    {"STEP REACHED", String.valueOf(d.getLevelAchieved())},
                    {"TIME IN SECTOR", formatTime(d.getTime())},
                    {"COLLAPSE TIME", formatTime(d.getDeathModeTime())},
                    {"CREW RESCUED", String.valueOf(d.getAstronautsTaken())},
                    {"ANOMALIES DESTROYED", String.valueOf(d.getEnemiesDestroyed())},
                    {"ASTEROIDS MINED", String.valueOf(d.getMinedAsteroids())},
                    {"ITEMS PICKED / USED", d.getPickUpItems() + " / " + d.getUsedItems()},
                    {"DAMAGE DEALT / TAKEN", d.getDmgDone() + " / " + d.getDmgReceived()},
                    {"EFFECTIVENESS", d.getEffectiveness() + " / 100"},
            };
            for (String[] l : lines) {
                drawText(batch, smallFont, l[0], x, y, Theme.DIM, a);
                layout.setText(smallFont, l[1]);
                drawText(batch, smallFont, l[1], x + w - layout.width, y, Theme.TEXT, a);
                y -= 66f;
            }
        } else if (open == Panel.CODEX && codex != null) {
            float box = Theme.portrait() ? 190f : 330f;
            codex.draw(batch, x, y - 10f, w, a, headFont, smallFont, box);
        }
    }

    private void drawText(Batch batch, BitmapFont font, String text, float x, float y, Color c, float a) {
        if (text == null) {
            return;
        }
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * a);
        font.draw(batch, text, x, y);
        font.setColor(old);
    }

    // ------------------------------------------------------------------ formatting

    private static String stripMarkup(String s) {
        StringBuilder b = new StringBuilder();
        boolean tag = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                tag = true;
            } else if (c == ']' && tag) {
                tag = false;
            } else if (!tag) {
                b.append(c);
            }
        }
        return b.toString();
    }

    private static String stickerName(Sticker s) {
        String plain = stripMarkup(s.getDescription());
        int nl = plain.indexOf('\n');
        return (nl > 0 ? plain.substring(0, nl) : s.name()).trim();
    }

    private static String stickerBody(Sticker s) {
        String plain = stripMarkup(s.getDescription());
        int nl = plain.indexOf('\n');
        return nl > 0 ? plain.substring(nl + 1).trim() : plain;
    }

    private static String levelName(String level) {
        if ("LOW".equals(level)) return "EASY";
        if ("MEDIUM".equals(level)) return "NORMAL";
        if ("HIGH".equals(level)) return "HARD";
        return level;
    }

    private static String formatDate(ResultDAO r) {
        Calendar c = Calendar.getInstance();
        c.setTime(r.getDate());
        return c.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR);
    }

    private static String formatScore(long score) {
        String s = String.valueOf(Math.abs(score));
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (s.length() - i) % 3 == 0) {
                b.append(' ');
            }
            b.append(s.charAt(i));
        }
        return (score < 0 ? "-" : "") + b;
    }

    private static String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return m + ":" + (s < 10 ? "0" : "") + s;
    }
}
