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
        SALVAGE
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
    /** Start with a charged laser (the sector that teaches it). */
    public boolean startWithLaser;

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
            default:
                return "";
        }
    }
}
