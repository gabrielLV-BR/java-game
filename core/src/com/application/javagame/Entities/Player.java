package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.ModelManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {

    ModelInstance instance;
    PerspectiveCamera olho;

    public Player(GameState state) {
        super(Vector3.Zero);
        instance = ModelManager.GetInstance("player.obj", Vector3.Zero);

        olho = state.getCamera();
        olho.position.set(0, 0, 10);
        olho.lookAt(0, 0, 0);
    }

    @Override
    public void update(GameState state) {
    }


    @Override
    public void draw(ModelBatch batch, Environment environment) {
        batch.render(instance, environment);        
    }

    @Override public void dispose() {}
}
