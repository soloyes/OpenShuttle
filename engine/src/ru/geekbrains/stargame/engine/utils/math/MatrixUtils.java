package ru.geekbrains.stargame.engine.utils.math;


import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;

import ru.geekbrains.stargame.engine.Rect;

public class MatrixUtils {

    /**
     * Calculation for matrix 4*4
     * @param mat destanation matrix
     * @param src source rectangle
     * @param dst destanation rectangle
     */

    public static void calcTransitionMatrix(Matrix4 mat, Rect src, Rect dst) {
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        mat
                .idt()
                .translate(dst.pos.x, dst.pos.y, 0f)
                .scale(scaleX, scaleY, 1f)
                .translate(-src.pos.x, -src.pos.y, 0f);
    }

    /**
     * Calculation for matrix 3*3
     * @param mat destanation matrix
     * @param src source rectangle
     * @param dst destanation rectangle
     */

    public static void calcTransitionMatrix(Matrix3 mat, Rect src, Rect dst) {
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        mat
                .idt()
                .translate(dst.pos.x, dst.pos.y)
                .scale(scaleX, scaleY)
                .translate(-src.pos.x, -src.pos.y);
    }
}