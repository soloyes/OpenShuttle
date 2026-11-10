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

    private enum Panel { NONE, PLAY, PILOTS, RESULTS, CODEX, SETTINGS, ABOUT }

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
    private final BitmapFont labelFont;
    /** Panel header (title) and footer (button hints) heights. */
    private static final float HEAD = 170f;
    private static final float FOOT = 96f;
    private float clock;
    private final GlyphLayout layout = new GlyphLayout();
    private final StickerResolver stickers = new StickerResolver();
    private float panelAlpha;
    private Row.Action pilotRow;
    private boolean retireArmed;

    // Detail pane content (results / codex).
    private DetailedResultDAO detailResult;
    private Codex codex;
    private Codex.Group codexGroup;
    private BackButton backButton;

    /** "B  -  BACK" in the panel footer: B on a pad, a tap or click on the hint. */
    private class BackButton extends com.badlogic.gdx.scenes.scene2d.Actor {
        BackButton() {
            addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    if (open != Panel.NONE) {
                        boomBox.playSound(MSConstants.UI_CLICK);
                        panelBack();
                    }
                }
            });
        }

        String key() {
            return Hints.button("B", "ESC");
        }

        void place() {
            float w = Theme.hintWidth(smallFont, key(), "BACK") + 40f;
            setSize(w, FOOT);
            setPosition(panelX() + panelW() - w - 30f, panelY());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float a = parentAlpha * panelAlpha;
            Theme.hint(batch, smallFont, key(), "BACK", getX() + 20f, panelY() + FOOT / 2f, isOver() ? Theme.AMBER : Theme.DIM, a);
        }

        private boolean isOver() {
            return ((com.badlogic.gdx.scenes.scene2d.utils.ClickListener) getListeners().first()).isOver();
        }
    }

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
        labelFont = Theme.font(32);

        buildHome();
        addActor(home);
        addActor(panel);
        backButton = new BackButton();
        backButton.setVisible(false);
        addActor(backButton);
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

    /** Share of the panel width taken by the list when a detail pane is shown. */
    private float listShare() {
        return open == Panel.CODEX ? 0.3f : 0.5f;
    }

    private boolean hasDetail() {
        return open == Panel.RESULTS || open == Panel.CODEX;
    }

    /** Footer hint baseline. */
    private float footTextY() {
        return panelY() + FOOT / 2f + smallFont.getCapHeight() / 2f;
    }

    /** The home list sits in its own instrument ("FLIGHT DECK") under the title. */
    private float homeTop() {
        return Rules.WORLD_HEIGHT * 0.56f;
    }

    private void layoutLists() {
        float mx = Theme.marginX();
        if (Theme.portrait()) {
            home.setBox(mx - 36f, Rules.WORLD_HEIGHT * 0.07f, Rules.WORLD_WIDTH - 2 * mx + 72f, Rules.WORLD_HEIGHT * 0.36f);
        } else {
            float h = home.contentHeight();
            home.setBox(mx - 36f, homeTop() - h, Rules.WORLD_WIDTH * 0.24f, h);
        }
        float head = HEAD;
        float px = panelX() + 30f;
        float pw = panelW() - 60f;
        float py = panelY() + FOOT + 14f;
        float ph = panelH() - head - FOOT - 34f;
        if (hasDetail()) {
            if (Theme.portrait()) {
                float listH = ph * (open == Panel.CODEX ? 0.30f : 0.40f);
                panel.setBox(px, py + ph - listH, pw, listH);
            } else {
                panel.setBox(px, py, pw * listShare(), ph);
            }
        } else {
            panel.setBox(px, py, pw, ph);
        }
    }

    // ------------------------------------------------------------------ home

    private void buildHome() {
        home.clearRows();
        home.add(new Row.Action("PLAY", rowFont, () -> openPanel(Panel.PLAY)));
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

    /** Campaign sectors (locked beyond progress) and the endless arcade run. */
    private void buildPlay() {
        int unlocked = com.buran.game.campaign.Campaign.unlocked();
        for (final com.buran.game.campaign.Sector sec : com.buran.game.campaign.Campaign.sectors()) {
            final boolean open = sec.number <= unlocked;
            Row.Action row = panel.add(new Row.Action("SECTOR " + sec.code() + "   " + sec.name, rowFont, () -> {
                if (open) {
                    com.buran.game.campaign.Campaign.start(sec);
                    play();
                } else {
                    boomBox.playSound(MSConstants.UI_EMPTY_NAME);
                }
            }));
            if (com.buran.game.campaign.Campaign.isDone(sec.number)) {
                row.withValue("CLEAR", Theme.OK);
            } else if (open) {
                row.withValue(sec.goalText(), Theme.ACCENT);
            } else {
                row.withValue("LOCKED", Theme.FAINT);
            }
        }
        panel.add(new Row.Info("SECTORS 04 - 19", smallFont, "SIGNAL LOST"));
        panel.add(new Row.Action("ARCADE   -   ENDLESS", rowFont, () -> {
            com.buran.game.campaign.Campaign.arcade();
            play();
        }));
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
            case PLAY: buildPlay(); break;
            case PILOTS: buildPilots(); break;
            case RESULTS: buildResults(); break;
            case CODEX: buildCodex(); break;
            case SETTINGS: buildSettings(); break;
            case ABOUT: buildAbout(); break;
            default: break;
        }
        panel.onBack(this::panelBack);
        layoutLists();
        backButton.place();
        backButton.setVisible(true);
        panel.setVisible(true);
        home.setVisible(!Theme.portrait());
        home.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        panel.focusFirst();
    }

    private void closePanel() {
        open = Panel.NONE;
        codexGroup = null;
        backButton.setVisible(false);
        panel.setVisible(false);
        home.setVisible(true);
        home.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
        refreshPilotRow();
        home.focusFirst();
    }

    private String panelTitle() {
        switch (open) {
            case PLAY: return "FLIGHT PLAN";
            case PILOTS: return "PILOTS";
            case RESULTS: return "FLIGHT LOG";
            case CODEX: return codexGroup == null ? "CODEX" : "CODEX / " + codexGroup.title();
            case SETTINGS: return "SETTINGS";
            case ABOUT: return "ABOUT";
            default: return "";
        }
    }

    /** Instrument code shown in the panel header, like a plate on a ship's console. */
    private String panelCode() {
        switch (open) {
            case PLAY: return "NAV-01";
            case PILOTS: return "CREW-02";
            case RESULTS: return "LOG-03";
            case CODEX: return "DB-04";
            case SETTINGS: return "SYS-05";
            case ABOUT: return "ARC-06";
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
    }

    private void buildCodex() {
        if (codex == null) {
            codex = new Codex();
        }
        if (codexGroup == null) {
            // Top level: one row per section, opening its own list.
            for (final Codex.Group g : codex.groups()) {
                panel.add(new Row.Action(g.title(), rowFont, () -> {
                    codexGroup = g;
                    reopenPanel();
                }).withValue(String.valueOf(g.entries().size())));
            }
            panel.onFocus((row, index) -> {
                if (index >= 0 && index < codex.groups().size()) {
                    codex.select(codex.groups().get(index).entries().get(0));
                }
            });
        } else {
            final List<Codex.Entry> entries = codexGroup.entries();
            for (Codex.Entry e : entries) {
                panel.add(new Row.Action(e.name(), smallFont, null));
            }
            panel.onFocus((row, index) -> {
                if (index >= 0 && index < entries.size()) {
                    codex.select(entries.get(index));
                }
            });
        }
    }

    /** Rebuilds the open panel in place (used by the codex sections). */
    private void reopenPanel() {
        Panel p = open;
        Codex.Group g = codexGroup;
        openPanel(p);
        codexGroup = g;
    }

    /** BACK inside a panel: up one codex level, otherwise close. */
    private void panelBack() {
        if (open == Panel.CODEX && codexGroup != null) {
            Codex.Group from = codexGroup;
            codexGroup = null;
            openPanel(Panel.CODEX);
            // Keep the section we came from focused.
            int idx = codex.groups().indexOf(from);
            for (int k = 0; k < idx; k++) {
                panel.nav(Nav.DOWN);
            }
            return;
        }
        closePanel();
    }

    private void buildSettings() {
        final SettingsDAO s = Profiles.getSettings();
        // One master volume is redundant: music and effects have their own sliders.
        s.setSoundVolume(1f);
        panel.add(new Row.Slider("MUSIC", rowFont, () -> s.isMusic() ? s.getMusicVolume() : 0f, v -> {
            boolean was = s.isMusic();
            s.setMusic(v > 0f);
            s.setMusicVolume(Math.max(v, 0.1f));
            applyAudio(s, was);
        }));
        panel.add(new Row.Slider("EFFECTS", rowFont, () -> s.isEffects() ? s.getEffectsVolume() : 0f, v -> {
            s.setEffects(v > 0f);
            s.setEffectsVolume(Math.max(v, 0.1f));
            applyAudio(s, s.isMusic());
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
        panel.add(new Row.Toggle("SUBTITLES", rowFont, s::isSubtitles, v -> {
            s.setSubtitles(v);
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
    }

    /** Volume changes apply live; the menu song only (re)starts when music is switched on. */
    private void applyAudio(SettingsDAO s, boolean musicWasOn) {
        boomBox.setAllMusicVolume(s.overallMusicVolume());
        if (!s.isMusic()) {
            boomBox.pauseAllMusic();
        } else if (!musicWasOn) {
            boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
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
        clock += delta;
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
            drawText(batch, labelFont, "ORBITAL SHIP 11F35  -  FLIGHT 19", mx + 6, titleY + 44f, Theme.HEAD, parentAlpha);
            drawText(batch, titleFont, "BURAN-19", mx, titleY, Theme.TEXT, parentAlpha);
            drawText(batch, subFont, "THE LAST FLIGHT  -  1991", mx + 6, titleY - titleFont.getLineHeight() + 10,
                    Theme.DIM, parentAlpha);
        }
        if (!portrait) {
            drawHomeDeck(batch, parentAlpha * home.getColor().a);
            drawStatusStrip(batch, parentAlpha);
        }

        if (panelAlpha > 0.01f) {
            float a = parentAlpha * panelAlpha;
            float x = panelX();
            float y = panelY();
            float w = panelW();
            float h = panelH();
            Theme.panel(batch, x, y, w, h, a);
            drawText(batch, headFont, panelTitle(), x + 66f, y + h - 50f, Theme.TEXT, a);
            // Header plate: instrument code and a lit lamp.
            float plateY = y + h - 62f;
            Theme.textR(batch, labelFont, panelCode(), x + w - 84f, plateY, Theme.HEAD, a);
            Theme.lamp(batch, x + w - 66f, plateY - 20f, Theme.OK, true, a);
            Theme.hairline(batch, x + 30f, y + h - HEAD + 6f, w - 60f, a);
            // Footer: what the buttons do here.
            Theme.hairline(batch, x + 30f, y + FOOT, w - 60f, a);
            if (!Hints.touch()) {
                float hx = x + 66f;
                float cy = y + FOOT / 2f;
                hx += Theme.hint(batch, smallFont, Hints.button("A", "ENTER"), "SELECT", hx, cy, Theme.DIM, a) + 60f;
                if (open == Panel.SETTINGS) {
                    Theme.hint(batch, smallFont, Hints.button("D-PAD", "ARROWS"), "ADJUST", hx, cy, Theme.DIM, a);
                }
            }
            if (hasDetail()) {
                drawDetail(batch, a);
            }
        }
        super.draw(batch, parentAlpha);
    }

    /** The home list's instrument: a panel sized to the list, with its plate. */
    private void drawHomeDeck(Batch batch, float a) {
        float bx = Theme.marginX() - 36f;
        float bw = Rules.WORLD_WIDTH * 0.24f;
        float h = home.contentHeight();
        float top = homeTop();
        float plate = 80f;
        float x = bx - 30f;
        float y = top - h - 30f;
        float w = bw + 60f;
        float ph = h + 30f + plate;
        Theme.panel(batch, x, y, w, ph, a);
        drawText(batch, labelFont, "FLIGHT DECK", x + 66f, y + ph - 28f, Theme.HEAD, a);
        Theme.lamp(batch, x + w - 48f, y + ph - 50f, Theme.OK, true, a);
        Theme.hairline(batch, x + 30f, top + 14f, w - 60f, a);
    }

    /**
     * A thin telemetry strip along the bottom: the radio status (no one answers), the pilot,
     * mission elapsed time. Quiet, but alive: the lamp blinks and the clock runs.
     */
    private void drawStatusStrip(Batch batch, float a) {
        float x = Rules.SAFE_X;
        float y = Rules.SAFE_Y;
        float w = Rules.WORLD_WIDTH - 2f * Rules.SAFE_X;
        float h = 76f;
        Theme.panel(batch, x, y, w, h, a);
        float ty = y + h / 2f + labelFont.getCapHeight() / 2f;
        boolean blink = (clock % 1.6f) < 0.8f;
        Theme.lamp(batch, x + 44f, y + h / 2f - 7f, Theme.ACCENT, blink, a);
        drawText(batch, labelFont, "MISSION CONTROL  -  NO CARRIER", x + 80f, ty, Theme.HEAD, a);
        ProfileDAO p = activePilot();
        String mid = "PILOT  " + (p == null ? "NONE" : p.getUsername()) + "      SECTORS  "
                + com.buran.game.campaign.Campaign.unlocked() + " / 19";
        float mw = Theme.width(labelFont, mid);
        drawText(batch, labelFont, mid, x + w / 2f - mw / 2f, ty, Theme.HEAD, a);
        int t = 412 * 3600 + 7 * 60 + (int) clock;
        String met = "MET  " + t / 3600 + ":" + pad2(t / 60 % 60) + ":" + pad2(t % 60);
        Theme.textR(batch, labelFont, met, x + w - 44f, ty, Theme.HEAD, a);
    }

    private static String pad2(int v) {
        return v < 10 ? "0" + v : String.valueOf(v);
    }

    private void drawDetail(Batch batch, float a) {
        float head = HEAD;
        float px = panelX() + 30f;
        float pw = panelW() - 60f;
        float py = panelY() + FOOT + 14f;
        float ph = panelH() - head - FOOT - 34f;
        float x;
        float y;
        float w;
        if (Theme.portrait()) {
            x = px + 36f;
            w = pw - 72f;
            y = open == Panel.CODEX ? py + ph * 0.70f - 30f : py + ph * 0.40f;
        } else {
            float share = listShare();
            x = px + pw * share + 60f;
            w = pw * (1f - share) - 96f;
            y = py + ph;
        }
        Theme.rect(batch, Theme.portrait() ? px : px + pw * listShare() + 20f, Theme.portrait() ? y + 24f : py,
                Theme.portrait() ? pw : 2f, Theme.portrait() ? 2f : ph, Theme.EDGE, a);

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
            // Big scanner frame in the middle of the detail pane, the note under it.
            float box = Theme.portrait() ? Math.min(w * 0.62f, ph * 0.36f) : Math.min(w * 0.62f, ph * 0.56f);
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
