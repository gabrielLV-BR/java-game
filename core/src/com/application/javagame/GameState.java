package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Entities.Entity;
import com.application.javagame.Entities.Player;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Managers.ModelManager;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Disposable;

public class GameState implements Disposable {

    private InputManager inputManager;
    private MyGame game;
    private ModelBatch batch;

    public Player player;
    public ArrayList<Entity> entities;
    public float delta;

    public GameState(MyGame g) {
        ModelManager.Initialize();
        inputManager = new InputManager();
        game = g;
        delta = 0;
        entities = new ArrayList<>();
        batch = new ModelBatch();
    }

    public MyGame getGame() {
        return game;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public ModelBatch getBatch() {
        return batch;
    }

    @Override public void dispose() {
        game = null;
        inputManager.dispose();
        batch.dispose();
        player.dispose();

        for (Entity e: entities) {
            e.dispose();
        }
    }
}