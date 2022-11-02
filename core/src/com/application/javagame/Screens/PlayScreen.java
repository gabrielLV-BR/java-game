package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.badlogic.gdx.*;

public class PlayScreen extends ScreenAdapter {

    private final GameState state;

    public PlayScreen(GameState s) {
        this.state = s;
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