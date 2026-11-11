package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
 * The codex. Entries are grouped (threats, crew, cargo, markings, items). The selected entry is
 * shown big in a scanner frame: a horizontal scan line sweeps down and the object materialises
 * from a blueprint. Items instead play a short usage scenario on a radar screen. A short note is
 * typed out under the frame.
 */
public class Codex {
    /** Draws an animated usage scenario inside a radar circle. */
    interface Demo {
        void draw(Batch b, float cx, float cy, float r, float t, float a);
    }

    public static class Entry {
        final String name;
        final String category;
        final TextureRegion[] frames;
        final float fps;
        final String text;
        TextureRegion badge;
        TextureRegion overlay;
        Color[] variants;
        String[] variantNames;
        Demo demo;
        boolean spin;
        /** Painted plate (assets/ui/...), scanned in instead of the sprite when present. */
        String plate;

        Entry(String name, String category, TextureRegion[] frames, float fps, String text) {
            this.name = name;
            this.category = category;
            this.frames = frames;
            this.fps = fps;
            this.text = text;
        }

        public String name() {
            return name;
        }
    }

    public static class Group {
        final String title;
        final List<Entry> entries = new ArrayList<Entry>();

        Group(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }

        public List<Entry> entries() {
            return entries;
        }
    }

    private static final float TYPE_SPEED = 55f;
    private static final float SCAN_TIME = 1.6f;
    private static final float CYCLE = 4.5f;
    private static final float DEMO_PERIOD = 6f;
    private static final Color BLUE = Color.valueOf("5fd7ffff");
    private static final Color[] CORROSIVE = {Color.TAN, Color.GREEN, Color.YELLOW, Color.RED};

    private final List<Group> groups = new ArrayList<Group>();
    private final TextureRegion tmp = new TextureRegion();
    private final StickerResolver icons = new StickerResolver();
    private Entry current;
    private float time;
    private float sel;
    private float typed;

    // Demo assets.
    private final Texture dot;
    private final Texture ring;
    private final TextureRegion ship;
    private final TextureRegion anomaly;
    private final TextureRegion asteroid;
    private final TextureRegion crate;
    private final TextureRegion shieldCrate;
    private final TextureRegion question;
    private final TextureRegion electric;
    private final TextureRegion astro;

