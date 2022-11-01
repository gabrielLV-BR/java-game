package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Entity implements Disposable {
    public Vector3 position;
    protected Scene scene;

    Entity(Vector3 p) { position = p; }

    public Scene getScene() {
        return scene;
    }

    public abstract void update(GameState state);
    public void dispose() {}
}
