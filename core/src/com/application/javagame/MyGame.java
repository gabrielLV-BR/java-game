package com.application.javagame;

import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;

public class MyGame extends Game {

	PlayScreen screen;

	GameState state;

	@Override
	public void create() {
		state = new GameState(this);
		screen = new PlayScreen(state);
		setScreen(screen);
	}

	@Override public void render() { super.render(); }

	@Override public void dispose() {
		state.dispose();
		super.dispose();
	}
}
