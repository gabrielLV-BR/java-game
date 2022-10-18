package com.application.javagame.Screens;

import com.application.javagame.Entities.Player;
import com.application.javagame.Globals.Constantes;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.MyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class PlayScreen implements Screen {

    private InputManager input;
    private AssetManager manager;
    private MyGame game;

    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    Model model;
    ModelInstance instance;

    private Environment environment;
    private PerspectiveCamera camera;
    private FitViewport viewport;

    public PlayScreen(MyGame game) {
        this.game = game;

//        input = InputManager.getManager();
//        manager = Assets.getManager();

        modelBatch = game.getBatch();

//        player = new Player();

        camera = new PerspectiveCamera(
            90.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        camera.near = 0.01f;
        camera.far = 1000;
        camera.position.set(0, 0, -10);
        camera.lookAt(0, 0, 0);
        camera.update(true);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        environment = new Environment();
        environment.set(ColorAttribute.createAmbient(1, 1, 1, 1));

        modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(
            3f, 3f, 3f,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal
        );
        instance = new ModelInstance(model);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void update(float delta) {
//        player.update(delta);
    }

    @Override
    public void render(float delta) {
//        this.update(delta);
        instance.transform.rotate(Vector3.X, (float) Math.sin(delta) / 2 + 1);
        instance.transform.rotate(Vector3.Y, delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        modelBatch.begin(camera);

        modelBatch.render(instance);

        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
//        input.dispose();
//        player.dispose();

//        for (Model model: models) {
//            model.dispose();
//        }
    }
}
