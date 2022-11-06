package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class GameObject extends Scene implements Disposable {

    protected Vector3 tmpVector;

    protected GameObject(SceneModel sceneModel, Vector3 p) {
        super(sceneModel);
        tmpVector = new Vector3();
        modelInstance.transform.setTranslation(p);
    }

    public abstract void update(GameState state);
    @Override public void dispose() {}
}
