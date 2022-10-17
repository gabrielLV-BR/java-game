package com.application.javagame;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class MyGame extends Game {

	Screen screen;
	ModelBatch batch;

	AssetManager assetManager;
	InputManager inputManager;

	@Override
	public void create() {
		assetManager = Assets.getManager();
		assetManager.load("snake.jpg", Texture.class);

		inputManager = InputManager.getManager();

		batch = new ModelBatch();

		screen = new PlayScreen(this);
		setScreen(screen);
	}

	public ModelBatch getBatch() {
		return batch;
	}

	@Override public void render() { super.render(); }

	@Override public void dispose() {
		assetManager.dispose();
		inputManager.dispose();
		super.dispose();
	}
}
