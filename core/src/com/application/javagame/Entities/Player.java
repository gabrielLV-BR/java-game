package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Managers.ModelManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {

    InputManager inputManager;
    ModelInstance instance;
    PerspectiveCamera olho;

    public Player(GameState state) {
        super(Vector3.Zero);

        inputManager = new InputManager();

        ObjLoader loader = new ObjLoader();
        Model model = loader.loadModel(Gdx.files.internal("player.obj"));
        instance = new ModelInstance(model, Vector3.Zero);

        olho = state.getCamera();
        olho.position.set(0, 0, -10);
        olho.lookAt(0, 0, 0);
        olho.update();
    }

    @Override
    public void update(GameState state) {
        Vector2 input = inputManager.getMovimento();
        Vector3 movimento = new Vector3(
                input.x, 0, input.y
        );

        System.out.println("Posição: (" + olho.position.x + ", " + olho.position.z + ")");
        olho.position.add(movimento);
        olho.update();
    }

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        batch.render(instance, environment);        
    }

    @Override public void dispose() {
        inputManager.dispose();
    }
}
