package com.shuttle.game.utils;

import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.asteroids.MetalAsteroidEmitter;
import com.shuttle.game.bot.asteroids.MineralAsteroidEmitter;
import com.shuttle.game.bot.consumable.AstronautEmitter;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.rocket.RocketEmitter;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.groups.ConfigGroup;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.panels.UILeftPanel;
import com.shuttle.game.ui.panels.UIRightPanel;

public class Restart {
    public static void doRestart() {
        Rules.GAME_OVER = false;
        ParticleEmitter.getInstance().dispose();
        MetalAsteroidEmitter.getInstance().dispose();
        MineralAsteroidEmitter.getInstance().dispose();
        CrateEmitter.getInstance().dispose();
        StorageEmitter.getInstance().dispose();
        AstronautEmitter.getInstance().dispose();
        ElectricalAnomalyEmitter.getInstance().dispose();
        CorrosiveAnomalyEmitter.getInstance().dispose();
        MagneticAnomalyEmitter.getInstance().dispose();
        RadiationAnomalyEmitter.getInstance().dispose();
        FuelEmitter.getInstance().dispose();
        RocketEmitter.getInstance().dispose();
        SelectorGroup.getInstance().dispose();
        SwitchGroup.getInstance().dispose();
        PauseGroup.getInstance().dispose();
        ConfigGroup.getInstance().dispose();
        UILeftPanel.getInstance().dispose();
        UIRightPanel.getInstance().dispose();
        Standing.getInstance().dispose();
        Shuttle.respawn();
    }
}
