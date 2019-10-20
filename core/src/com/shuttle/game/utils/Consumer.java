package com.shuttle.game.utils;

import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.panels.UILeftPanel;

public class Consumer {
    public static void consume(Sticker sticker, AbstractConsumable consumable) {

        //todo pattertn вроде тоже рисуется..
        if (sticker != Sticker.SHABBY && sticker != Sticker.SHABBY_GOLD) {
            addToUI(sticker, consumable);
        }
        addToSelector(sticker, consumable);
    }

    private static void addToUI(Sticker sticker, AbstractConsumable consumable) {
        UILeftPanel.getInstance().add(sticker);
    }

    private static void addToSelector(Sticker sticker, AbstractConsumable consumable) {
        if (GamePreferences.isChildEnabled()) {
            if (Sticker.SELECTABLE.contains(sticker) && consumable.isGolden()) {
                SelectorGroup.getInstance().addSelector(sticker, 1);
                sticker.notify(consumable);
            } else {
                sticker.consume(consumable);
            }
        } else {
            if (Sticker.SELECTABLE.contains(sticker)) {
                int amount = 1;
                if (consumable.isGolden()) {
                    amount = Rules.SELECTOR_GOLDEN_COUNT;
                }
                SelectorGroup.getInstance().addSelector(sticker, amount);
                sticker.notify(consumable);
            } else {
                sticker.consume(consumable);
            }
        }
    }

    public static void select(Sticker sticker) {
        sticker.consume(Shuttle.position, false);
        sticker.sound();
    }
}