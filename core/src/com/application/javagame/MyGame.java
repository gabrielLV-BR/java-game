package com.application.javagame;

import com.application.javagame.Managers.InputManager;
import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class MyGame extends Game {

	PlayScreen screen;
	ModelBatch batch;

	AssetManager assetManager;
	InputManager inputManager;

	@Override
	public void create() {
		System.out.println("Created");

		inputManager = InputManager.getManager();

		batch = new ModelBatch();

		System.out.println("Before Screen");
		screen = new PlayScreen(this);
		System.out.println("Set screen");
		setScreen(screen);
		System.out.println("PENIS");
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