    public Codex() {
        TextureRegion[] astronaut = Regions.split(region(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);
        TextureRegion[] corrosive = sheet(PaintConstants.ANOMALY_CORROSIVE_REGION, 3, 4, PaintConstants.ANOMALY_CORROSIVE_FRAMES);
        TextureRegion[] metal = sheet(PaintConstants.ASTEROID_METAL_REGION, 8, 8, PaintConstants.ASTEROID_METAL_FRAMES);

        dot = makeDot();
        ring = makeRing();
        ship = region(PaintConstants.BURAN_REGION);
        anomaly = corrosive[0];
        asteroid = metal[0];
        crate = region(PaintConstants.CRATE_REGION);
        shieldCrate = region(PaintConstants.SHIELD_CRATE_REGION);
        question = icons.getRegion(Sticker.PATTERN);
        electric = sheet(PaintConstants.ANOMALY_ELECTRIC_REGION, 4, 4, PaintConstants.ANOMALY_ELECTRIC_FRAMES)[0];
        astro = astronaut[1];

        // ---------------------------------------------------------------- threats
        Group threats = group("THREATS");
        Entry c = add(threats, new Entry("CORROSIVE ANOMALY", "THREAT", corrosive, 14f,
                "Acid clouds left by the experiment. Four strengths, shown by colour: tan, green, yellow, red. "
                        + "They drift until the anomaly timer wakes them, then they hunt. Ram one with a shield "
                        + "stronger than its colour, burn it, or steer an asteroid into it."));
        c.variants = CORROSIVE;
        c.plate = "ui/codex_corrosive.jpg";
        c.variantNames = new String[]{"LEVEL 1  -  TAN", "LEVEL 2  -  GREEN", "LEVEL 3  -  YELLOW", "LEVEL 4  -  RED"};
        add(threats, new Entry("ELECTRIC ANOMALY", "THREAT",
                sheet(PaintConstants.ANOMALY_ELECTRIC_REGION, 4, 4, PaintConstants.ANOMALY_ELECTRIC_FRAMES), 16f,
                "A fast discharge that never fades on its own. It hunts at high speed and knocks out radar and "
                        + "laser. Let it come close and fire the EMP pulse, or send rockets after it."));
        add(threats, new Entry("RADIATION ANOMALY", "THREAT",
                sheet(PaintConstants.ANOMALY_RADIATION_REGION, 4, 4, PaintConstants.ANOMALY_RADIATION_FRAMES), 14f,
                "Sticks to the hull and feeds on your score while it holds on. Burns out after two minutes. "
                        + "Rockets break it apart."));
        add(threats, new Entry("MAGNETIC ANOMALY", "THREAT",
                sheet(PaintConstants.ANOMALY_MAGNETIC_REGION, 4, 4, PaintConstants.ANOMALY_MAGNETIC_FRAMES), 14f,
                "Clamps onto the hull and siphons fuel. Burns out after two minutes. Mechanics aboard make the "
                        + "leak smaller. Rockets break it apart."));
        add(threats, new Entry("METAL ASTEROID", "THREAT", metal, 20f,
                "Wreckage of the station. A hit scrapes the shield. Burn it with the laser and pull the fragments "
                        + "in with the tractor beam: metal charges the ram shield."));
        add(threats, new Entry("MINERAL ASTEROID", "THREAT",
                sheet(PaintConstants.ASTEROID_MINERAL_REGION, 8, 8, PaintConstants.ASTEROID_METAL_FRAMES), 20f,
                "Rock torn from Object-19. Burn it with the laser; mineral fragments recharge the laser."));
        Entry hole = add(threats, new Entry("THE RIFT", "PHENOMENON  -  BLACK HOLE",
                new TextureRegion[]{region(PaintConstants.BLACKHOLE_REGION)}, 0f,
                "The rift at the heart of Object-19. Anything inside the capture ring spirals in and crumbles. "
                        + "Survive the fall and it throws you out elsewhere in the sector with a damaged hull."));
        hole.spin = true;
        hole.plate = "ui/codex_rift.jpg";
        for (Entry e : threats.entries()) {
            if (e.name.equals("ELECTRIC ANOMALY")) {
                e.plate = "ui/codex_electric.jpg";
            } else if (e.name.equals("RADIATION ANOMALY")) {
                e.plate = "ui/codex_radiation.jpg";
            } else if (e.name.equals("MAGNETIC ANOMALY")) {
                e.plate = "ui/codex_magnetic.jpg";
            }
        }

        // ---------------------------------------------------------------- crew
        Group crew = group("CREW");
        crew(crew, "COMMANDER", astronaut[0], Sticker.COMMANDER,
                "Raises every score you earn, engine thrust and resistance to the black hole's pull.");
        crew(crew, "MEDIC", astronaut[1], Sticker.MEDIC,
                "Raises maximum hull. Repairs get stronger and the black hole hurts less.");
        crew(crew, "SCIENTIST", astronaut[2], Sticker.SCIENTIST,
                "Raises fuel capacity. The laser burns faster and radiation steals less.");
        crew(crew, "MECHANIC", astronaut[3], Sticker.MECHANIC,
                "Raises shield capacity. Magnetic anomalies siphon less fuel.");
        crew(crew, "MILITARY", astronaut[4], Sticker.MILITARY,
                "Raises laser capacity and the ram power you get from metal.");

        // ---------------------------------------------------------------- cargo
        Group cargo = group("CARGO");
        add(cargo, new Entry("CRATE", "CARGO", new TextureRegion[]{crate}, 0f,
                "Station cargo with one item inside. Gold crates hold three. Asteroids smash crates unless they "
                        + "are shielded."));
        add(cargo, new Entry("STORAGE", "CARGO", new TextureRegion[]{region(PaintConstants.STORAGE_REGION)}, 0f,
                "A heavy container with one or two items, including laser charge, radar and rockets."));
        add(cargo, new Entry("FUEL TANK", "CARGO", new TextureRegion[]{region(PaintConstants.FUEL_REGION)}, 0f,
                "Refuels the Buran. A gold tank fills it to the brim. Breaking one hurts the hull."));

        for (Entry e : cargo.entries()) {
            e.plate = e.name.equals("CRATE") ? "ui/codex_crate.jpg"
                    : e.name.equals("STORAGE") ? "ui/codex_storage.jpg" : "ui/codex_fuel.jpg";
        }

        // ---------------------------------------------------------------- markings
        Group marks = group("MARKINGS");
        Entry sealed = add(marks, new Entry("SEALED", "MARKING ON CRATES AND STORAGES", new TextureRegion[]{crate}, 0f,
                "The contents are hidden. Use SCAN to read what is inside before you open it. A gold seal "
                        + "means a richer, stranger load."));
        sealed.overlay = icons.getRegion(Sticker.PATTERN);
        Entry damaged = add(marks, new Entry("DAMAGED", "MARKING ON CRATES AND STORAGES", new TextureRegion[]{crate}, 0f,
                "Burnt by radiation. Opening it is a gamble: a big reward or a nasty surprise. A gold mark "
                        + "raises the stakes both ways."));
        damaged.overlay = icons.getRegion(Sticker.SHABBY);

        // ---------------------------------------------------------------- items
        // ---------------------------------------------------------------- ship systems
        Group systems = group("SHIP SYSTEMS");
        Entry ram = add(systems, new Entry("RAM SHIELD", "SYSTEM  -  {A} ON / OFF", new TextureRegion[]{ship}, 0f,
                "Metal fragments charge it, four levels: tan, green, yellow, red. Switched on it breaks every "
                        + "corrosive anomaly below its level, softens rock hits and slowly drains. With it on, crew "
                        + "come aboard only over the wings: the nose and tail kill. Switched off, everything comes in safely."));
        ram.demo = this::demoRam;
        Entry emp = add(systems, new Entry("EMP PULSE", "SYSTEM  -  {Y}  -  COSTS SHIELD", new TextureRegion[]{ship}, 0f,
                "Burns out every electric discharge nearby and throws all other anomalies back; they lose your "
                        + "trail. Each pulse costs a third of the shield and takes eight seconds to recharge."));
        emp.demo = this::demoEmp;
        Entry shelter = add(systems, new Entry("SHELTER", "STATION REMAINS", new TextureRegion[]{ship}, 0f,
                "A shielded dome left from the station. Inside it nothing hunts you and the shield mends fast. "
                        + "Its field wears thin while you hide and switches off for a while when spent."));
        shelter.demo = this::demoShelter;

        Group items = group("ITEMS");
        item(items, Sticker.FREEZE, "Every anomaly stops hunting for ten seconds.", this::demoFreeze);
        item(items, Sticker.DEGRADE, "All corrosive anomalies drop to the weakest level.", this::demoDegrade);
        item(items, Sticker.BOOST, "Triples engine speed for a moment. At that speed rocks and corrosive anomalies break on the hull. Electric ones do not: they hurt.", this::demoBoost);
        item(items, Sticker.REPAIR, "Restores hull. Stronger with medics aboard.", this::demoRepair);
        item(items, Sticker.SHIELD, "Shields every container from asteroids and fills the ram shield.", this::demoShield);
        item(items, Sticker.TRASH, "Points the moment you take it. Three seconds later the spot where you took it bursts into a volley of rocks: be far away.", this::demoTrash);
        item(items, Sticker.SPLASH, "A shockwave that pops corrosive anomalies across the screen.", this::demoSplash);
        item(items, Sticker.SCAN, "Reveals what sealed boxes hold for thirty seconds.", this::demoScan);
        item(items, Sticker.ROCKETS, "Charges for three seconds, then launches one homing rocket per anomaly in range.", this::demoRockets);
        item(items, Sticker.LASER, "Automatic. Burns asteroids ahead of the ship while the charge lasts.", this::demoLaser);
        item(items, Sticker.RADAR, "Automatic. Marks anomalies around you.", this::demoRadar);
    }

    // ------------------------------------------------------------------ building

    private Group group(String title) {
        Group g = new Group(title);
        groups.add(g);
        return g;
    }

    private Entry add(Group g, Entry e) {
        g.entries.add(e);
        return e;
    }

    private void crew(Group g, String name, TextureRegion frame, Sticker s, String text) {
        Entry e = add(g, new Entry(name, "CREW", new TextureRegion[]{frame}, 0f,
                text + " Fly close and the tractor beam takes them aboard. With the ram shield on, catch them "
                        + "over the wings: the nose and tail kill. Switch it off and they come in safely."));
        e.badge = icons.getRegion(s);
        e.plate = "ui/crew_" + name.toLowerCase() + ".jpg";
    }

    private void item(Group g, Sticker s, String text, Demo demo) {
        boolean system = s == Sticker.LASER || s == Sticker.RADAR;
        boolean onPickup = !Sticker.SELECTABLE.contains(s);
        Entry e = add(g, new Entry(s.name(), system ? "SYSTEM" : onPickup ? "WORKS THE MOMENT YOU PICK IT UP" : "ITEM  -  USE WITH {X}",
                new TextureRegion[]{icons.getRegion(s)}, 0f, text));
        e.demo = demo;
    }

    private static TextureRegion region(String name) {
        return Assets.getInstance().getAtlas().findRegion(name);
    }

    private static TextureRegion[] sheet(String name, int rows, int cols, int frames) {
        return Regions.split(new TextureRegion(region(name)), rows, cols, frames);
    }

    private static Texture makeDot() {
        int s = 32;
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                float a = MathUtils.clamp((1f - d) * 2.5f, 0f, 1f);
                p.setColor(1, 1, 1, a);
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    private static Texture makeRing() {
        int s = 128;
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                float a = MathUtils.clamp(1f - Math.abs(d - 0.94f) * 30f, 0f, 1f);
                p.setColor(1, 1, 1, a);
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    // ------------------------------------------------------------------ state

    public List<Group> groups() {
        return groups;
    }

    public void select(Entry e) {
        if (e != current) {
            current = e;
            sel = 0f;
            typed = 0f;
        }
    }

    public void update(float dt) {
        time += dt;
        sel += dt;
        if (current != null) {
            typed = Math.min(current.text.length(), typed + dt * TYPE_SPEED);
        }
    }

    // ------------------------------------------------------------------ drawing

    /**
     * Draws the selected entry: a scanner frame of {@code box} size centred in {@code w}, then the
     * title and the typed note below it.
     */
    public void draw(Batch batch, float x, float top, float w, float alpha, BitmapFont titleFont,
                     BitmapFont textFont, float box) {
        if (current == null) {
            return;
        }
        Entry e = current;
        // With a painted plate, the plate goes left and the in-game look gets the right side.
        boolean plated = e.demo == null && e.plate != null && Art.get(e.plate) != null && !Theme.portrait();
        float bx = plated ? x : x + (w - box) / 2f;
        float by = top - box;
        paneRight = x + w;
        labelFont = textFont;
        Theme.rect(batch, bx, by, box, box, Theme.PANEL, alpha);
        for (int i = 1; i < 12; i++) {
            Theme.rect(batch, bx, by + box * i / 12f, box, 1.5f, Theme.LINE, 0.05f * alpha);
            Theme.rect(batch, bx + box * i / 12f, by, 1.5f, box, Theme.LINE, 0.05f * alpha);
        }
        brackets(batch, bx, by, box, alpha);

        if (e.demo != null) {
            drawDemo(batch, e, bx, by, box, alpha);
        } else {
            drawScan(batch, e, bx, by, box, alpha);
        }

        // ---- title, category and variant
        float ty = by - 46f;
        text(batch, titleFont, e.name, x, ty, Theme.TEXT, alpha);
        ty -= titleFont.getLineHeight() * 0.95f;
        String cat = e.category;
        if (e.variants != null) {
            cat = e.variantNames[variantIndex(e)];
        }
        Theme.rich(batch, textFont, cat, x, ty - textFont.getCapHeight() / 2f, e.variants != null ? e.variants[variantIndex(e)] : Theme.ACCENT, alpha);
        if (e.variants != null) {
            float sw = 36f;
            float sx = x + w - e.variants.length * (sw + 12f);
            for (int i = 0; i < e.variants.length; i++) {
                Theme.rect(batch, sx + i * (sw + 12f), ty - textFont.getCapHeight(), sw, textFont.getCapHeight(),
                        e.variants[i], alpha * (i == variantIndex(e) ? 1f : 0.3f));
            }
        }
        ty -= textFont.getLineHeight() * 1.5f;

        // ---- typed note with a blinking cursor
        int n = (int) typed;
        boolean cursorOn = ((int) (time * 2.5f)) % 2 == 0 || n < e.text.length();
        String shown = e.text.substring(0, n) + (cursorOn ? "_" : " ");
        Color old = textFont.getColor().cpy();
        textFont.setColor(Theme.DIM.r, Theme.DIM.g, Theme.DIM.b, alpha);
        textFont.draw(batch, shown, x, ty, w, Align.left, true);
        textFont.setColor(old);
    }

    private int variantIndex(Entry e) {
        return ((int) (sel / CYCLE)) % e.variants.length;
    }

    private float paneRight;
    private BitmapFont labelFont;

    /**
     * Beside the painted plate, on the plain panel: the object as it looks in the game (animated,
     * crew as astronauts), in a small bracketed frame labelled IN GAME.
     */
    private void specimen(Batch batch, Entry e, float left, float by, float box, float alpha) {
        TextureRegion icon = e.frames[e.fps > 0 ? ((int) (time * e.fps)) % e.frames.length : 0];
        float space = paneRight - left;
        float s = Math.min(box * 0.46f, space - 80f);
        if (icon == null || s < 80f) {
            return;
        }
        float sx = left + (space - s) / 2f;
        float sy = by;
        brackets(batch, sx, sy, s, alpha * 0.8f);
        if (labelFont != null) {
            String l = "IN GAME";
            text(batch, labelFont, l, sx + s / 2f - Theme.width(labelFont, l) / 2f, sy + s + 44f, Theme.HEAD, alpha);
        }
        float is = s * 0.78f;
        Color tint = e.variants != null ? e.variants[variantIndex(e)] : Color.WHITE;
        float rot = e.spin ? time * 25f : 0f;
        batch.setColor(tint.r, tint.g, tint.b, alpha);
        batch.draw(icon, sx + (s - is) / 2f, sy + (s - is) / 2f, is / 2f, is / 2f, is, is, 1f, 1f, rot);
        batch.setColor(Color.WHITE);
    }

    /** The painted plate: dim blue below the scan line, in full colour above; the sprite as an icon. */
    private void drawPlate(Batch batch, Entry e, Texture t, float bx, float by, float box, float alpha, float f) {
        float in = 8f;
        float x = bx + in;
        float y = by + in;
        float s = box - 2f * in;
        int th = t.getHeight();
        int side = Math.min(t.getWidth(), th);
        int sx = (t.getWidth() - side) / 2;
        int sy = (th - side) / 2;
        batch.setColor(0.25f, 0.45f, 0.7f, 0.35f * alpha);
        batch.draw(t, x, y, s, s, sx, sy, side, side, false, false);
        if (f > 0f) {
            int rh = Math.max(1, (int) (side * f));
            float h = s * rh / side;
            batch.setColor(1f, 1f, 1f, alpha);
            batch.draw(t, x, y + s - h, s, h, sx, sy, side, rh, false, false);
        }
        specimen(batch, e, bx + box, by, box, alpha);
        batch.setColor(Color.WHITE);
        if (f < 1f) {
            float ly = by + box * (1f - f);
            Theme.rect(batch, bx, ly - 2f, box, 4f, BLUE, alpha);
            for (int i = 1; i < 6; i++) {
                Theme.rect(batch, bx, ly + i * 6f, box, 6f, BLUE, alpha * 0.12f * (6 - i) / 6f);
            }
        }
    }

    /** Blueprint below the scan line, the real object above it. */
    private void drawScan(Batch batch, Entry e, float bx, float by, float box, float alpha) {
        float phase = sel % CYCLE;
        float f = MathUtils.clamp(phase / SCAN_TIME, 0f, 1f);
        f = f * f * (3f - 2f * f);
        Texture plateTex = e.plate == null ? null : Art.get(e.plate);
        if (plateTex != null) {
            drawPlate(batch, e, plateTex, bx, by, box, alpha, f);
            return;
        }
        TextureRegion frame = e.frames[e.fps > 0 ? ((int) (time * e.fps)) % e.frames.length : 0];
        if (frame == null) {
            return;
        }
        float size = box * 0.74f;
        float ox = bx + (box - size) / 2f;
        float oy = by + (box - size) / 2f;
        float rot = e.spin ? time * 25f : 0f;
        Color tint = e.variants != null ? e.variants[variantIndex(e)] : Color.WHITE;

        // Blueprint: a cyan ghost with a soft offset outline.
        if (f < 1f) {
            for (int i = 0; i < 4; i++) {
                float dx = (i % 2 == 0 ? 1 : -1) * 3f;
                float dy = (i < 2 ? 1 : -1) * 3f;
                batch.setColor(BLUE.r, BLUE.g, BLUE.b, 0.12f * alpha);
                batch.draw(frame, ox + dx, oy + dy, size / 2f, size / 2f, size, size, 1f, 1f, rot);
            }
            batch.setColor(0.02f, 0.05f, 0.1f, 0.85f * alpha);
            batch.draw(frame, ox, oy, size / 2f, size / 2f, size, size, 1f, 1f, rot);
            batch.setColor(BLUE.r, BLUE.g, BLUE.b, 0.18f * alpha);
            batch.draw(frame, ox, oy, size / 2f, size / 2f, size, size, 1f, 1f, rot);
        }
        // Materialised part: the top f of the sprite.
        if (f > 0f) {
            int rw = frame.getRegionWidth();
            int rh = Math.max(1, (int) (frame.getRegionHeight() * f));
            tmp.setRegion(frame, 0, 0, rw, rh);
            float flicker = 0.92f + 0.08f * MathUtils.sin(time * 23f);
            batch.setColor(tint.r, tint.g, tint.b, alpha * flicker);
            float h = size * rh / frame.getRegionHeight();
            batch.draw(tmp, ox, oy + size - h, size / 2f, h - size / 2f, size, h, 1f, 1f, rot);
            batch.setColor(Color.WHITE);
        }
        if (e.overlay != null && f >= 1f) {
            float s = size * 0.42f;
            batch.setColor(1f, 1f, 1f, alpha);
            batch.draw(e.overlay, bx + (box - s) / 2f, by + (box - s) / 2f, s, s);
        }
        if (e.badge != null && f >= 1f) {
            float s = box * 0.24f;
            batch.setColor(1f, 1f, 1f, alpha);
            batch.draw(e.badge, bx + box - s - 14f, by + 14f, s, s);
        }
        batch.setColor(Color.WHITE);
        // Scan line with a glow trail.
        if (f < 1f) {
            float ly = by + box * (1f - f);
            Theme.rect(batch, bx, ly - 2f, box, 4f, BLUE, alpha);
            for (int i = 1; i < 6; i++) {
                Theme.rect(batch, bx, ly + i * 6f, box, 6f, BLUE, alpha * 0.12f * (6 - i) / 6f);
            }
        }
    }

    /** Items: a radar screen running the usage scenario, item icon in the corner. */
    private void drawDemo(Batch batch, Entry e, float bx, float by, float box, float alpha) {
        float cx = bx + box / 2f;
        float cy = by + box / 2f;
        float r = box * 0.44f;
        // Radar rings and sweep.
        batch.setColor(BLUE.r, BLUE.g, BLUE.b, 0.25f * alpha);
        batch.draw(ring, cx - r, cy - r, r * 2f, r * 2f);
        batch.setColor(BLUE.r, BLUE.g, BLUE.b, 0.12f * alpha);
        batch.draw(ring, cx - r * 0.6f, cy - r * 0.6f, r * 1.2f, r * 1.2f);
        Theme.rect(batch, cx - r, cy - 0.75f, r * 2f, 1.5f, BLUE, 0.1f * alpha);
        Theme.rect(batch, cx - 0.75f, cy - r, 1.5f, r * 2f, BLUE, 0.1f * alpha);
        float t = sel % DEMO_PERIOD;
        e.demo.draw(batch, cx, cy, r, t, alpha);
        batch.setColor(Color.WHITE);
        float s = box * 0.2f;
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(e.frames[0], bx + 14f, by + box - s - 14f, s, s);
        batch.setColor(Color.WHITE);
    }

    // ------------------------------------------------------------------ demo helpers

    private void sprite(Batch b, TextureRegion r, float x, float y, float size, float rot, Color c, float a) {
        b.setColor(c.r, c.g, c.b, a);
        b.draw(r, x - size / 2f, y - size / 2f, size / 2f, size / 2f, size, size, 1f, 1f, rot);
    }

    private void glowDot(Batch b, float x, float y, float size, Color c, float a) {
        b.setColor(c.r, c.g, c.b, a);
        b.draw(dot, x - size / 2f, y - size / 2f, size, size);
    }

    private void ringAt(Batch b, float x, float y, float radius, Color c, float a) {
        b.setColor(c.r, c.g, c.b, a);
        b.draw(ring, x - radius, y - radius, radius * 2f, radius * 2f);
    }

    private static float sm(float t, float a, float b) {
        float x = MathUtils.clamp((t - a) / (b - a), 0f, 1f);
        return x * x * (3f - 2f * x);
    }

    private void shipAt(Batch b, float x, float y, float r, float rot, float a) {
        sprite(b, ship, x, y, r * 0.42f, rot, Color.WHITE, a);
    }

    // ------------------------------------------------------------------ demos

    private void demoFreeze(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy, r, 0f, a);
        float stop = 1.8f;
        float frozen = sm(t, stop, stop + 0.2f);
        for (int i = 0; i < 4; i++) {
            float ang = i * MathUtils.PI / 2f + 0.6f;
            float d = r * MathUtils.lerp(0.95f, 0.45f, sm(Math.min(t, stop), 0f, stop));
            Color c = frozen > 0f ? BLUE : CORROSIVE[i];
            sprite(b, anomaly, cx + MathUtils.cos(ang) * d, cy + MathUtils.sin(ang) * d, r * 0.3f,
                    frozen > 0f ? 0f : t * 90f, c, a);
        }
        if (t > stop) {
            float k = sm(t, stop, stop + 0.8f);
            ringAt(b, cx, cy, r * k, BLUE, a * (1f - k));
        }
    }

    private void demoDegrade(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy, r, 0f, a);
        float hit = 1.6f;
        for (int i = 0; i < 4; i++) {
            float ang = i * MathUtils.PI / 2f + time * 0.3f;
            float d = r * 0.7f;
            Color c = t > hit ? CORROSIVE[0] : CORROSIVE[3 - (i % 3)];
            float s = r * (t > hit ? 0.24f : 0.34f);
            sprite(b, anomaly, cx + MathUtils.cos(ang) * d, cy + MathUtils.sin(ang) * d, s, time * 60f, c, a);
        }
        if (t > hit) {
            float k = sm(t, hit, hit + 0.7f);
            ringAt(b, cx, cy, r * k, Color.TAN, a * (1f - k));
        }
    }

    /**
     * Boost: a dash that breaks what it hits - a rock, a corrosive anomaly - but not an electric
     * one: that one stops the ship cold and hurts it.
     */
    private void demoBoost(Batch b, float cx, float cy, float r, float t, float a) {
        float go = 1.2f;
        float rockX = cx - r * 0.3f;
        float corrX = cx + r * 0.1f;
        float elecX = cx + r * 0.5f;
        float hit = go + 0.75f;
        float x;
        if (t < go) {
            x = cx - r * 0.85f + r * 0.15f * t / go;
        } else if (t < hit) {
            x = MathUtils.lerp(cx - r * 0.7f, elecX - r * 0.2f, (t - go) / (hit - go));
        } else {
            // Bounced back off the discharge, drifting to a stop.
            x = elecX - r * 0.2f - r * 0.25f * sm(t, hit, hit + 0.8f);
        }
        float rockAt = go + 0.75f * (rockX - (cx - r * 0.7f)) / (elecX - r * 0.2f - (cx - r * 0.7f));
        float corrAt = go + 0.75f * (corrX - (cx - r * 0.7f)) / (elecX - r * 0.2f - (cx - r * 0.7f));
        if (t > go && t < hit) {
            for (int i = 1; i < 8; i++) {
                glowDot(b, x - i * r * 0.08f, cy, r * 0.14f, Color.ORANGE, a * 0.5f * (8 - i) / 8f);
            }
        }
        if (t < rockAt) {
            sprite(b, asteroid, rockX, cy, r * 0.26f, time * 20f, Color.WHITE, a);
        } else {
            burst(b, rockX, cy, r, sm(t, rockAt, rockAt + 0.7f), Color.LIGHT_GRAY, a);
        }
        if (t < corrAt) {
            sprite(b, anomaly, corrX, cy, r * 0.26f, time * 90f, Color.GREEN, a);
        } else {
            burst(b, corrX, cy, r, sm(t, corrAt, corrAt + 0.7f), Color.GREEN, a);
        }
        float flick = 0.8f + 0.2f * MathUtils.sin(time * 40f);
        sprite(b, electric, elecX, cy, r * 0.3f, time * 60f, Color.WHITE, a * flick);
        if (t > hit && t < hit + 0.6f) {
            float k = sm(t, hit, hit + 0.6f);
            ringAt(b, x, cy, r * (0.15f + 0.25f * k), Color.RED, a * (1f - k));
            glowDot(b, x, cy, r * 0.5f, Color.RED, a * 0.5f * (1f - k));
        }
        shipAt(b, x, cy, r, 0f, a);
    }

    private static final Color RAM_GREEN = Color.valueOf("6cc24aff");
    private static final Color AMBER = Color.valueOf("ffb547ff");

    /**
     * Ram shield: on, a corrosive anomaly breaks against it; a crew member comes in over the
     * wing; then the shield goes off and the next one comes in over the nose, safely.
     */
    private void demoRam(Batch b, float cx, float cy, float r, float t, float a) {
        boolean on = t < 4f;
        float off = sm(t, 4f, 4.4f);
        if (t < 4.4f) {
            float pulse = 0.75f + 0.2f * MathUtils.sin(time * 5f);
            glowDot(b, cx, cy, r * 0.75f * (1f + 0.3f * off), RAM_GREEN, a * 0.35f * (1f - off));
            ringAt(b, cx, cy, r * 0.28f * (1f + 0.3f * off), RAM_GREEN, a * pulse * (1f - off));
        }
        // An anomaly from the right breaks on the shield.
        float hit = 1.3f;
        if (t < hit) {
            sprite(b, anomaly, MathUtils.lerp(cx + r * 0.95f, cx + r * 0.3f, sm(t, 0f, hit)), cy, r * 0.26f, time * 90f, Color.TAN, a);
        } else {
            burst(b, cx + r * 0.3f, cy, r, sm(t, hit, hit + 0.7f), Color.TAN, a);
        }
        // Crew from above, over the wing (the side).
        float in1 = 3.2f;
        if (t > 1.6f && t < in1) {
            sprite(b, astro, cx, MathUtils.lerp(cy + r * 0.95f, cy + r * 0.2f, sm(t, 1.6f, in1)), r * 0.24f, 0f, Color.WHITE, a);
        } else if (t >= in1 && t < in1 + 0.4f) {
            glowDot(b, cx, cy + r * 0.2f, r * 0.4f, Color.WHITE, a * (1f - sm(t, in1, in1 + 0.4f)));
        }
        // Shield off: the next one comes straight at the nose, safely.
        float in2 = 5.6f;
        if (!on && t < in2) {
            sprite(b, astro, MathUtils.lerp(cx + r * 0.95f, cx + r * 0.25f, sm(t, 4.4f, in2)), cy, r * 0.24f, 0f, Color.WHITE, a);
        } else if (t >= in2 && t < in2 + 0.4f) {
            glowDot(b, cx + r * 0.25f, cy, r * 0.4f, Color.WHITE, a * (1f - sm(t, in2, in2 + 0.4f)));
        }
        shipAt(b, cx, cy, r, 0f, a);
    }

    /** EMP: the ring goes out, the discharge burns, the rest are thrown back; the shield pays. */
    private void demoEmp(Batch b, float cx, float cy, float r, float t, float a) {
        float go = 1.5f;
        float k = sm(t, go, go + 0.5f);
        float push = sm(t, go + 0.1f, go + 1.4f);
        float[] ang = {0.4f, 2.3f, 4.1f};
        for (int i = 0; i < 3; i++) {
            float d = r * (MathUtils.lerp(0.75f, 0.5f, sm(Math.min(t, go), 0f, go)) + 0.42f * push);
            sprite(b, anomaly, cx + MathUtils.cos(ang[i]) * d, cy + MathUtils.sin(ang[i]) * d, r * 0.24f,
                    time * 90f, CORROSIVE[i], a * (1f - sm(t, go + 1.6f, go + 2.2f)));
        }
        float ex = cx - r * 0.4f;
        float ey = cy - r * 0.25f;
        if (t < go + 0.2f) {
            sprite(b, electric, ex, ey, r * 0.28f, time * 60f, Color.WHITE, a);
        } else {
            burst(b, ex, ey, r, sm(t, go + 0.2f, go + 0.9f), BLUE, a);
        }
        if (t > go && t < go + 0.6f) {
            ringAt(b, cx, cy, r * 0.95f * k, BLUE, a * (1f - k));
        }
        // The shield bar under the ship: a third gone at the pulse, slowly back.
        float level = t < go ? 1f : MathUtils.lerp(0.66f, 0.8f, sm(t, go + 0.5f, 5.5f));
        float bw = r * 0.6f;
        Theme.rect(b, cx - bw / 2f, cy - r * 0.3f, bw, 8f, Theme.TRACK, a);
        Theme.rect(b, cx - bw / 2f, cy - r * 0.3f, bw * level, 8f, BLUE, a);
        shipAt(b, cx, cy, r, 0f, a);
    }

    /** Shelter: inside the dome nothing hunts you, the shield fills; hunters circle outside. */
    private void demoShelter(Batch b, float cx, float cy, float r, float t, float a) {
        float dome = r * 0.5f;
        ringAt(b, cx, cy, dome, AMBER, a * (0.35f + 0.1f * MathUtils.sin(time * 2f)));
        glowDot(b, cx, cy, dome * 2f, AMBER, a * 0.08f);
        for (int i = 0; i < 3; i++) {
            float ang = time * 0.6f + i * MathUtils.PI2 / 3f;
            float d = r * 0.78f;
            sprite(b, anomaly, cx + MathUtils.cos(ang) * d, cy + MathUtils.sin(ang) * d, r * 0.22f, time * 90f,
                    CORROSIVE[i], a);
        }
        float level = MathUtils.lerp(0.2f, 1f, sm(t, 0.3f, 4.5f));
        float bw = r * 0.6f;
        Theme.rect(b, cx - bw / 2f, cy - r * 0.3f, bw, 8f, Theme.TRACK, a);
        Theme.rect(b, cx - bw / 2f, cy - r * 0.3f, bw * level, 8f, BLUE, a);
        shipAt(b, cx, cy, r, 0f, a);
    }

    /** Shards flying apart from a broken object (k: 0..1). */
    private void burst(Batch b, float x, float y, float r, float k, Color c, float a) {
        if (k >= 1f) {
            return;
        }
        for (int i = 0; i < 6; i++) {
            float ang = i * MathUtils.PI2 / 6f + 0.3f;
            glowDot(b, x + MathUtils.cos(ang) * r * 0.32f * k, y + MathUtils.sin(ang) * r * 0.32f * k,
                    r * 0.08f, c, a * (1f - k));
        }
    }

    private void demoRepair(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy + r * 0.12f, r, 0f, a);
        float p = MathUtils.lerp(0.25f, 1f, sm(t, 1.2f, 2.6f));
        float w = r * 1.2f;
        float y = cy - r * 0.45f;
        Theme.rect(b, cx - w / 2f, y, w, r * 0.07f, Theme.FAINT, a);
        Theme.rect(b, cx - w / 2f, y, w * p, r * 0.07f, p > 0.3f ? Theme.OK : Color.valueOf("ff8a6aff"), a);
        if (t > 1.2f && t < 2.8f) {
            for (int i = 0; i < 5; i++) {
                float k = ((t - 1.2f) * 0.8f + i * 0.2f) % 1f;
                glowDot(b, cx + (i - 2) * r * 0.15f, cy + r * (0.1f + 0.4f * k), r * 0.09f, Theme.OK, a * (1f - k));
            }
        }
    }

