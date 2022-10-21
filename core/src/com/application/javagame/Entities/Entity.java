package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public abstract class Entity implements Disposable {
    public Vector3 position;

    Entity(Vector3 p) { position = p; }

    public abstract void update(GameState state);
    public abstract void draw(ModelBatch batch, Environment environment);
}
