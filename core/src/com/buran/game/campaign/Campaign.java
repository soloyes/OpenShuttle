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
                "{LS} FLY      THE BEAM TAKES CREW ABOARD");
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
                "{Y} EMP: BURNS DISCHARGES, THROWS THE REST BACK. COSTS SHIELD");
        empty(s3);
        s3.electricMax = 2;
        s3.astronautMax = 3;
        s3.space = 1.6f;
        SECTORS.add(s3);

        // The rift: the crew drifts at its edge. Stay outside the ring; when something hunts
        // you, a shelter hides you and mends the shield (tips 4, 15, 39).
        Sector s4 = new Sector(4, "THE RIFT", Sector.Goal.RESCUE, 3,
                "There it is. The rift. Three of ours are circling its edge, too close. Stay outside the ring: what goes in comes out broken. If something hunts you, the shelters will hide you.",
                "STAY OUT OF THE RING      SHELTERS HIDE YOU AND MEND THE SHIELD");
        empty(s4);
        s4.blackhole = true;
        s4.shelters = 2;
        s4.astronautMax = 3;
        s4.corrosiveMax = 3;
        s4.corrosiveRespawn = 12f;
        s4.corrosiveHuntTime = 25f;
        s4.space = 2.2f;
        SECTORS.add(s4);

        // Sealed cargo: scan before you open; a burnt crate is a gamble (tips 57, 68).
        Sector s5 = new Sector(5, "SEALED CARGO", Sector.Goal.CARGO, 6,
                "The station's cargo is still drifting out here. Some crates are sealed, some are burnt. Scan before you open them. Six crates, and we can keep going.",
                "{X} SCAN READS SEALED CRATES      BURNT ONES ARE A GAMBLE");
        empty(s5);
        s5.crateMax = 5;
        s5.storageMax = 2;
        s5.dangerOdds = 1;
        s5.startItems.put(com.buran.game.labelsticker.Sticker.SCAN, 2);
        s5.corrosiveMax = 2;
        s5.corrosiveRespawn = 15f;
        s5.space = 2.0f;
        SECTORS.add(s5);

        // Echo: everything learned, at once, with nothing new (tip 28: apply it creatively).
        Sector s6 = new Sector(6, "ECHO", Sector.Goal.RESCUE, 4,
                "Four voices on the beacon now. And the whole field is awake. You know everything you need. Use it. Bring them home.",
                "EVERYTHING AT ONCE      THE EDGE OF THE SECTOR HURTS AGAIN");
        empty(s6);
        s6.blackhole = true;
        s6.shelters = 2;
        s6.astronautMax = 4;
        s6.corrosiveMax = 6;
        s6.corrosiveRespawn = 6f;
        s6.electricMax = 1;
        s6.asteroidMax = 12;
        s6.asteroidRespawn = 4f;
        s6.crateMax = 2;
        s6.fuelMax = 2;
        s6.borderDamage = true;
        s6.space = 2.6f;
        s6.boss = 1;
        SECTORS.add(s6);

        chapterTwo();
        chapterThree();
    }

    /**
     * Chapter three: home. The commander's lost crew, the graveyard, the guarded cargo, the
     * tide of the rift, a blind sector, everything at once, and the way home (tips 6, 7, 37).
     */
    private static void chapterThree() {
        com.buran.game.labelsticker.Sticker ROCKETS = com.buran.game.labelsticker.Sticker.ROCKETS;
        com.buran.game.labelsticker.Sticker FREEZE = com.buran.game.labelsticker.Sticker.FREEZE;
        Sector s13 = new Sector(13, "ECHOES", Sector.Goal.RESCUE, 4,
                "His crew is still out here. Their beacons are old, the signals weak. Radiation and magnets everywhere. Four to bring home.",
                null);
        empty(s13);
        s13.chapter = 3;
        s13.radiationMax = 2;
        s13.magneticMax = 2;
        s13.fuelMax = 2;
        s13.astronautMax = 4;
        s13.startItems.put(ROCKETS, 3);
        s13.borderDamage = true;
        s13.space = 2.2f;
        SECTORS.add(s13);

        Sector s14 = new Sector(14, "GRAVEYARD", Sector.Goal.SALVAGE, 50,
                "The station's graveyard. Everything that broke is drifting toward the rift. We need metal for the hull before we go in. Fifty pieces.",
                null);
        empty(s14);
        s14.chapter = 3;
        s14.blackhole = true;
        s14.shelters = 1;
        s14.asteroidMax = 16;
        s14.asteroidRespawn = 2f;
        s14.corrosiveMax = 4;
        s14.corrosiveRespawn = 8f;
        s14.startWithLaser = true;
        s14.borderDamage = true;
        s14.space = 2.4f;
        SECTORS.add(s14);

        Sector s15 = new Sector(15, "WARDENS", Sector.Goal.CARGO, 6,
                "The field is guarding the way. Discharges and packs, all around the cargo we need. Six crates. Take them and run.",
                null);
        empty(s15);
        s15.chapter = 3;
        s15.crateMax = 4;
        s15.storageMax = 2;
        s15.electricMax = 2;
        s15.corrosiveMax = 8;
        s15.corrosiveRespawn = 4f;
        s15.corrosiveHuntTime = 20f;
        s15.shelters = 2;
        s15.startItems.put(FREEZE, 2);
        s15.borderDamage = true;
        s15.space = 2.4f;
        SECTORS.add(s15);

        Sector s16 = new Sector(16, "TIDE", Sector.Goal.RESCUE, 5,
                "The tide is turning. The rift pulls harder every minute. Five more of ours on its edge. Be quick.",
                null);
        empty(s16);
        s16.chapter = 3;
        s16.blackhole = true;
        s16.shelters = 2;
        s16.astronautMax = 5;
        s16.corrosiveMax = 4;
        s16.corrosiveRespawn = 6f;
        s16.asteroidMax = 8;
        s16.escalate = true;
        s16.borderDamage = true;
        s16.space = 2.4f;
        SECTORS.add(s16);

        Sector s17 = new Sector(17, "BLIND", Sector.Goal.RESCUE, 4,
                "The radar is dead. Nothing on the screen now. Only your eyes, and the beacons. Find four of them.",
                "NO RADAR      FOLLOW THE BEACONS");
        empty(s17);
        s17.chapter = 3;
        s17.noRadar = true;
        s17.astronautMax = 4;
        s17.corrosiveMax = 6;
        s17.corrosiveRespawn = 5f;
        s17.electricMax = 1;
        s17.asteroidMax = 10;
        s17.borderDamage = true;
        s17.space = 2.4f;
        SECTORS.add(s17);

        Sector s18 = new Sector(18, "THRESHOLD", Sector.Goal.RESCUE, 6,
                "This is the threshold. Everything the field has, it throws at us now. Six voices. The last ones.",
                null);
        empty(s18);
        s18.chapter = 3;
        s18.blackhole = true;
        s18.shelters = 2;
        s18.astronautMax = 6;
        s18.corrosiveMax = 8;
        s18.corrosiveRespawn = 4f;
        s18.electricMax = 2;
        s18.radiationMax = 1;
        s18.magneticMax = 1;
        s18.asteroidMax = 12;
        s18.crateMax = 2;
        s18.fuelMax = 2;
        s18.escalate = true;
        s18.borderDamage = true;
        s18.startItems.put(ROCKETS, 2);
        s18.startItems.put(FREEZE, 2);
        s18.space = 2.8f;
        SECTORS.add(s18);

        Sector s19 = new Sector(19, "HOME", Sector.Goal.RESCUE, 1,
                "He is waiting at the edge of the rift. The last one. Bring him aboard, then follow the light home.",
                "THE LAST ONE      THEN INTO THE LIGHT");
        empty(s19);
        s19.chapter = 3;
        s19.blackhole = true;
        s19.shelters = 2;
        s19.astronautMax = 1;
        s19.corrosiveMax = 10;
        s19.corrosiveRespawn = 3f;
        s19.electricMax = 2;
        s19.asteroidMax = 10;
        s19.escalate = true;
        s19.beaconAtRift = true;
        s19.borderDamage = true;
        s19.startItems.put(FREEZE, 2);
        s19.space = 2.6f;
        SECTORS.add(s19);
    }

    /**
     * Chapter two: the voice from the rift. One new threat or tool per sector, then everything
     * again under a collapsing field (tips 4, 28, 37).
     */
    private static void chapterTwo() {
        com.buran.game.labelsticker.Sticker ROCKETS = com.buran.game.labelsticker.Sticker.ROCKETS;
        Sector s7 = new Sector(7, "LEECH", Sector.Goal.RESCUE, 3,
                "Something is clinging to the hull. The counters are dropping. Radiation feeds on us while it holds on. The rockets will tear it off. Three more voices out here.",
                "RADIATION CLINGS AND EATS YOUR SCORE      {X} ROCKETS TEAR IT OFF");
        empty(s7);
        s7.chapter = 2;
        s7.radiationMax = 2;
        s7.astronautMax = 3;
        s7.startItems.put(ROCKETS, 3);
        s7.space = 1.8f;
        SECTORS.add(s7);

        Sector s8 = new Sector(8, "DRAIN", Sector.Goal.RESCUE, 3,
                "The fuel is falling, and we are not burning it. Magnets. They latch on and drink. Keep the tanks full: canisters are drifting nearby.",
                "MAGNETS DRAIN THE TANKS      CANISTERS REFILL THEM");
        empty(s8);
        s8.chapter = 2;
        s8.magneticMax = 2;
        s8.fuelMax = 3;
        s8.astronautMax = 3;
        s8.startItems.put(ROCKETS, 2);
        s8.space = 2.0f;
        SECTORS.add(s8);

        Sector s9 = new Sector(9, "COLD", Sector.Goal.RESCUE, 3,
                "They are coming in packs now. Too many to outrun. The freeze charge stops all of them at once, for a while. Use it when they close in.",
                "{X} FREEZE STOPS EVERY HUNTER AT ONCE");
        empty(s9);
        s9.chapter = 2;
        s9.corrosiveMax = 10;
        s9.corrosiveRespawn = 3f;
        s9.corrosiveHuntTime = 15f;
        s9.astronautMax = 3;
        s9.startItems.put(com.buran.game.labelsticker.Sticker.FREEZE, 3);
        s9.space = 2.0f;
        SECTORS.add(s9);

        Sector s10 = new Sector(10, "DEEP FIELD", Sector.Goal.SALVAGE, 40,
                "Deep field. Rock and wreckage everywhere. Burn the metal and pull it in: it charges the ram. Then let the ram do the work.",
                "{A} RAM SHIELD BREAKS WHAT IT OUTRANKS      METAL CHARGES IT");
        empty(s10);
        s10.chapter = 2;
        s10.asteroidMax = 14;
        s10.asteroidRespawn = 2.5f;
        s10.corrosiveMax = 4;
        s10.corrosiveRespawn = 8f;
        s10.startWithLaser = true;
        s10.space = 2.0f;
        SECTORS.add(s10);

        Sector s11 = new Sector(11, "STORM", Sector.Goal.RESCUE, 4,
                "A storm of discharges. They will blind the radar and kill the laser. Pulse them away, then hide in a shelter to get your shield back. Four of ours are in the storm.",
                "{Y} EMP COSTS SHIELD      SHELTERS GIVE IT BACK");
        empty(s11);
        s11.chapter = 2;
        s11.electricMax = 3;
        s11.shelters = 2;
        s11.astronautMax = 4;
        s11.asteroidMax = 6;
        s11.space = 2.2f;
        SECTORS.add(s11);

        Sector s12 = new Sector(12, "THE VOICE", Sector.Goal.RESCUE, 5,
                "The voice again. Come to the rift, it says. Five beacons around it. The field is collapsing, faster every minute. Do not wait.",
                "IT GETS WORSE EVERY MINUTE      DON'T WAIT");
        empty(s12);
        s12.chapter = 2;
        s12.blackhole = true;
        s12.shelters = 2;
        s12.astronautMax = 5;
        s12.corrosiveMax = 4;
        s12.corrosiveRespawn = 6f;
        s12.electricMax = 1;
        s12.radiationMax = 1;
        s12.magneticMax = 1;
        s12.asteroidMax = 10;
        s12.crateMax = 2;
        s12.fuelMax = 2;
        s12.borderDamage = true;
        s12.escalate = true;
        s12.boss = 2;
        s12.space = 2.6f;
        s12.startItems.put(ROCKETS, 2);
        s12.startItems.put(com.buran.game.labelsticker.Sticker.FREEZE, 1);
        SECTORS.add(s12);
    }

    /** Chapter titles for the flight plan. */
    public static String chapterTitle(int chapter) {
        return chapter == 1 ? "CHAPTER ONE  -  SILENCE" : chapter == 2 ? "CHAPTER TWO  -  THE VOICE"
                : chapter == 3 ? "CHAPTER THREE  -  HOME" : "CHAPTER " + chapter;
    }

    /** Station logs, one hidden in each sector (tips 60, 68, 88). */
    public static final String[] LOGS = {
            "Day one on Object-19. The light hums at night. Nobody sleeps well, and nobody says why.",
            "We measured it again. It is not getting brighter. It is getting closer.",
            "Lena paints the porthole frames blue so the station feels like home. Sasha says home is where the tea is.",
            "The radio picks up voices from the light. Old broadcasts, 1961, 1975. The commander has forbidden us to listen.",
            "The cargo bay is full of things nobody ordered. Crates with no manifest. Someone sealed them before we arrived.",
            "The experiment is tomorrow. The commander wrote a letter to his daughter and gave it to me. 'In case.'",
            "Something on the hull at night. The counters jump and the air tastes of metal.",
            "Fuel lines frozen again. The magnets drink from the tanks like animals at a river.",
            "They come in packs now. The old ones lead, the young ones follow. We learned to keep still.",
            "We built the ram from scrap and station plating. It is ugly. It works.",
            "The storm lasted nine hours. Two of the shelters held. We sang to keep awake.",
            "The commander says he heard his own voice from the rift, calling his name. He is not afraid. That frightens me.",
            "His crew drifted out the morning after. Their beacons still work. Batteries of the old kind, they never die.",
            "Everything that breaks falls toward the light. The graveyard grows every day.",
            "Someone is guarding the way. Not something. Someone.",
            "The rift pulls harder at night. Or what we call night. The clocks stopped on the fourth day.",
            "Radar dead. We fly by the stars and by the beacons. Strange: it is quieter like this.",
            "Last log of the station. If you read this, you got further than we did. Do not stop at the edge.",
            "The commander went in today. He said: it is 1991 inside, and the way home is open. We will see.",
    };

    /** The hidden cache crate of the sector being flown (null when none). */
    public static Object cache;

    /** The cache was opened: its log is kept and shows in the codex. */
    public static void cacheFound() {
        Sector s = current;
        cache = null;
        if (s == null) {
            return;
        }
        Preferences p = prefs();
        p.putBoolean("campaign.log." + s.number, true);
        p.flush();
        com.buran.game.ui.GameInfoSystem.setup(com.buran.game.player.Buran.position.x, com.buran.game.player.Buran.position.y,
                "[GOLD]STATION LOG " + s.code() + " FOUND[]", 4.0f, true);
    }

    public static boolean logFound(int number) {
        return prefs().getBoolean("campaign.log." + number, false);
    }

    /** Containers taken aboard in this run (sector goal CARGO counts them). */
    public static int cargoTaken;

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
        // The last sector of a chapter, cleared for the first time: its ending plays next.
        if (!isDone(s.number) && (next(s) == null || next(s).chapter != s.chapter)) {
            p.putInteger("campaign.chapterEnd", s.chapter);
        }
        if (s.number + 1 > unlocked()) {
            p.putInteger("campaign.unlocked", Math.min(TOTAL, s.number + 1));
        }
        p.putBoolean("campaign.done." + s.number, true);
        p.putInteger("campaign.fails." + s.number, 0);
        p.flush();
    }

    // ------------------------------------------------------------------ records

    /** No crew lost (rescue sectors). */
    public static final int MEDAL_SAFE = 1;
    /** The hull was never hit. */
    public static final int MEDAL_UNTOUCHED = 2;
    /** Done within the par time. */
    public static final int MEDAL_SWIFT = 4;

    /** Par time: about half a minute per crew member or crate, a few seconds per rock. */
    public static float par(Sector s) {
        return s.goal == Sector.Goal.SALVAGE ? s.target * 3.5f : 25f + s.target * 25f;
    }

    /** Saves the run; true if it is the best time for the sector. Medals add up over runs. */
    public static boolean record(Sector s, float seconds, int medals) {
        Preferences p = prefs();
        float best = best(s.number);
        boolean better = best <= 0f || seconds < best;
        if (better) {
            p.putFloat("campaign.best." + s.number, seconds);
        }
        p.putInteger("campaign.medals." + s.number, medals(s.number) | medals);
        p.flush();
        return better;
    }

    /** Best time in seconds, 0 if never cleared. */
    public static float best(int number) {
        return prefs().getFloat("campaign.best." + number, 0f);
    }

    public static int medals(int number) {
        return prefs().getInteger("campaign.medals." + number, 0);
    }

    public static String formatTime(float seconds) {
        int t = Math.round(seconds);
        return t / 60 + ":" + (t % 60 < 10 ? "0" : "") + t % 60;
    }

    /** A run of this sector ended in failure (for the quiet difficulty easing, tip 21). */
    public static void failed(Sector s) {
        Preferences p = prefs();
        p.putInteger("campaign.fails." + s.number, Math.min(3, fails(s.number) + 1));
        p.flush();
    }

    /** Failures in a row in this sector, 0..3; cleared when it is cleared. */
    public static int fails(int number) {
        return prefs().getInteger("campaign.fails." + number, 0);
    }

    /** The chapter whose ending is waiting to be shown (0 = none); clears it. */
    public static int takeChapterEnd() {
        Preferences p = prefs();
        int c = p.getInteger("campaign.chapterEnd", 0);
        if (c != 0) {
            p.putInteger("campaign.chapterEnd", 0);
            p.flush();
        }
        return c;
    }

    public static boolean isDone(int number) {
        return prefs().getBoolean("campaign.done." + number, false);
    }
}