    private void demoShield(Batch b, float cx, float cy, float r, float t, float a) {
        float on = 1.4f;
        float[][] pos = {{-0.5f, 0.3f}, {0.45f, 0.4f}, {0f, -0.5f}};
        for (float[] p : pos) {
            sprite(b, t > on ? shieldCrate : crate, cx + p[0] * r, cy + p[1] * r, r * 0.34f, 0f, Color.WHITE, a);
            if (t > on) {
                float k = sm(t, on, on + 0.5f);
                ringAt(b, cx + p[0] * r, cy + p[1] * r, r * 0.24f * k, BLUE, a * 0.8f);
            }
        }
        // An asteroid hits the bottom crate and bounces off.
        float hitT = 3f;
        float ax;
        float ay;
        if (t < hitT) {
            ax = cx + r * 0.9f - r * 0.9f * sm(t, 2f, hitT);
            ay = cy - r * 0.5f;
        } else {
            ax = cx + r * 0.6f * sm(t, hitT, hitT + 1.5f);
            ay = cy - r * 0.5f - r * 0.4f * sm(t, hitT, hitT + 1.5f);
        }
        if (t > 2f) {
            sprite(b, asteroid, ax, ay, r * 0.22f, time * 50f, Color.WHITE, a);
        }
    }

    /**
     * Trash: the ship picks up the container and flies on; where it was taken a countdown ticks,
     * then a volley of rocks bursts out - and one of them catches the ship.
     */
    private void demoTrash(Batch b, float cx, float cy, float r, float t, float a) {
        float pick = 1.1f;
        float boom = 3.4f;
        float px = cx - r * 0.15f;
        float py = cy - r * 0.15f;
        // The ship: in from the left to the container, then away up and to the right.
        float sx;
        float sy;
        float rot;
        if (t < pick) {
            float k = sm(t, 0f, pick);
            sx = MathUtils.lerp(cx - r * 0.85f, px, k);
            sy = py;
            rot = 0f;
        } else {
            float k = sm(t, pick, boom + 0.4f);
            sx = MathUtils.lerp(px, cx + r * 0.5f, k);
            sy = MathUtils.lerp(py, cy + r * 0.45f, k);
            rot = 40f * sm(t, pick, pick + 0.5f);
        }
        if (t < pick) {
            sprite(b, crate, px + r * 0.12f, py, r * 0.22f, 0f, Color.WHITE, a);
        } else if (t < pick + 0.3f) {
            glowDot(b, px, py, r * 0.4f, Color.WHITE, a * (1f - sm(t, pick, pick + 0.3f)));
        }
        // Countdown: three beats at the pickup point, white, yellow, red.
        if (t > pick + 0.3f && t < boom) {
            float beat = (t - pick - 0.3f) / ((boom - pick - 0.3f) / 3f);
            int n = Math.min(2, (int) beat);
            float f = beat - (int) beat;
            Color c = n == 0 ? Color.WHITE : n == 1 ? Color.YELLOW : Color.RED;
            ringAt(b, px, py, r * (0.18f - 0.1f * f), c, a * (1f - f));
            glowDot(b, px, py, r * 0.12f, c, a * 0.8f);
        }
        // The volley: seven rocks outward; the eighth goes for the ship.
        float hitAt = boom + 0.55f;
        if (t > boom) {
            float k = sm(t, boom, boom + 1.6f);
            float fade = 1f - sm(t, boom + 1.6f, boom + 2.2f);
            for (int i = 0; i < 7; i++) {
                float ang = i * MathUtils.PI2 / 7f + 2.1f;
                sprite(b, asteroid, px + MathUtils.cos(ang) * r * 0.85f * k, py + MathUtils.sin(ang) * r * 0.85f * k,
                        r * 0.16f, time * 40f, Color.WHITE, a * fade);
            }
            if (t < hitAt) {
                float q = (t - boom) / (hitAt - boom);
                sprite(b, asteroid, MathUtils.lerp(px, sx, q), MathUtils.lerp(py, sy, q), r * 0.18f, time * 60f,
                        Color.WHITE, a);
            }
            if (t < boom + 0.3f) {
                glowDot(b, px, py, r * 0.6f, Color.ORANGE, a * (1f - sm(t, boom, boom + 0.3f)));
            }
        }
        if (t > hitAt && t < hitAt + 0.6f) {
            float k = sm(t, hitAt, hitAt + 0.6f);
            ringAt(b, sx, sy, r * (0.15f + 0.25f * k), Color.RED, a * (1f - k));
            glowDot(b, sx, sy, r * 0.5f, Color.RED, a * 0.5f * (1f - k));
            sx += MathUtils.sin(time * 70f) * r * 0.02f * (1f - k);
        }
        shipAt(b, sx, sy, r, rot, a);
    }

