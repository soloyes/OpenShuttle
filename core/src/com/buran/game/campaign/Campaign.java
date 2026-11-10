package com.buran.game.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * The campaign: 19 sectors around Object-19. Holds the sector table, the run that is being
 * played (null = arcade) and the saved progress.
 */
public final class Campaign {
    public static final int TOTAL = 19;
    private static final List<Sector> SECTORS = new ArrayList<Sector>();
    private static Sector current;
    private static SectorRun run;

    /** The live run of the current sector (set by the game screen). */
    public static SectorRun run() {
        return run;
    }

    public static void setRun(SectorRun r) {
        run = r;
    }

    static {
        // Each sector holds only what it teaches (tip 4): no extra threats, no loose cargo,
        // and a small room where the whole task fits in a glance or two (tip 85).
        Sector s1 = new Sector(1, "SILENCE", Sector.Goal.RESCUE, 2,
                "Buran-19, this is... no one. Baikonur is silent. Two beacons are still calling, close by. Sasha. Lena. Bring them home.",
                "LEFT STICK - FLY      THE BEAM TAKES CREW ABOARD");
        empty(s1);
        s1.astronautMax = 2;
        s1.space = 1.4f;
        SECTORS.add(s1);

        Sector s2 = new Sector(2, "DEBRIS FIELD", Sector.Goal.SALVAGE, 50,
                "The hull is holding. But we need metal to get home. The laser is charged. Burn the rocks, then pull the pieces in.",
                "THE LASER FIRES AHEAD AUTOMATICALLY      MINERAL RECHARGES IT");
        empty(s2);
        s2.asteroidMax = 10;
        s2.asteroidRespawn = 2.5f;
        s2.space = 1.6f;
        s2.startWithLaser = true;
        SECTORS.add(s2);

        Sector s3 = new Sector(3, "STATIC", Sector.Goal.RESCUE, 3,
                "Static on every channel. The radar is going blind. Three more of ours are out there. If the discharge comes for you, let it close. Then hit the pulse.",
                "Y - EMP PULSE STOPS ELECTRIC ANOMALIES NEARBY");
        empty(s3);
        s3.electricMax = 2;
        s3.astronautMax = 3;
        s3.space = 1.6f;
        SECTORS.add(s3);
    }

    /** Nothing in the sector until the sector table adds it. */
    private static void empty(Sector s) {
        s.corrosiveMax = 0;
        s.electricMax = 0;
        s.radiationMax = 0;
        s.magneticMax = 0;
        s.asteroidMax = 0;
        s.astronautMax = 0;
        s.shelters = 0;
        s.blackhole = false;
        s.crateMax = 0;
        s.storageMax = 0;
        s.fuelMax = 0;
        s.borderDamage = false;
    }

    private Campaign() {
    }

    public static List<Sector> sectors() {
        return SECTORS;
    }

    /** The sector being played, or null in arcade mode. */
    public static Sector current() {
        return current;
    }

    public static void start(Sector s) {
        current = s;
    }

    public static void arcade() {
        current = null;
    }

    public static Sector next(Sector s) {
        int i = SECTORS.indexOf(s);
        return i >= 0 && i + 1 < SECTORS.size() ? SECTORS.get(i + 1) : null;
    }

    // ------------------------------------------------------------------ progress

    private static Preferences prefs() {
        return Gdx.app.getPreferences("buran19");
    }

    /** Highest sector number the player may start (1 = only the first). */
    public static int unlocked() {
        if (com.buran.game.conf.Rules.DEBUG_UNLOCK) {
            return TOTAL;
        }
        return Math.max(1, prefs().getInteger("campaign.unlocked", 1));
    }

    public static void complete(Sector s) {
        Preferences p = prefs();
        if (s.number + 1 > unlocked()) {
            p.putInteger("campaign.unlocked", Math.min(TOTAL, s.number + 1));
        }
        p.putBoolean("campaign.done." + s.number, true);
        p.flush();
    }

    public static boolean isDone(int number) {
        return prefs().getBoolean("campaign.done." + number, false);
    }
}
