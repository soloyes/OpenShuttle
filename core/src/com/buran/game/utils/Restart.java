package com.buran.game.utils;

import com.buran.game.effects.Shake;
import com.buran.game.ui.groups.configs.LeftGroup;
import com.buran.game.ui.groups.configs.RightGroup;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.groups.switches.SwitchGroup;
import com.buran.game.ui.panels.UILeftPanel;
import com.buran.game.ui.panels.UIRightPanel;

/**
 * @author soloyes on 02/05/19.
 */

public class Restart {
    public static void doRestart() {
        RightGroup.getInstance().dispose();
        LeftGroup.getInstance().dispose();
        SelectorGroup.getInstance().dispose();
        SwitchGroup.getInstance().dispose();
        UILeftPanel.getInstance().dispose();
        UIRightPanel.getInstance().dispose();
        Shake.getInstance().dispose();
    }
}