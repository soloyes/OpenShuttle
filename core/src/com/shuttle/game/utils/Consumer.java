package com.shuttle.game.utils;

import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.panels.UILatestPanel;

public class Consumer {
    public static void consume(Sticker sticker, AbstractConsumable consumable) {
        sticker.consume(consumable);
        addToUI(sticker, consumable);
        addToSelector(sticker, consumable);
    }

    private static void addToUI(Sticker sticker, AbstractConsumable consumable) {
        UILatestPanel.getInstance().addLabel(sticker);
    }

    private static void addToSelector(Sticker sticker, AbstractConsumable consumable) {
        if (consumable.isGolden()) {
            if (Sticker.SELECTABLE.contains(sticker)) {
                SelectorGroup.getInstance().addSelector(sticker);
            }
        }
    }

    public static void select(Sticker sticker) {
        sticker.consume(Shuttle.position, false);
    }
}
