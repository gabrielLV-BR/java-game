package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.Map;
import com.application.javagame.Objects.Door;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.Entities.Enemies.Crawler;
import com.application.javagame.Objects.Entities.Enemies.Vesper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;

public class PlayScreen extends ScreenAdapter {

    private final GameState state;

    public PlayScreen(GameState s) {
        this.state = s;

        Gdx.input.setInputProcessor(InputManager.GetInputManager());

        new Map("map.glb", new Vector3(0, 0, 0), 1)
            .register(state);;

        new Player(state.getMap().spawnPoints.get(0))
            .register(state);

        s.newRound();
    }

    @Override
    public void render(float delta) {
        state.update(delta);
        state.render();
    }

    @Override public void resize(int width, int height) {
        state.resize(width, height);
    }
}