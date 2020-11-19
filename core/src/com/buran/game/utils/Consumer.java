package com.buran.game.utils;

import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.conf.Rules;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Buran;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.panels.UILeftPanel;

/**
 * @author soloyes on 28/12/18.
 */

public class Consumer {
    public static void consume(Sticker sticker, AbstractConsumable consumable) {
        if (sticker != Sticker.SHABBY && sticker != Sticker.SHABBY_GOLD) {
            if (!Sticker.ASTRONAUTS.contains(sticker)) {
                addToUI(sticker, consumable);
            }
        }
        addToSelector(sticker, consumable);
    }

    private static void addToUI(Sticker sticker, AbstractConsumable consumable) {
        UILeftPanel.getInstance().add(sticker);
    }

    private static void addToSelector(Sticker sticker, AbstractConsumable consumable) {
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

    public static void select(Sticker sticker) {
        sticker.consume(Buran.position, false);
        sticker.sound();
    }
}