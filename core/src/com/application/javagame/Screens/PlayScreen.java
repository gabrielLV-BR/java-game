package com.application.javagame.Screens;

import com.application.javagame.Entities.Bullet;
import com.application.javagame.Entities.Entity;
import com.application.javagame.Entities.Player;
import com.application.javagame.GameState;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class PlayScreen implements Screen {

    private SceneManager sceneManager;
    private GameState state;

    public PlayScreen(GameState state) {
        this.state = state;
        this.state.player = new Player(state);

        sceneManager = this.state.getSceneManager();
        sceneManager.setCamera(this.state.player.getCamera());

        sceneManager.addScene(this.state.player.getScene(), true);
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

        sceneManager.camera.update();
        sceneManager.update(delta);
        sceneManager.render();
    }

    @Override public void show() {

    }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }
    @Override public void dispose() {}
}