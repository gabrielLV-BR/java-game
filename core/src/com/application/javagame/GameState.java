package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Entities.Entity;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.ModelManager;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Disposable;

public class GameState implements Disposable {

    private PerspectiveCamera camera;
	private ModelBatch batch;

    private MyGame game;
    public float delta;
    public ArrayList<Entity> entities;

    public GameState(MyGame g) {
        ModelManager.Initialize();
        game = g;
        delta = 0;
        entities = new ArrayList<>();
        batch = new ModelBatch();

        camera = new PerspectiveCamera();

    }

    public MyGame getGame() {
        return game;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public ModelBatch getBatch() {
        return batch;
    }

    @Override public void dispose() {
        game = null;
        batch.dispose();
    }
}
