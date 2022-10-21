package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Entities.Entity;
import com.application.javagame.Entities.Player;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class PlayScreen implements Screen {

    private ModelBatch modelBatch;
    private Environment environment;

    GameState state;

    public PlayScreen(GameState state) {
        AssetManager assetManager = Assets.GetManager();
        assetManager.load("player.obj", Model.class);

        this.state = state;

        state.entities.add(new Player(state));

        modelBatch = state.getBatch();

        environment = new Environment();
        environment.set(ColorAttribute.createAmbient(1, 1, 1, 1));

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void update() {
        for(Entity e : state.entities) {
            e.update(state);
        }
    }

    @Override
    public void render(float delta) {
        state.delta = delta;
        update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(state.getCamera());

        for (Entity entity : state.entities) {
            entity.draw(modelBatch, environment);
        }

        modelBatch.end();
    }

    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void resize(int width, int height) { }

    @Override
    public void dispose() {
        for (Entity entity : state.entities) {
            entity.dispose();
        }
    }
}
