package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {

    Model model;
    ModelInstance instance;
    PerspectiveCamera camera;

    public Player() {
        super(Vector3.Zero);

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near =0.1f;
        camera.far = 300f;

        ObjLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal("player.obj"));
        instance = new ModelInstance(model,0,0,0);
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public void update(GameState state) {
    }

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        camera.update();
        batch.render(instance, environment);
    }

    @Override public void dispose() {
        model.dispose();
    }
}
