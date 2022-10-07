package com.application.javagame;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {

	Screen screen;
	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		screen = new PlayScreen(this);
		setScreen(screen);

		AssetManager manager = Assets.getManager();
		manager.load("snake.jpg", Texture.class);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

}
