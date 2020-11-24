package com.buran.game.labelsticker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomalyEmitter;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomalyEmitter;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.bot.consumable.Crate;
import com.buran.game.bot.consumable.CrateEmitter;
import com.buran.game.bot.consumable.FuelEmitter;
import com.buran.game.bot.consumable.Storage;
import com.buran.game.bot.consumable.StorageEmitter;
import com.buran.game.bot.consumable.fuel.Fuel;
import com.buran.game.bot.rocket.RocketLauncher;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.AsteroidLaunchEmitter;
import com.buran.game.effects.BoomBox;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.player.navigation.Radar;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.panels.UILeftPanel;
import com.buran.game.utils.pool.AbstractPoolable;
import com.buran.game.utils.pool.ObjectPool;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 3/27/19.
 */

public enum Sticker {
    //Crate
    FREEZE("[#FFFFFF]FREEZE[]\n[#cc6e54]Stop as much anomalies as possible![]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                Sticker.abstractAnomalyFeed(((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getActiveList());
                Sticker.abstractAnomalyFeed(((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).getActiveList());
                Sticker.abstractAnomalyFeed(((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).getActiveList());
                Sticker.abstractAnomalyFeed(((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).getActiveList());
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.FREEZE + "[]",
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.FREEZE);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_FREEZE);
        }
    },
    DEGRADE("[#FFFFFF]DEGRADE[]\n[#cc6e54]You able to change the anomaly level. This items right for that.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                List<AbstractAnomaly> anomalies = ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    anomalies.get(i).setLevel(0);
                }
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.DEGRADE + "[]",
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.DEGRADE);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_DEGRADE);
        }
    },
    BOOST("[#FFFFFF]BOOST[]\n[#cc6e54]Acceleration is the rate of change in the velocity of the ship with respect to time. When you get this object, you can activate it from the Storage menu at the top. Warning! When velocity is too high, your ship can get more damage. Or you may kill more anomalies.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                float d = Rules.PLAYER_BOOST_ENGINE_POWER - Buran.velocity.len();
                Buran.setShadowsCoordinates();
                Buran.velocity.add(
                        d * (float) Math.cos(Math.toRadians(Buran.angle)),
                        d * (float) Math.sin(Math.toRadians(Buran.angle))
                );
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.TURBO + "[] x" + (int) Rules.PLAYER_ENGINE_POWER_FACTOR,
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.TURBO);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_BOOST);
        }
    },
    REPAIR("[#FFFFFF]REPAIR[]\n[#cc6e54]Restores HP by replacement of components or fixing what's broken. Your ship is damaged when it collides with asteroids, black holes and anomalies. Guard it like the apple of your eye and don't cross the borders![]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                standing.changeHP(Buffs.getRepairAdd());
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.REPAIR + "[] +" + Buffs.getRepairAdd(),
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.REPAIR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_REPAIR);
        }
    },
    SHIELD("[#FFFFFF]SHIELD[]\n[#cc6e54]A shield is a round piece of armor. Your shield may bite an anomaly with a matching color. Remember that the shield needs to be charged! Just do the harvesting and look around.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                List<AbstractConsumable> list = crateEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                FuelEmitter fuelEmitter = (FuelEmitter) ObjectPool.EMITTERS.get(Fuel.class);
                list = fuelEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                list = storageEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.SHIELD + "[] " + (int) Rules.CONSUMABLE_SHIELD_TIME,
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SHIELD);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SHIELD);
        }
    },
    TRASH("[#FFFFFF]TRASH[]\n[#cc6e54]A box like this can give you many interesting possibilities. Don't be afraid - when you open it, you can earn more points.[]") {
        int score;

        @Override
        public void consume(Vector2 position, boolean isGolden) {
            AsteroidLaunchEmitter asteroidLaunchEmitter = (AsteroidLaunchEmitter) ObjectPool.EMITTERS.get(AsteroidLaunchEmitter.Element.class);
            asteroidLaunchEmitter.addToList();
            score = Rules.CONSUMABLE_TRASH_BASE_SCORE *
                    (MathUtils.random(Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN, Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX) *
                            ((isGolden) ? Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_FACTOR : 1)
                    );
            score = (int) (score * Sticker.COMMANDER.getCoefficient());
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            standing.changeScore(score, consumable.getPosition(), (consumable.isGolden() ? "[GOLD]" : "[WHITE]") + GameInfoSystem.Preset.TRASH + "[] " + (consumable.isGolden() ? "[WHITE]" : "[GREEN]+") + score + "[]", 3.0f, true);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_TRASH);
        }
    },
    SPLASH("[#FFFFFF]SPLASH[]\n[#cc6e54]This item helps to clear the anomalies from the map. Not every anomaly can be defeated by Splash, so a few experiments will be required.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    if (anomalies.get(i).getPosition().dst(Buran.position) <= Rules.WORLD_WIDTH) {
                        if (!anomalies.get(i).isTaken()) {
                            anomalies.get(i).setMarkedToBoom(true);
                        }
                    }
                }
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.SPLASH + "[]",
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SPLASH);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SPLASH);
        }
    },
    SCAN("[#FFFFFF]SCAN[]\n[#cc6e54]You may find boxes that are difficult to recognize. They can contain anything. Use the scanner to peek inside.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                List<AbstractConsumable> list = crateEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
                StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                list = storageEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.SCAN + "[] " + (int) Rules.CONSUMABLE_SCAN_TIME,
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SCAN);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SCANNER);
        }
    },
    //Storage
    LASER("[#FFFFFF]LASER[]\n[#cc6e54]You can destroy asteroids at a distance if your laser scale charge is non-zero. The purple scale indicates the presence of a laser and gives you the option of automatic fire.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            Buran.isLaser = true;
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.LASER);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_LASER);
        }
    },
    RADAR("[#FFFFFF]RADAR[]\n[#cc6e54]Our space is a boundless two-dimensional area where objects and events have relative position and direction. Nonetheless, it's good that there is a device that shows the location of those objects.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            Radar.isActive = true;
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.RADAR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_RADAR);
        }
    },
    ROCKETS("[#FFFFFF]ROCKETS[]\n[#cc6e54]When there is no way out, you can smash everything around. Indeed, powerful weapons take time to recharge. Go find some ammunition and activate it![]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                RocketLauncher.launch();
                GameInfoSystem.setup(
                        position.x, position.y,
                        "[GREEN]" + GameInfoSystem.Preset.ROCKETS + "[]",
                        5.0f,
                        true
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.ROCKETS);
            boomBox.playSound(MSConstants.GET_ROCKETS);
        }

        @Override
        public void sound() {
        }
    },
    //Fuel
    FUEL("[#FFFFFF]FUEL[]\n[#cc6e54]The ship cannot fly if there is no fuel, but still it can move. Obviously, the speed may not be enough to get away from the attacking anomalies.[]") {
        float fuel;

        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (isGolden) {
                fuel = Standing.getInstance().getMaxFuel();
            } else {
                fuel = Rules.CONSUMABLE_FUEL_ADD_FACTOR *
                        MathUtils.random(
                                Rules.CONSUMABLE_FUEL_MIN_ADD,
                                Rules.CONSUMABLE_FUEL_MAX_ADD
                        );
            }
            Standing.getInstance().changeFuel(fuel);

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.FUEL);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_FUEL);
        }
    },
    FUEL_UI("[#FFFFFF]FUEL[]\n[#cc6e54]The ship cannot fly if there is no fuel, but still it can move. Obviously, the speed may not be enough to get away from the attacking anomalies.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Surprise
    PATTERN("[#FFFFFF]PATTERN[]\n[#cc6e54]Not all boxes can be labeled. This happens for a variety of reasons...[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    PATTERN_GOLD("[#FFFFFF]GOLD PATTERN[]\n[#cc6e54]There is a theory that gold boxes have great potential. However, this holds high risks for the ship. Like the outer space, the space of the possibilities inside these boxes is limitless.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY("[#FFFFFF]SHABBY[]\n[#cc6e54]Space debris, radiation, and other aggressive phenomena completely destroy the ability to scan such boxes. They shouldn't be dangerous, but no one ever explored the possibilities of such forgotten objects.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY_GOLD("[#FFFFFF]GOLD SHABBY[]\n[#cc6e54]Our scientists cannot determine what it is.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Astronaut
    COMMANDER("[#FFFFFF]COMMANDER[]\n[#cc6e54]Every troop needs a leader. A real chief gets more points, his Buran is faster than fast and evades the obstacles like a snake.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Standing.getInstance().incrementScore();
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MECHANIC("[#FFFFFF]MECHANIC[]\n[#cc6e54]His SHIELD BAR is a full metal jacket. Effectively repairs holes from fuel anomalies and enrich reloads of REPAIR and BOOST items.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Standing.getInstance().incrementMaxLifeShield();
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MEDIC("[#FFFFFF]MEDIC[]\n[#cc6e54]Boosts health for all teammates and keeps an extended HEALTH BAR in his first-aid kit. Cures any damage with a band-aid and aspirin like a field medic.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Standing.getInstance().incrementMaxHP();
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MILITARY("[#FFFFFF]MILITARY[]\n[#cc6e54]Armed and dangerous. Fights longer, reloads faster. No doubt, he is a loaded gun who is extending LASER BAR.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Standing.getInstance().incrementLaserChargeLevel();
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SCIENTIST("[#FFFFFF]SCIENTIST[]\n[#cc6e54]Ph.D. in fuel consumption efficiency, extended FUEL BAR. Been studying laser impact on asteroids and some types of anomalies since high school.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Standing.getInstance().incrementMaxFuel();
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    };

    Sticker(String description) {
        this.description = description;
    }

    public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS, FREEZE, DEGRADE);
    public static List<Sticker> ASTRONAUTS = new LinkedList<Sticker>(Arrays.asList(COMMANDER, MEDIC, SCIENTIST, MECHANIC, MILITARY));
    public static List<Sticker> ALL = Arrays.asList(Sticker.values());
    private static BoomBox boomBox;
    private static Standing standing;
    @Getter
    private String description;
    @Getter
    @Setter
    private float coefficient = Rules.STANDARD_COEFFICIENT;

    static {
        boomBox = new BoomBox();
        standing = Standing.getInstance();
    }

    public abstract void consume(Vector2 position, boolean isGolden);

    public abstract void consume(AbstractConsumable consumable);

    public abstract void notify(AbstractConsumable consumable);

    public abstract void sound();

    private static void consumeShabby(AbstractConsumable consumable) {
        if (MathUtils.random(0, 1) == 0) {
            getPositive(consumable);
        } else {
            getNegative(consumable);
        }
    }

    private static void getPositive(AbstractConsumable consumable) {
        // 0 - Kill related level corrosive anomaly
        // 1 - Add score
        // 2 - Add manual activated loot, or generate Crate/Storage
        // 3 - Kill Fuel/Radiation anomaly, or add HP/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        switch (r) {
            case 0:
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    AbstractAnomaly anomaly = anomalies.get(i);
                    if (anomaly.getLevel() == level && !anomaly.isTaken()) {
                        anomaly.setMarkedToBoom(true);
                    }
                }
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_FLUSH,
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.ANOMALY_FLUSH);
                break;
            case 1:
                int score = (level + 1) * Rules.SCORE_BASE_BONUS;
                score = (int) (score * Sticker.COMMANDER.getCoefficient());
                Standing.getInstance().changeScore(score, consumable.getPosition(), GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]", 5.0f, true);
                boomBox.playSound(MSConstants.ADD_SCORE);
                break;
            case 2:
                if (consumable.isGolden()) {
                    for (int i = 0; i < Sticker.SELECTABLE.size(); i++) {
                        for (int j = 0; j < Rules.SELECTOR_SHABBY_BASE_BONUS; j++) {
                            SelectorGroup.getInstance().addSelector(Sticker.SELECTABLE.get(i), consumable.getType() == AbstractPoolable.Type.CRATE ? 1 : 2);
                            UILeftPanel.getInstance().add(Sticker.SELECTABLE.get(i));
                        }
                    }
                    GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            GameInfoSystem.Preset.BIG_BONUS,
                            5.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.ADD_SELECTABLE);
                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                                crateEmitter.createBonusCrate();
                            }
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_CRATE,
                                    5.0f,
                                    true
                            );
                            break;
                        case STORAGE:
                            StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_STORAGE,
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                storageEmitter.createBonusStorage();
                            }
                            break;
                    }
                    boomBox.playSound(MSConstants.GENERATE_CRATE_STORAGE);
                }
                break;
            case 3:
                if (consumable.isGolden()) {
                    switch (consumable.getType()) {
                        case CRATE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            boomBox.playSound(MSConstants.ADD_HP);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "+[GREEN]" + GameInfoSystem.Preset.HP + "[]",
                                    8.0f,
                                    true
                            );
                            break;
                        case STORAGE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            Standing.getInstance().changeLives(1);
                            boomBox.playSound(MSConstants.UP);
                            break;
                    }
                } else {
                    List<AbstractAnomaly> list;
                    switch (consumable.getType()) {
                        case CRATE:
                            MagneticAnomalyEmitter magneticAnomalyEmitter = (MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class);
                            list = magneticAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    list.get(i).destroyWithEffect();
                                }
                            }
                            break;
                        case STORAGE:
                            RadiationAnomalyEmitter radiationAnomalyEmitter = (RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class);
                            list = radiationAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    anomaly.destroyWithEffect();
                                }
                            }
                            break;
                    }
                    GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            GameInfoSystem.Preset.ANOMALY_FLUSH,
                            5.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.KILL_MAGNETIC_RADIATION);
                    break;
                }
        }
    }

    private static void getNegative(AbstractConsumable consumable) {
        // 0 - Add related level corrosive anomaly
        // 1 - Del score
        // 2 - Disable Laser/Radar, or del Crate/Storage
        // 3 - Add Electrical/Corrosive/Radiation anomaly, or del HP/fuel/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        switch (r) {
            case 0:
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                corrosiveAnomalyEmitter.createBonusAnomaly(consumable.getPosition(), level);
                for (int i = 0; i < (4 - level) * Rules.ANOMALY_GENERATE_BASE_BONUS; i++) {
                    corrosiveAnomalyEmitter.createBonusAnomaly(level);
                }
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_ARISE,
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.ADD_CORROSIVE);
                break;
            case 1:
                int score = (level + 1) * Rules.SCORE_BASE_BONUS;
                score = (int) (score * Sticker.COMMANDER.getCoefficient());
                Standing.getInstance().changeScore(-score, consumable.getPosition(), GameInfoSystem.Preset.SCORE + " -[RED]" + score + "[]", 5.0f, true);
                boomBox.playSound(MSConstants.DEL_SCORE);
                break;
            case 2:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            Radar.isActive = false;
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.RADAR + "[]",
                                    5.0f,
                                    true
                            );
                        } else {
                            Buran.isLaser = false;
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.LASER + "[]",
                                    5.0f,
                                    true
                            );
                        }
                        boomBox.playSound(MSConstants.DISABLE_LASER_RADAR);
                        break;
                    case STORAGE:
                        List<AbstractConsumable> list;
                        GameInfoSystem.Preset preset;
                        if (consumable.isGolden()) {
                            StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                            list = storageEmitter.getActiveList();
                            preset = GameInfoSystem.Preset.STORAGE_FLUSH;
                        } else {
                            CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                            list = crateEmitter.getActiveList();
                            preset = GameInfoSystem.Preset.CRATE_FLUSH;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            AbstractConsumable c = list.get(i);
                            if (!c.isTaken()) {
                                c.destroyWithEffect();
                            }
                        }
                        GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                preset,
                                5.0f,
                                true
                        );
                        boomBox.playSound(MSConstants.DEL_CRATE_STORAGE);
                        break;
                }
                break;
            case 3:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            Standing.getInstance().changeLives(-1);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-1[RED] " + GameInfoSystem.Preset.UP + "[]",
                                    5.0f,
                                    true
                            );
                            boomBox.playSound(MSConstants.DOWN);
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                Standing.getInstance().changeHP(-Standing.getInstance().getHp() + 1);
                                GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.HP + "[]",
                                        5.0f,
                                        true
                                );
                            } else {
                                Standing.getInstance().changeFuel(-Standing.getInstance().getFuel());
                                GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.FUEL + "[]",
                                        5.0f,
                                        true
                                );
                            }
                            boomBox.playSound(MSConstants.DEL_HP_FUEL);
                        }
                        break;
                    case STORAGE:
                        if (consumable.isGolden()) {
                            ElectricalAnomalyEmitter electricalAnomalyEmitter = (ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class);
                            electricalAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                RadiationAnomalyEmitter radiationAnomalyEmitter = (RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class);
                                radiationAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            } else {
                                MagneticAnomalyEmitter magneticAnomalyEmitter = (MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class);
                                magneticAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            }
                        }
                        GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                GameInfoSystem.Preset.ANOMALY_ARISE,
                                5.0f,
                                true
                        );
                        boomBox.playSound(MSConstants.ANOMALIES_BORN);
                        break;
                }
                break;
        }
    }

    private static void print(AbstractConsumable consumable, GameInfoSystem.Preset p) {
        GameInfoSystem.setup(
                consumable.getPosition().x, consumable.getPosition().y,
                (consumable.isGolden() ? "[GOLD]" : "[WHITE]") + p + "[]",
                (consumable.isGolden() ? 7.0f : 3.0f),
                true
        );
    }

    private static void abstractAnomalyFeed(List<AbstractAnomaly> anomalies) {
        for (int i = 0; i < anomalies.size(); i++) {
            if (anomalies.get(i).isHungry()) {
                anomalies.get(i).setHungry(false);
            }
        }
    }
}