    private void demoSplash(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy, r, 0f, a);
        float go = 1.5f;
        float wave = r * sm(t, go, go + 1.2f);
        for (int i = 0; i < 6; i++) {
            float ang = i * MathUtils.PI2 / 6f + 0.4f;
            float d = r * (0.45f + 0.08f * (i % 3));
            float px = cx + MathUtils.cos(ang) * d;
            float py = cy + MathUtils.sin(ang) * d;
            if (t < go || wave < d) {
                sprite(b, anomaly, px, py, r * 0.26f, time * 60f, CORROSIVE[i % 4], a);
            } else {
                float k = MathUtils.clamp((wave - d) / (r * 0.4f), 0f, 1f);
                for (int j = 0; j < 5; j++) {
                    float aj = j * MathUtils.PI2 / 5f;
                    glowDot(b, px + MathUtils.cos(aj) * r * 0.15f * k, py + MathUtils.sin(aj) * r * 0.15f * k,
                            r * 0.08f, CORROSIVE[i % 4], a * (1f - k));
                }
            }
        }
        if (t > go && t < go + 1.2f) {
            ringAt(b, cx, cy, Math.max(1f, wave), Color.valueOf("9fd0ffff"), a * (1f - sm(t, go, go + 1.2f)));
        }
    }

    private void demoScan(Batch b, float cx, float cy, float r, float t, float a) {
        Sticker[] reveal = {Sticker.ROCKETS, Sticker.REPAIR, Sticker.FREEZE};
        float[][] pos = {{-0.5f, 0.25f}, {0.5f, 0.25f}, {0f, -0.45f}};
        float go = 1.2f;
        float wave = r * 1.1f * sm(t, go, go + 1.4f);
        for (int i = 0; i < 3; i++) {
            float px = cx + pos[i][0] * r;
            float py = cy + pos[i][1] * r;
            sprite(b, crate, px, py, r * 0.36f, 0f, Color.WHITE, a);
            float d = (float) Math.sqrt(pos[i][0] * pos[i][0] + pos[i][1] * pos[i][1]) * r;
            TextureRegion icon = t > go && wave > d ? icons.getRegion(reveal[i]) : question;
            sprite(b, icon, px, py, r * 0.2f, 0f, Color.WHITE, a);
        }
        if (t > go && t < go + 1.4f) {
            ringAt(b, cx, cy, Math.max(1f, wave), Theme.OK, a * 0.8f);
        }
    }

    private void demoRockets(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy, r, 0f, a);
        float charge = 1.5f;
        float[][] targets = {{-0.6f, 0.5f}, {0.65f, 0.35f}, {0.1f, -0.7f}};
        if (t < charge) {
            ringAt(b, cx, cy, r * 0.35f * (t / charge), Theme.ACCENT, a * 0.8f);
        }
        for (int i = 0; i < targets.length; i++) {
            float tx = cx + targets[i][0] * r;
            float ty = cy + targets[i][1] * r;
            float k = sm(t, charge + i * 0.15f, charge + 1.2f + i * 0.15f);
            if (k < 1f) {
                sprite(b, anomaly, tx, ty, r * 0.26f, time * 60f, CORROSIVE[(i + 1) % 4], a);
            }
            if (t > charge && k < 1f) {
                // Curved homing path.
                float mx = cx + (targets[i][0] * 0.3f - targets[i][1] * 0.4f) * r;
                float my = cy + (targets[i][1] * 0.3f + targets[i][0] * 0.4f) * r;
                float u = k;
                float x = (1 - u) * (1 - u) * cx + 2 * (1 - u) * u * mx + u * u * tx;
                float y = (1 - u) * (1 - u) * cy + 2 * (1 - u) * u * my + u * u * ty;
                glowDot(b, x, y, r * 0.1f, Theme.ACCENT, a);
            }
            if (k >= 1f) {
                float e = sm(t, charge + 1.2f + i * 0.15f, charge + 2f + i * 0.15f);
                ringAt(b, tx, ty, r * 0.2f * e, Theme.ACCENT, a * (1f - e));
            }
        }
    }

    private void demoLaser(Batch b, float cx, float cy, float r, float t, float a) {
        float sx = cx - r * 0.6f;
        shipAt(b, sx, cy, r, -90f, a);
        float on = 0.8f;
        float burn = 2.2f;
        float ax = cx + r * 0.45f;
        if (t > on && t < burn) {
            float flick = 0.7f + 0.3f * MathUtils.sin(time * 40f);
            Theme.rect(b, sx + r * 0.15f, cy - 3f, ax - sx - r * 0.15f, 6f, Color.valueOf("c58bffff"), a * flick);
            glowDot(b, ax, cy, r * 0.25f, Color.valueOf("c58bffff"), a * 0.6f * flick);
        }
        if (t < burn) {
            float heat = sm(t, on, burn);
            Color c = new Color(1f, 1f - heat * 0.5f, 1f - heat * 0.8f, 1f);
            sprite(b, asteroid, ax, cy, r * 0.4f, time * 20f, c, a);
        } else {
            float k = sm(t, burn, burn + 2.2f);
            for (int i = 0; i < 6; i++) {
                float ang = i * MathUtils.PI2 / 6f + 0.3f;
                float spread = r * 0.25f * sm(t, burn, burn + 0.4f);
                float fx = ax + MathUtils.cos(ang) * spread;
                float fy = cy + MathUtils.sin(ang) * spread;
                // Tractor beam pulls fragments to the ship.
                float px = MathUtils.lerp(fx, sx, sm(t, burn + 0.6f, burn + 2.2f));
                float py = MathUtils.lerp(fy, cy, sm(t, burn + 0.6f, burn + 2.2f));
                glowDot(b, px, py, r * 0.1f, Color.LIGHT_GRAY, a * (1f - k * 0.8f));
            }
        }
    }

    private void demoRadar(Batch b, float cx, float cy, float r, float t, float a) {
        shipAt(b, cx, cy, r, 0f, a);
        float sweep = time * 2.2f;
        for (int i = 0; i < 24; i++) {
            float ang = sweep - i * 0.04f;
            float k = (24 - i) / 24f;
            Theme.rect(b, cx, cy, 0f, 0f, Theme.OK, 0f);
            for (int s = 1; s < 10; s++) {
                glowDot(b, cx + MathUtils.cos(ang) * r * s / 10f, cy + MathUtils.sin(ang) * r * s / 10f, r * 0.05f,
                        Theme.OK, a * 0.25f * k);
            }
        }
        float[][] blips = {{0.6f, 0.4f}, {-0.5f, 0.55f}, {-0.3f, -0.6f}, {0.7f, -0.3f}};
        for (float[] p : blips) {
            float ang = MathUtils.atan2(p[1], p[0]);
            float since = ((sweep - ang) % MathUtils.PI2 + MathUtils.PI2) % MathUtils.PI2;
            float glow = MathUtils.clamp(1f - since / MathUtils.PI2 * 1.3f, 0f, 1f);
            glowDot(b, cx + p[0] * r, cy + p[1] * r, r * 0.12f, Color.RED, a * glow);
        }
    }

    // ------------------------------------------------------------------ misc

    private static void brackets(Batch batch, float x, float y, float s, float a) {
        float k = s * 0.12f;
        float t = 5f;
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

    private static void text(Batch batch, BitmapFont font, String s, float x, float y, Color c, float a) {
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * a);
        font.draw(batch, s, x, y);
        font.setColor(old);
    }
}
