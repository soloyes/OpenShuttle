package com.buran.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.MoveProcessor;
import com.buran.game.effects.Vibrator;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.labelsticker.Label;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Consumer;
import com.buran.game.utils.Regions;

/**
 * @author soloyes on 10/11/18.
 */

public class Astronaut extends AbstractConsumable {
    private TextureRegion[] regions;
    private int multiplier;

    public Astronaut(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        this.regions = crewRegions();
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
        this.moveProcessor = new MoveProcessor();
        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.COMMANDER);
    }

    @Override
    public void badEffect() {
        Standing standing = Standing.getInstance();
        int score = (int) (Rules.CONSUMABLE_MURDER_SCORE_DECREASE * Sticker.COMMANDER.getCoefficient());
        standing.changeScore(-score, position, GameInfoSystem.Preset.MURDERER + "\n[RED]-" + (int) (Rules.CONSUMABLE_MURDER_SCORE_DECREASE * Sticker.COMMANDER.getCoefficient()) + "[]", 8.0f, true);
        boomBox.playSound(MSConstants.DESTROY_ASTRONAUT);
        Vibrator.vibrateGame(VConstants.PLAYER_ASTRONAUT_DESTROY);
    }

    @Override
    public void consumed() {
        int score = Rules.CONSUMABLE_ASTRONAUT_BASE_SCORE_ADD +
                multiplier * Rules.CONSUMABLE_ASTRONAUT_RANGE_SCORE_ADD +
                com.buran.game.player.Buran.ramLevel() * Rules.CONSUMABLE_ASTRONAUT_BONUS_SCORE_ADD;
        score = (int) (score * Sticker.COMMANDER.getCoefficient());
        Standing.getInstance().changeScore(score, position, GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]", 3.0f, true);
        boomBox.playSound(MSConstants.GET_ASTRONAUT);
        Sticker who = labels.get(Label.Layer.LABEL).get(0).getSticker();
        Consumer.consume(who, this);
        // Each profession brings something you can feel at once, and the ship says what.
        Standing st = Standing.getInstance();
        String gift;
        switch (who) {
            case MEDIC:
                st.changeHP(st.getMaxHp());
                gift = "MEDIC ABOARD  -  HULL REPAIRED";
                break;
            case MECHANIC:
                st.setLifeShield(st.getMaxLifeShield());
                gift = "MECHANIC ABOARD  -  SHIELD RESTORED";
                break;
            case SCIENTIST:
                st.changeFuel(st.getMaxFuel());
                gift = "SCIENTIST ABOARD  -  FUEL REFILLED";
                break;
            case MILITARY:
                st.changeLaserChargeLevel(st.getMaxLaserChargeLevel());
                gift = "MILITARY ABOARD  -  LASER CHARGED";
                break;
            default:
                st.changeHP(st.getMaxHp() * 0.5f);
                st.setLifeShield(Math.min(st.getMaxLifeShield(), st.getLifeShield() + st.getMaxLifeShield() * 0.5f));
                st.changeFuel(st.getMaxFuel() * 0.5f);
                gift = "COMMANDER ABOARD  -  ALL SYSTEMS +50%";
                break;
        }
        int cut = gift.indexOf("  -  ");
        com.buran.game.ui.hud.CrewCard.show(who, who.name(), cut > 0 ? gift.substring(cut + 5) : gift);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.active = true;
        this.taken = false;
        this.harvested = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
        if (fake) {
            this.multiplier = 0;
        } else {
            this.multiplier = MathUtils.random(0, 4);
        }
        this.labels.get(Label.Layer.LABEL).get(0).setSticker(Sticker.ASTRONAUTS.get(multiplier));
        this.texture = regions[multiplier];
        // Painted crew have their own shape: keep the proportions, the longest side about 200.
        float side = Math.max(texture.getRegionWidth(), texture.getRegionHeight());
        float k = painted ? 200f / side : PaintConstants.ASTRONAUT_WIDTH / side;
        this.width = (int) (texture.getRegionWidth() * k);
        this.height = (int) (texture.getRegionHeight() * k);
        playSound();
    }

    private static TextureRegion[] crew;
    private static boolean painted;

    /**
     * One region per profession (COMMANDER, MEDIC, SCIENTIST, MECHANIC, MILITARY): the painted
     * sprites in assets/sprites when present, the old atlas strip otherwise.
     */
    public static TextureRegion[] crewRegions() {
        if (crew != null) {
            return crew;
        }
        String[] n = {"commander", "medic", "scientist", "mechanic", "military"};
        TextureRegion[] r = new TextureRegion[5];
        boolean all = true;
        for (int i = 0; i < 5; i++) {
            com.badlogic.gdx.files.FileHandle f = com.badlogic.gdx.Gdx.files.internal("sprites/sprite_crew_" + n[i] + ".png");
            if (!f.exists()) {
                all = false;
                break;
            }
            com.badlogic.gdx.graphics.Texture t = new com.badlogic.gdx.graphics.Texture(f);
            t.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
            r[i] = new TextureRegion(t);
        }
        painted = all;
        crew = all ? r : Regions.split(Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION), 1, 5, 5);
        return crew;
    }

    public Sticker getProfession() {
        return labels.get(Label.Layer.LABEL).get(0).getSticker();
    }

    private void playSound() {
        if (!fake && boundCheck.checkAndHandleBoundsInCamera(this)) {
            boomBox.playSound(MSConstants.NEW_ASTRONAUT);
        }
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.keepInside(this);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision();
    }
}