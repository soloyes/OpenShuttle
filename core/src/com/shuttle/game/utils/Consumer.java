package com.shuttle.game.utils;

import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;

public class Consumer {
    public static void consume(GameScreen gs, Sticker sticker, AbstractConsumable consumable) {
        sticker.consume(gs, consumable);
        addToUI(gs, sticker, consumable);
        addToSelector(gs, sticker, consumable);
    }

    private static void addToUI(GameScreen gs, Sticker sticker, AbstractConsumable consumable) {
        gs.getUiLatestPanel().addLabel(sticker);
    }

    private static void addToSelector(GameScreen gs, Sticker sticker, AbstractConsumable consumable) {
        if (consumable.isGolden()) {
            if (Sticker.SELECTABLE.contains(sticker)) {
                gs.getSelectorGroup().addSelector(sticker);
            }
        }
    }

    public static void select(GameScreen gs, Sticker sticker) {
        sticker.consume(gs, Shuttle.position, false);
    }
}
