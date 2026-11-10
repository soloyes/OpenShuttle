package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.buran.game.conf.PaintConstants;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Regions;
import com.buran.game.utils.StickerResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * The codex: every threat, phenomenon, crew member, container and item with a live
 * hologram animation and a short note typed out like on a terminal.
 */
public class Codex {
    /** One codex entry. */
    public static class Entry {
        final String name;
        final String category;
        final TextureRegion[] frames;
        final float fps;
        final TextureRegion badge;
        final String text;

        Entry(String name, String category, TextureRegion[] frames, float fps, TextureRegion badge, String text) {
            this.name = name;
            this.category = category;
            this.frames = frames;
            this.fps = fps;
            this.badge = badge;
            this.text = text;
        }
    }

    private static final float TYPE_SPEED = 55f;   // characters per second

    private final List<Entry> entries = new ArrayList<Entry>();
    private final GlyphLayout layout = new GlyphLayout();
    private Entry current;
    private float time;
    private float typed;

    public Codex() {
        StickerResolver icons = new StickerResolver();
        TextureRegion[] astronaut = Regions.split(region(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);

        threat("CORROSIVE ANOMALY", sheet(PaintConstants.ANOMALY_CORROSIVE_REGION, 3, 4, PaintConstants.ANOMALY_CORROSIVE_FRAMES), 14f,
                "Acid clouds left behind by the experiment. Colour shows strength: tan, green, yellow, red. "
                        + "They drift until the anomaly timer wakes them, then they hunt. Ram them when your shield is "
                        + "stronger, burn them, or steer asteroids into them.");
        threat("ELECTRIC ANOMALY", sheet(PaintConstants.ANOMALY_ELECTRIC_REGION, 4, 4, PaintConstants.ANOMALY_ELECTRIC_FRAMES), 16f,
                "A fast discharge that never fades on its own. It hunts at high speed and knocks out the radar "
                        + "and laser. Let it come close and fire the EMP pulse (Y), or send rockets after it.");
        threat("RADIATION ANOMALY", sheet(PaintConstants.ANOMALY_RADIATION_REGION, 4, 4, PaintConstants.ANOMALY_RADIATION_FRAMES), 14f,
                "Sticks to the hull and feeds on your score for as long as it holds on. Burns out after two "
                        + "minutes. Rockets break it apart.");
        threat("MAGNETIC ANOMALY", sheet(PaintConstants.ANOMALY_MAGNETIC_REGION, 4, 4, PaintConstants.ANOMALY_MAGNETIC_FRAMES), 14f,
                "Clamps onto the hull and siphons fuel. Burns out after two minutes. Mechanics aboard make the "
                        + "leak smaller. Rockets break it apart.");
        threat("METAL ASTEROID", sheet(PaintConstants.ASTEROID_METAL_REGION, 8, 8, PaintConstants.ASTEROID_METAL_FRAMES), 20f,
                "Wreckage of the station. A collision scrapes the shield. Burn it with the laser and pull the "
                        + "fragments in with the tractor beam: metal charges the ram shield.");
        threat("MINERAL ASTEROID", sheet(PaintConstants.ASTEROID_MINERAL_REGION, 8, 8, PaintConstants.ASTEROID_METAL_FRAMES), 20f,
                "Rock torn from Object-19. Burn it with the laser; mineral fragments recharge the laser itself.");
        add(new Entry("THE RIFT", "PHENOMENON", new TextureRegion[]{region(PaintConstants.BLACKHOLE_REGION)}, 0f, null,
                "The anomaly at the heart of Object-19. Anything inside the capture ring spirals in. Survive the "
                        + "fall and the rift throws you out somewhere else in the sector, hull damaged."));

        crew("COMMANDER", astronaut, icons.getRegion(Sticker.COMMANDER),
                "Raises every score you earn, engine thrust and resistance to the rift's pull.");
        crew("MEDIC", astronaut, icons.getRegion(Sticker.MEDIC),
                "Raises maximum hull. Repairs get stronger and the rift hurts less.");
        crew("SCIENTIST", astronaut, icons.getRegion(Sticker.SCIENTIST),
                "Raises fuel capacity. The laser burns faster and radiation steals less.");
        crew("MECHANIC", astronaut, icons.getRegion(Sticker.MECHANIC),
                "Raises shield capacity. Magnetic anomalies siphon less fuel.");
        crew("MILITARY", astronaut, icons.getRegion(Sticker.MILITARY),
                "Raises laser capacity and the ram power you get from metal.");

        cargo("CRATE", region(PaintConstants.CRATE_REGION),
                "Cargo from the station with one item inside. Gold crates hold three. Asteroids smash crates "
                        + "unless they are shielded.");
        cargo("STORAGE", region(PaintConstants.STORAGE_REGION),
                "A heavy container with one or two items, including laser charge, radar and rockets.");
        cargo("FUEL TANK", region(PaintConstants.FUEL_REGION),
                "Refuels the shuttle. A gold tank fills it to the brim. Breaking one hurts the hull.");
        cargo("SEALED BOX", icons.getRegion(Sticker.PATTERN),
                "Contents unknown. Use SCAN to read what is inside before you open it.");
        cargo("DAMAGED BOX", icons.getRegion(Sticker.SHABBY),
                "Burnt by radiation. Opening it is a gamble: a big reward or a nasty surprise.");

        item(Sticker.FREEZE, "Every anomaly stops hunting for ten seconds.");
        item(Sticker.DEGRADE, "All corrosive anomalies drop to the weakest level.");
        item(Sticker.BOOST, "Triples engine speed for a moment. Above ramming speed whatever you hit breaks.");
        item(Sticker.REPAIR, "Restores hull. Stronger with medics aboard.");
        item(Sticker.SHIELD, "Shields every container in the sector from asteroids and fills the ram shield.");
        item(Sticker.TRASH, "Points, and a volley of asteroids launched around the ship.");
        item(Sticker.SPLASH, "A shockwave that pops corrosive anomalies across the screen.");
        item(Sticker.SCAN, "Reveals what sealed boxes hold for thirty seconds.");
        item(Sticker.LASER, "Automatic. Burns asteroids ahead of the ship while the charge lasts.");
        item(Sticker.RADAR, "Automatic. Marks anomalies around you.");
        item(Sticker.ROCKETS, "Charges for three seconds, then launches one homing rocket per anomaly in range.");
    }

    private static TextureRegion region(String name) {
        return Assets.getInstance().getAtlas().findRegion(name);
    }

    private static TextureRegion[] sheet(String name, int rows, int cols, int frames) {
        return Regions.split(new TextureRegion(region(name)), rows, cols, frames);
    }

    private void add(Entry e) {
        entries.add(e);
    }

    private void threat(String name, TextureRegion[] frames, float fps, String text) {
        add(new Entry(name, "THREAT", frames, fps, null, text));
    }

    private void crew(String name, TextureRegion[] frames, TextureRegion badge, String text) {
        add(new Entry(name, "CREW", frames, 6f, badge,
                text + " Fly close and the tractor beam takes them aboard. Once your shield glows, catch them "
                        + "from the side: a head-on hit kills."));
    }

    private void cargo(String name, TextureRegion frame, String text) {
        add(new Entry(name, "CARGO", new TextureRegion[]{frame}, 0f, null, text));
    }

    private void item(Sticker s, String text) {
        StickerResolver icons = new StickerResolver();
        String name = s.name();
        boolean automatic = s == Sticker.LASER || s == Sticker.RADAR;
        add(new Entry(name, automatic ? "SYSTEM" : "ITEM - USE WITH A", new TextureRegion[]{icons.getRegion(s)}, 0f, null, text));
    }

    public List<Entry> entries() {
        return entries;
    }

    public void select(Entry e) {
        if (e != current) {
            current = e;
            typed = 0f;
        }
    }

    public void update(float dt) {
        time += dt;
        if (current != null) {
            typed = Math.min(current.text.length(), typed + dt * TYPE_SPEED);
        }
    }

    /** Draws the selected entry in the given box (top-left anchored). */
    public void draw(Batch batch, float x, float top, float w, float alpha, BitmapFont titleFont, BitmapFont textFont,
                     float boxSize) {
        if (current == null) {
            return;
        }
        Entry e = current;
        // ---- hologram box
        float bx = x;
        float by = top - boxSize;
        Theme.rect(batch, bx, by, boxSize, boxSize, Theme.LINE, 0.05f * alpha);
        brackets(batch, bx, by, boxSize, alpha);
        TextureRegion frame = e.frames[e.fps > 0 ? ((int) (time * e.fps)) % e.frames.length : 0];
        if (frame != null) {
            float flicker = 0.88f + 0.12f * MathUtils.sin(time * 23f) * MathUtils.sin(time * 7f);
            float size = boxSize * 0.78f;
            float bob = MathUtils.sin(time * 1.6f) * boxSize * 0.02f;
            float rot = e.fps == 0f && e.frames.length == 1 && "THE RIFT".equals(e.name) ? time * 25f : 0f;
            batch.setColor(0.85f, 0.95f, 1f, alpha * flicker);
            batch.draw(frame, bx + (boxSize - size) / 2f, by + (boxSize - size) / 2f + bob, size / 2f, size / 2f,
                    size, size, 1f, 1f, rot);
            batch.setColor(Color.WHITE);
        }
        if (e.badge != null) {
            float b = boxSize * 0.3f;
            batch.setColor(1f, 1f, 1f, alpha);
            batch.draw(e.badge, bx + boxSize - b - 10f, by + 10f, b, b);
            batch.setColor(Color.WHITE);
        }
        // Scan line sweeping down the box.
        float scan = (time * 0.45f) % 1f;
        Theme.rect(batch, bx, by + boxSize * (1f - scan), boxSize, 3f, Theme.LINE, 0.35f * alpha);
        for (int i = 0; i < 16; i++) {
            Theme.rect(batch, bx, by + boxSize * i / 16f, boxSize, 1f, Theme.LINE, 0.04f * alpha);
        }

        // ---- title and category
        float tx = x;
        float ty = by - 40f;
        draw(batch, titleFont, e.name, tx, ty, Theme.TEXT, alpha);
        ty -= titleFont.getLineHeight() * 0.9f;
        draw(batch, textFont, e.category, tx, ty, Theme.ACCENT, alpha);
        ty -= textFont.getLineHeight() * 1.6f;

        // ---- typed note with a blinking cursor
        int n = (int) typed;
        boolean cursorOn = ((int) (time * 2.5f)) % 2 == 0 || n < e.text.length();
        String shown = e.text.substring(0, n) + (cursorOn ? "_" : " ");
        Color old = textFont.getColor().cpy();
        textFont.setColor(Theme.DIM.r, Theme.DIM.g, Theme.DIM.b, alpha);
        textFont.draw(batch, shown, tx, ty, w, Align.left, true);
        textFont.setColor(old);
    }

    private static void brackets(Batch batch, float x, float y, float s, float a) {
        float k = s * 0.16f;
        float t = 4f;
        Color c = Theme.LINE;
        Theme.rect(batch, x, y, k, t, c, a);
        Theme.rect(batch, x, y, t, k, c, a);
        Theme.rect(batch, x + s - k, y, k, t, c, a);
        Theme.rect(batch, x + s - t, y, t, k, c, a);
        Theme.rect(batch, x, y + s - t, k, t, c, a);
        Theme.rect(batch, x, y + s - k, t, k, c, a);
        Theme.rect(batch, x + s - k, y + s - t, k, t, c, a);
        Theme.rect(batch, x + s - t, y + s - k, t, k, c, a);
    }

    private static void draw(Batch batch, BitmapFont font, String text, float x, float y, Color c, float a) {
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * a);
        font.draw(batch, text, x, y);
        font.setColor(old);
    }
}
