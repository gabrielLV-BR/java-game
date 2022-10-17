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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class PlayScreen implements Screen {

    private InputManager input;
    private AssetManager manager;
    private MyGame game;

    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    private ArrayList<Model> models;
    private ArrayList<ModelInstance> instances;

    private Environment environment;
    private PerspectiveCamera camera;
    private FitViewport viewport;

    Player player;

    public PlayScreen(MyGame game) {
        models = new ArrayList<>();
        instances = new ArrayList<>();

        this.game = game;

        input = InputManager.getManager();
        manager = Assets.getManager();

        modelBatch = game.getBatch();

        player = new Player();

        camera = new PerspectiveCamera(
            90.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        camera.position.z = -5;
        camera.lookAt(0, 0, 0);
        viewport = new FitViewport(Constantes.LARGURA, Constantes.ALTURA, camera);

        environment = new Environment();
        environment.set(ColorAttribute.createAmbient(1, 1, 1, 1));

        loadLevel();
    }

    private void loadLevel() {
        modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(
                2, 2, 2, new Material(
                        ColorAttribute.createDiffuse(1, 0, 0, 1)
                ),
            VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal
        );

        ModelInstance instance = new ModelInstance(model, 0, 0, 0);

        models.add(model);
        instances.add(instance);
    }

    private void update(float delta) {
        player.update(delta);
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        modelBatch.begin(camera);
//            player.draw(modelBatch, environment);

            for(ModelInstance instance : instances) {
                modelBatch.render(instance, environment);
            }

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
        input.dispose();
        player.dispose();

        for (Model model: models) {
            model.dispose();
        }
    }
}
