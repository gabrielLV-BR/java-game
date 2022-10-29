package com.application.javagame;

import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;

public class MyGame extends Game {

	GameState state;
	PlayScreen currentScreen;

	@Override
	public void create() {
		state = new GameState(this);
		currentScreen = new PlayScreen(state);
		setScreen(currentScreen);
	}
}
