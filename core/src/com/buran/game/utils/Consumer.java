package com.buran.game.utils;

/**
 * @author soloyes on 28/12/18.
 */

public class Consumer {
    public static void consume(Sticker sticker, com.buran.game.bot.consumable.AbstractConsumable consumable) {
        if (sticker != Sticker.SHABBY && sticker != Sticker.SHABBY_GOLD) {
            addToUI(sticker, consumable);
        }
        addToSelector(sticker, consumable);
    }

    private static void addToUI(Sticker sticker, com.buran.game.bot.consumable.AbstractConsumable consumable) {
        UILeftPanel.getInstance().add(sticker);
    }

    private static void addToSelector(Sticker sticker, com.buran.game.bot.consumable.AbstractConsumable consumable) {
        if (Sticker.SELECTABLE.contains(sticker)) {
            int amount = 1;
            if (consumable.isGolden()) {
                amount = com.buran.game.conf.Rules.SELECTOR_GOLDEN_COUNT;
            }
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance().addSelector(sticker, amount);
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