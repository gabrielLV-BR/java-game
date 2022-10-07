package com.application.javagame;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {

	Screen screen;
	SpriteBatch batch;

	AssetManager assetManager;
	InputManager inputManager;

	@Override
	public void create() {
		assetManager = Assets.getManager();
		assetManager.load("snake.jpg", Texture.class);

		inputManager = InputManager.getManager();

		batch = new SpriteBatch();

		screen = new PlayScreen(this);
		setScreen(screen);
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override public void render() { super.render(); }

	@Override public void dispose() {
		assetManager.dispose();
		inputManager.dispose();
		super.dispose();
	}
}
