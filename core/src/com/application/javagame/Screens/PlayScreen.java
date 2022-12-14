package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.Map;
import com.application.javagame.Objects.Wall;
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

        Map map = new Map("map2.glb", new Vector3(0, 0, 0), 1);
        map.register(s);

        new Wall(map.spawnPoints.get(1), 1).register(s);

        new Player(map.spawnPoints.get(0))
            .register(state);

        // Loading level
        new Crawler(new Vector3(10, 20, 0))
            .register(state);
        
        new Vesper(new Vector3(20, 10, 0))
            .register(state);

    
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