package com.shuttle.game.utils;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.ui.groups.configs.LeftGroup;
import com.shuttle.game.ui.groups.configs.RightGroup;
import com.shuttle.game.ui.groups.mvc.results.DetailedResultGroup;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.groups.switches.SwitchGroup;
import com.shuttle.game.ui.panels.UILeftPanel;
import com.shuttle.game.ui.panels.UIRightPanel;

/**
 * @author Shuttle on 02/05/19.
 */

public class Restart {
    public static void doRestart() {
        Rules.GAME_OVER = false;
        Standing.getInstance().dispose();
        DetailedResultGroup.getInstance().dispose();
        RightGroup.getInstance().dispose();
        LeftGroup.getInstance().dispose();
        SelectorGroup.getInstance().dispose();
        SwitchGroup.getInstance().dispose();
        PauseGroup.getInstance().dispose();
        ConfigGroup.getInstance().dispose();
        UILeftPanel.getInstance().dispose();
        UIRightPanel.getInstance().dispose();
        Shuttle.respawn();
    }
}