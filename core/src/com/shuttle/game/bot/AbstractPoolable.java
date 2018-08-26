package com.shuttle.game.bot;

import lombok.Data;

/**
 * @author Shuttle on 8/26/18.
 */
@Data
public abstract class AbstractPoolable implements Poolable {
    protected int width, height;
}
