package ru.geekbrains.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.geekbrains.game.Star2DGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//config.width = 1280;
		//config.height = 720;

		//config.fullscreen = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		new LwjglApplication(new Star2DGame(), config);

//		Matrix4 matrix4 = new Matrix4();
//		Rect rect = new Rect(4f,4f,2f,1f);
//		Rect rect1 = new Rect(6f,8f,1f,1f);
//		MatrixUtils.calcTransitionMatrix(matrix4, rect, rect1);
//		System.out.println(matrix4);
//		System.out.println(new Vector3(2,3,0).mul(matrix4));
//		System.out.println(new Vector3(6,3,0).mul(matrix4));
	}
}