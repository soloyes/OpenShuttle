package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.profiles.SettingsDAO;
import com.buran.game.ui.groups.mvc.results.DetailedResultDAO;
import com.buran.game.utils.Restart;
import com.buran.game.utils.StickerResolver;

/**
 * In-game overlay in the menu style: the pause menu (resume, audio, restart, quit, crew
 * summary) and the end-of-run report.
 */
public class GameMenu extends Group implements MenuNav.Target {
    private enum Mode { HIDDEN, PAUSE, REPORT, CLEAR }

    private final MenuList list = new MenuList();
    private final BoomBox boomBox = new BoomBox();
    private final StickerResolver stickers = new StickerResolver();
    private final BitmapFont headFont;
    private final BitmapFont rowFont;
    private final BitmapFont smallFont;
    private final GlyphLayout layout = new GlyphLayout();
    private Mode mode = Mode.HIDDEN;
    private float alpha;
    private boolean restartArmed;
    private boolean quitArmed;
    private DetailedResultDAO report;

    public GameMenu() {
        setTransform(false);
        boolean portrait = Theme.portrait();
        headFont = Theme.font(portrait ? 72 : 96);
        rowFont = Theme.font(portrait ? 64 : 72);
        smallFont = Theme.font(44);
        addActor(list);
        list.setVisible(false);
        setTouchable(Touchable.childrenOnly);
    }

    // ------------------------------------------------------------------ layout

    private float boxX() {
        return Theme.portrait() ? Rules.WORLD_WIDTH * 0.05f : Rules.WORLD_WIDTH * 0.2f;
    }

    private float boxW() {
        return Theme.portrait() ? Rules.WORLD_WIDTH * 0.90f : Rules.WORLD_WIDTH * 0.6f;
    }

    private float boxY() {
        return Theme.portrait() ? Rules.WORLD_HEIGHT * 0.06f : Rules.WORLD_HEIGHT * 0.12f;
    }

    private float boxH() {
        return Theme.portrait() ? Rules.WORLD_HEIGHT * 0.78f : Rules.WORLD_HEIGHT * 0.72f;
    }

    private void layoutList() {
        float px = boxX() + 30f;
        float pw = boxW() - 60f;
        float py = boxY() + 40f;
        float ph = boxH() - 230f;
        if (Theme.portrait()) {
            list.setBox(px, py, pw, ph * 0.58f);
        } else {
            list.setBox(px, py, pw * 0.5f, ph);
        }
    }

    // ------------------------------------------------------------------ modes

    private void showPause() {
        mode = Mode.PAUSE;
        restartArmed = false;
        quitArmed = false;
        list.clearRows();
        final SettingsDAO s = Profiles.getSettings();
        list.add(new Row.Action("RESUME", rowFont, PauseGroup::togglePause));
        // One master volume is redundant: music and effects have their own sliders.
        s.setSoundVolume(1f);
        list.add(new Row.Slider("MUSIC", rowFont, () -> s.isMusic() ? s.getMusicVolume() : 0f, v -> {
            s.setMusic(v > 0f);
            s.setMusicVolume(Math.max(v, 0.1f));
            applyAudio(s);
        }));
        list.add(new Row.Slider("EFFECTS", rowFont, () -> s.isEffects() ? s.getEffectsVolume() : 0f, v -> {
            s.setEffects(v > 0f);
            s.setEffectsVolume(Math.max(v, 0.1f));
            applyAudio(s);
        }));
        list.add(new Row.Toggle("SHADERS", rowFont, s::isShader, v -> {
            s.setShader(v);
            new Profiles().write();
        }));
        list.add(new Row.Action("RESTART", rowFont, null) {
            @Override
            protected boolean onNav(Nav nav) {
                if (nav != Nav.ACCEPT) {
                    return false;
                }
                if (!restartArmed) {
                    restartArmed = true;
                    setLabel("PRESS AGAIN TO RESTART");
                    return true;
                }
                restart();
                return true;
            }
        });
        list.add(new Row.Action("RETURN TO BASE", rowFont, null) {
            @Override
            protected boolean onNav(Nav nav) {
                if (nav != Nav.ACCEPT) {
                    return false;
                }
                if (!quitArmed) {
                    quitArmed = true;
                    setLabel("PRESS AGAIN TO ABORT RUN");
                    return true;
                }
                quit();
                return true;
            }
        });
        list.onBack(PauseGroup::togglePause);
        open();
    }

    private com.buran.game.campaign.Sector clearedSector;

    /** Sector clear: next sector or back to base. */
    private void showClear(com.buran.game.campaign.SectorRun run) {
        if (mode == Mode.CLEAR) {
            return;
        }
        mode = Mode.CLEAR;
        clearedSector = run.sector();
        report = Standing.getInstance().getDetails();
        list.clearRows();
        final com.buran.game.campaign.Sector next = com.buran.game.campaign.Campaign.next(clearedSector);
        if (next != null) {
            list.add(new Row.Action("NEXT: SECTOR " + next.code(), rowFont, () -> {
                com.buran.game.campaign.Campaign.start(next);
                restart();
            }));
        }
        list.add(new Row.Action("RETURN TO BASE", rowFont, this::quit));
        list.onBack(this::quit);
        open();
    }

    /** Called once when the run ends. */
    public void showReport() {
        if (mode == Mode.REPORT) {
            return;
        }
        mode = Mode.REPORT;
        report = Standing.getInstance().getDetails();
        list.clearRows();
        list.add(new Row.Action("FLY AGAIN", rowFont, this::restart));
        list.add(new Row.Action("RETURN TO BASE", rowFont, this::quit));
        list.onBack(this::quit);
        open();
    }

    private void open() {
        layoutList();
        list.setVisible(true);
        list.focusFirst();
        MenuNav.set(this);
    }

