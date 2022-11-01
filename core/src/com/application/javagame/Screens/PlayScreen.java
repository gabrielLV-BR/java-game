package com.application.javagame.Screens;

import com.application.javagame.Entities.Bullet;
import com.application.javagame.Entities.Entity;
import com.application.javagame.Entities.Player;
import com.application.javagame.GameState;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class PlayScreen implements Screen {

    private final ModelBatch modelBatch;
    private final Environment environment;

    private GameState state;

    public PlayScreen(GameState state) {
        this.state = state;
        this.state.player = new Player();

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
    }

    public void update() {
        state.player.update(state);
        for (Bullet b: state.bullets) {
            b.update(state);
        }
        for (Entity e: state.entities) {
            e.update(state);
        }
    }

    @Override
    public void render(float delta) {
        state.delta = delta;
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(state.player.getCamera());

        state.player.draw(modelBatch, environment);
        for (Bullet b: state.bullets) {
            b.draw(modelBatch, environment);
        }
        for (Entity e: state.entities) {
            e.draw(modelBatch, environment);
        }

        modelBatch.end();
    }

    @Override public void show() {

    }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void resize(int width, int height) {

    }
    @Override public void dispose() {}
}