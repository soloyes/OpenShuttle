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
        Sector s1 = new Sector(1, "SILENCE", Sector.Goal.RESCUE, 2,
                "Buran-19, this is Baikonur... no. Nobody answers. Station debris all around. "
                        + "Two transponders still beep nearby: Sasha, Lena. Fly close, the beam will take them.",
                "LEFT STICK - FLY      THE BEAM TAKES CREW ABOARD");
        s1.corrosiveMax = 8;
        s1.corrosiveRespawn = 6f;
        s1.corrosiveHuntTime = 150f;
        s1.electricMax = 0;
        s1.radiationMax = 0;
        s1.magneticMax = 0;
        s1.asteroidMax = 12;
        s1.asteroidRespawn = 8f;
        s1.astronautMax = 2;
        s1.shelters = 1;
        s1.asteroidMax = 8;
        s1.crateMax = 0;
        s1.storageMax = 0;
        s1.fuelMax = 1;
        SECTORS.add(s1);

        Sector s2 = new Sector(2, "DEBRIS FIELD", Sector.Goal.SALVAGE, 12,
                "The hull is holding. To get home we need metal and rock. The laser is charged: "
                        + "point the nose at an asteroid, it burns, then pull the fragments in.",
                "THE LASER FIRES AHEAD AUTOMATICALLY      MINERAL RECHARGES IT");
        s2.corrosiveMax = 16;
        s2.corrosiveRespawn = 4f;
        s2.corrosiveHuntTime = 90f;
        s2.electricMax = 0;
        s2.radiationMax = 0;
        s2.magneticMax = 1;
        s2.asteroidMax = 22;
        s2.asteroidRespawn = 2.5f;
        s2.astronautMax = 1;
        s2.shelters = 2;
        s2.crateMax = 1;
        s2.storageMax = 0;
        s2.fuelMax = 2;
        s2.startWithLaser = true;
        SECTORS.add(s2);

        Sector s3 = new Sector(3, "STATIC", Sector.Goal.RESCUE, 3,
                "Something hums out there and the radar flickers. Three more of ours are drifting "
                        + "in this sector. If the discharge comes for you, let it close and hit the EMP.",
                "Y - EMP PULSE STOPS ELECTRIC ANOMALIES NEARBY");
        s3.corrosiveMax = 20;
        s3.corrosiveRespawn = 3f;
        s3.corrosiveHuntTime = 75f;
        s3.electricMax = 2;
        s3.radiationMax = 0;
        s3.magneticMax = 1;
        s3.asteroidMax = 14;
        s3.asteroidRespawn = 3.5f;
        s3.astronautMax = 3;
        s3.shelters = 2;
        s3.crateMax = 2;
        s3.storageMax = 1;
        s3.fuelMax = 2;
        SECTORS.add(s3);
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
