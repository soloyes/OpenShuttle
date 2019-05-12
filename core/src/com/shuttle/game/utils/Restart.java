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
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.panels.UIBonusPanel;
import com.shuttle.game.ui.panels.UILatestPanel;

public class Restart {
    public static void doRestart() {
        Rules.GAME_OVER = false;
        ParticleEmitter.getInstance().doRestart();
        MetalAsteroidEmitter.getInstance().doRestart();
        MineralAsteroidEmitter.getInstance().doRestart();
        CrateEmitter.getInstance().doRestart();
        StorageEmitter.getInstance().doRestart();
        AstronautEmitter.getInstance().doRestart();
        ElectricalAnomalyEmitter.getInstance().doRestart();
        CorrosiveAnomalyEmitter.getInstance().doRestart();
        MagneticAnomalyEmitter.getInstance().doRestart();
        RadiationAnomalyEmitter.getInstance().doRestart();
        FuelEmitter.getInstance().doRestart();
        RocketLauncher.getInstance().doRestart();
        SelectorGroup.getInstance().doRestart();
        SwitchGroup.getInstance().doRestart();
        PauseGroup.getInstance().doRestart();
        UILatestPanel.getInstance().doRestart();
        UIBonusPanel.getInstance().doRestart();
        Standing.getInstance().doRestart();
        Shuttle.respawn();
    }
}
