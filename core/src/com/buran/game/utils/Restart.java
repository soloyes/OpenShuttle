package com.buran.game.utils;

import com.buran.game.ui.panels.UILeftPanel;
import com.buran.game.ui.panels.UIRightPanel;

/**
 * @author soloyes on 02/05/19.
 */

public class Restart {
    public static void doRestart() {
        com.buran.game.ui.groups.configs.RightGroup.getInstance().dispose();
        com.buran.game.ui.groups.configs.LeftGroup.getInstance().dispose();
        com.buran.game.ui.groups.selector.SelectorGroup.getInstance().dispose();
        com.buran.game.ui.groups.switches.SwitchGroup.getInstance().dispose();
        UILeftPanel.getInstance().dispose();
        UIRightPanel.getInstance().dispose();
    }
}