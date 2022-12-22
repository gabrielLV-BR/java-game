package com.application.javagame;

import com.application.javagame.Data.DBConnection;
import com.application.javagame.Data.GraphGenerator;
import com.application.javagame.Data.Score;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Jogo Java");
		config.setResizable(false);
		new Lwjgl3Application(new MyGame(), config);
	}
}
