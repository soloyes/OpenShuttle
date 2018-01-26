package ru.geekbrains.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture logo;
	TextureRegion textureRegion;
	float step = 1.0f;
	int direction;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("stars.jpg");
		logo = new Texture("badlogic.jpg");
		textureRegion = new TextureRegion(img, 0,0, 256, 256);
	}

	@Override
	public void render () {
		int x = Gdx.graphics.getWidth();
		int y = Gdx.graphics.getHeight();

		if (step == 1) direction = 1;
		if (step == 100) direction = -1;
		step += 1 * direction;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int repeatX = 2 + x / 256;
		int repeatY = 1 + y / 256;
		batch.begin();
		for (int i = 0; i <= repeatX; i++) {
			for (int j = 0; j <= repeatY; j++) {
				batch.draw(textureRegion, i*256, j*256);
			}
		}

		batch.draw(logo,step , 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
