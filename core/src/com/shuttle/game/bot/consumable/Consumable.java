package com.shuttle.game.bot.consumable;

import com.shuttle.game.screens.GameScreen;

public interface Consumable {
    void consumed(GameScreen gs);

    void badEffect(GameScreen gs);
}