    private void hide() {
        mode = Mode.HIDDEN;
        list.setVisible(false);
        MenuNav.clear(this);
    }

    private void restart() {
        MenuNav.clear(this);
        com.buran.game.campaign.Campaign.setRun(null);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
        Restart.doRestart();
    }

    private void quit() {
        MenuNav.clear(this);
        com.buran.game.campaign.Campaign.setRun(null);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        Restart.doRestart();
    }

    private void applyAudio(SettingsDAO s) {
        boomBox.setAllMusicVolume(s.overallMusicVolume());
        new Profiles().write();
    }

    @Override
    public void nav(Nav nav) {
        if (mode != Mode.HIDDEN) {
            list.nav(nav);
        }
    }

    // ------------------------------------------------------------------ frame

    @Override
    public void act(float delta) {
        super.act(delta);
        com.buran.game.campaign.SectorRun run = com.buran.game.campaign.Campaign.run();
        if (run != null && run.isClear() && !Rules.GAME_OVER) {
            showClear(run);
        } else if (Rules.GAME_OVER) {
            showReport();
        } else if (PauseGroup.isPause()) {
            if (mode != Mode.PAUSE) {
                boomBox.playSound(MSConstants.UI_MENU);
                showPause();
            }
        } else if (mode != Mode.HIDDEN) {
            hide();
        }
        float target = mode == Mode.HIDDEN ? 0f : 1f;
        alpha += (target - alpha) * Math.min(1f, delta * (Rules.GAME_OVER || mode == Mode.CLEAR ? 2f : 10f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (alpha < 0.01f) {
            return;
        }
        float a = parentAlpha * alpha;
        // Other actors (the item carousel) leave the grey pause shader on the batch.
        batch.setShader(null);
        Theme.frame(batch, boxX(), boxY(), boxW(), boxH(), a);
        String title = mode == Mode.REPORT ? "SIGNAL LOST" : mode == Mode.CLEAR ? "SECTOR " + clearedSector.code() + " CLEAR" : "PAUSED";
        draw(batch, headFont, title, boxX() + 66f, boxY() + boxH() - 50f,
                mode == Mode.REPORT ? Theme.ACCENT : mode == Mode.CLEAR ? Theme.OK : Theme.TEXT, a);
        String sub = mode == Mode.REPORT ? "FLIGHT REPORT" : mode == Mode.CLEAR ? clearedSector.name + "  -  CREW AND CARGO SECURED"
                : "MISSION CONTROL: NO SIGNAL";
        draw(batch, smallFont, sub, boxX() + 66f, boxY() + boxH() - 50f - headFont.getLineHeight() + 6f, Theme.DIM, a);
        drawSummary(batch, a);
        super.draw(batch, parentAlpha * alpha);
    }

    private void drawSummary(Batch batch, float a) {
        float px = boxX() + 30f;
        float pw = boxW() - 60f;
        float py = boxY() + 40f;
        float ph = boxH() - 230f;
        float x;
        float y;
        float w;
        if (Theme.portrait()) {
            x = px + 36f;
            w = pw - 72f;
            y = py + ph;
            Theme.rect(batch, px, py + ph * 0.58f + 20f, pw, 3f, Theme.FAINT, a);
        } else {
            x = px + pw * 0.5f + 60f;
            w = pw * 0.5f - 96f;
            y = py + ph;
            Theme.rect(batch, px + pw * 0.5f + 20f, py, 3f, ph, Theme.FAINT, a);
        }
        Standing st = Standing.getInstance();
        DetailedResultDAO d = (mode == Mode.REPORT || mode == Mode.CLEAR) && report != null ? report : st.getDetails();
        String[][] lines = {
                {"SCORE", formatScore(d.getScore())},
                {"STEP", st.getPlayingLevel() + " / 19"},
                {"CREW RESCUED", String.valueOf(d.getAstronautsTaken())},
                {"ANOMALIES DESTROYED", String.valueOf(d.getEnemiesDestroyed())},
                {"ASTEROIDS MINED", String.valueOf(d.getMinedAsteroids())},
                {"EFFECTIVENESS", d.getEffectiveness() + " / 100"},
        };
        for (String[] l : lines) {
            line(batch, l[0], l[1], x, y, w, a);
            y -= 64f;
        }
        if (Theme.portrait()) {
            return;   // no room for the crew table on a phone
        }
        y -= 30f;
        draw(batch, smallFont, "CREW ABOARD", x, y, Theme.TEXT, a);
        y -= 74f;
        float icon = 64f;
        for (Sticker crew : Sticker.ASTRONAUTS) {
            Integer n = st.getAstronauts() == null ? null : st.getAstronauts().get(crew);
            TextureRegion r = stickers.getRegion(crew);
            if (r != null) {
                batch.setColor(1, 1, 1, a);
                batch.draw(r, x, y - icon + 14f, icon, icon);
                batch.setColor(Color.WHITE);
            }
            String name = crew.name();
            line(batch, "   " + name, (n == null ? 0 : n) + "   x" + round1(crew.getCoefficient()), x + icon - 20f, y, w - icon + 20f, a);
            y -= 70f;
        }
    }

    private void line(Batch batch, String label, String value, float x, float y, float w, float a) {
        draw(batch, smallFont, label, x, y, Theme.DIM, a);
        layout.setText(smallFont, value);
        draw(batch, smallFont, value, x + w - layout.width, y, Theme.TEXT, a);
    }

    private void draw(Batch batch, BitmapFont font, String text, float x, float y, Color c, float a) {
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * a);
        font.draw(batch, text, x, y);
        font.setColor(old);
    }

    private static String round1(float v) {
        int t = Math.round(v * 10f);
        return (t / 10) + "." + (t % 10);
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
}
