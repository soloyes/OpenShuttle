package com.buran.game.campaign;

/**
 * One sector of the campaign: what the player must do, which threats are present, which single
 * mechanic it teaches, and the radio briefing that opens it (tips 1, 2, 4, 28).
 */
public final class Sector {
    public enum Goal {
        /** Take N crew members aboard. */
        RESCUE,
        /** Burn and salvage N asteroids with the laser and tractor beam. */
        SALVAGE,
        /** Take N crates or storages aboard. */
        CARGO
    }

    public final int number;
    public final String name;
    public final Goal goal;
    public final int target;
    /** Radio briefing typed at the start. */
    public final String briefing;
    /** One-line hint for the mechanic this sector introduces (null = none). */
    public final String lesson;

    // Threat table.
    public int corrosiveMax = 40;
    public float corrosiveRespawn = 2f;
    public float corrosiveHuntTime = 60f;
    public int electricMax = 1;
    public int radiationMax = 2;
    public int magneticMax = 2;
    public int asteroidMax = 50;
    public float asteroidRespawn = 4f;
    public int astronautMax = 3;
    public int shelters = 2;
    /** Size of the sector in screens: a room sized to its task, not an empty ocean. */
    public float space = 3f;
    /** The rift is a lesson of its own; earlier sectors do not have it. */
    public boolean blackhole = true;
    /** Loose cargo: few things on the map, so the goal reads clearly (tip: one focus at a time). */
    public int crateMax = 4;
    public int storageMax = 3;
    public int fuelMax = 3;
    /** Whether crossing the edge of space hurts (off in the training sectors). */
    public boolean borderDamage = true;
    /** Start with a charged laser (the sector that teaches it). */
    public boolean startWithLaser;
    /** Containers are sealed or burnt about one time in (odds + 1). */
    public int dangerOdds = 4;
    /** Items in the bay at the start (the sector that teaches them). */
    public final java.util.LinkedHashMap<com.buran.game.labelsticker.Sticker, Integer> startItems = new java.util.LinkedHashMap<>();
    /** The field grows harder every 40 seconds (more hunters, faster respawn). */
    public boolean escalate;
    /** A boss halfway through: 0 none, 1 the Mother (corrosive), 2 the Storm Heart (electric). */
    public int boss;
    /** Halfway: one of ours or the fuel depot - the rift takes the other (tip 41). */
    public boolean choice;
    /** The radar is dead for the whole sector (flying by eye). */
    public boolean noRadar;
    /** The extraction beacon sits at the edge of the rift (the way home). */
    public boolean beaconAtRift;
    /**
     * What a crate may hold here: only things that make sense in this sector. TRASH never (it
     * launches rocks); anomaly tools only where there are anomalies.
     */
    public java.util.List<com.buran.game.labelsticker.Sticker> loot() {
        java.util.List<com.buran.game.labelsticker.Sticker> l = new java.util.ArrayList<>();
        l.add(com.buran.game.labelsticker.Sticker.REPAIR);
        l.add(com.buran.game.labelsticker.Sticker.SHIELD);
        l.add(com.buran.game.labelsticker.Sticker.SCAN);
        l.add(com.buran.game.labelsticker.Sticker.BOOST);
        if (corrosiveMax > 0) {
            l.add(com.buran.game.labelsticker.Sticker.FREEZE);
            l.add(com.buran.game.labelsticker.Sticker.DEGRADE);
            l.add(com.buran.game.labelsticker.Sticker.SPLASH);
        }
        if (corrosiveMax + electricMax + radiationMax + magneticMax > 0) {
            l.add(com.buran.game.labelsticker.Sticker.ROCKETS);
        }
        return l;
    }

    /** A single storage's content here: rockets, or the radar where there is one to restore. */
    public com.buran.game.labelsticker.Sticker storageLoot() {
        if (!noRadar && com.badlogic.gdx.math.MathUtils.randomBoolean()) {
            return com.buran.game.labelsticker.Sticker.RADAR;
        }
        return com.buran.game.labelsticker.Sticker.ROCKETS;
    }

    /** Chapter this sector belongs to (headers on the flight plan). */
    public int chapter = 1;

    Sector(int number, String name, Goal goal, int target, String briefing, String lesson) {
        this.number = number;
        this.name = name;
        this.goal = goal;
        this.target = target;
        this.briefing = briefing;
        this.lesson = lesson;
    }

    public String code() {
        return (number < 10 ? "0" : "") + number;
    }

    public String goalText() {
        switch (goal) {
            case RESCUE:
                return "RESCUE CREW";
            case SALVAGE:
                return "SALVAGE ASTEROIDS";
            case CARGO:
                return "RECOVER CARGO";
            default:
                return "";
        }
    }
}
