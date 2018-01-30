package ru.geekbrains.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private Texture player;
	private FPSLogger logger;
	private float height;
	private float width;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("Grid1.png");
		player = new Texture("Player_Attack.png");
		logger = new FPSLogger();
	}

	@Override
	public void render () {
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//		System.out.println((int) (height/img.getHeight() + 1) +
//				" " +
//				(int) (width/img.getWidth() + 1));
		for (int i = 0; i < (int) (height/img.getHeight() + 2); i++) {
			for (int j = 0; j < (int) (width/img.getWidth() + 2); j++) {
				batch.begin();
				batch.draw(img,img.getWidth()*i, img.getHeight()*j);
				batch.end();
			}
		}

		//////
		logger.log();
		//////
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		player.dispose();
	}
}
