package com.application.javagame;

import com.application.javagame.Screens.PlayScreen;
import com.badlogic.gdx.Game;

public class MyGame extends Game {
	@Override
	public void create() {
		setScreen(new PlayScreen());
	}
}
