package com.application.javagame;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Screens.DeathScreen;
import com.application.javagame.Screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.bullet.Bullet;

public class MyGame extends Game {


	@Override
	public void create() {
		// Inicializa bullet
		Bullet.init(false, true);;
		Assets.Initialize();
		InputManager.Initialize();
		// carrega a tela principal
		GameState state = new GameState(this);
		// PlayScreen tela = new PlayScreen(state);
		Screen tela = new DeathScreen(state);
		setScreen(tela);
	}

	@Override
	public void render() {
		super.render();
		InputManager.GetInputManager().update();
	}
}
