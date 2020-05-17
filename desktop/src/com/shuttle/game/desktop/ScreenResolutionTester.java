package com.shuttle.game.desktop;

public enum ScreenResolutionTester {
    FOUR_TO_THREE(768, 1024, 3, 4),
    FIVE_TO_THREE(960, 1600, 3, 5),
    FIVE_TO_FOUR(1024, 1280, 4, 5),
    SEXTEEN_TO_TEN(960, 1536, 10, 16),
    SIXTEEN_TO_NINE(1080, 1920, 9, 16),
    TWENTY_ONE_TO_NINE(1080, 2520, 9, 21),
    TWENTY_FIVE_TO_SIXTEEN(1024, 1600, 16, 25);

    private int width;
    private int height;
    private int xRatio;
    private int yRatio;

    ScreenResolutionTester(int width, int height, int xRatio, int yRatio) {
        this.width = width;
        this.height = height;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public int getHalfWidth() {
        return width / 2;
    }

    public int getHalfHeight() {
        return height / 2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}