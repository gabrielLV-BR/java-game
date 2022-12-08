package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Objects.Map;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.Entities.Enemies.Crawler;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;

public class PlayScreen extends ScreenAdapter {

    private final GameState state;

    public PlayScreen(GameState s) {

        this.state = s;

        new Player(new Vector3(0, 10, 0))
            .register(state);;

        // Loading level
        new Crawler(new Vector3(10, 2, 0))
            .register(state);;

        new Map("map.glb", new Vector3(0, 0, 0), 1)
            .register(state);;
    
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