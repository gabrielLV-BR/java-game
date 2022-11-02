package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Bullet extends GameObject {

    Vector3 direction;
    Vector3 tmpVec;

    public Bullet(Vector3 position, Vector3 direction, float speed) {
        super(Assets.<SceneAsset>Get("bullet.gltf").scene, position);
        this.direction = direction.nor().cpy().scl(speed);
        tmpVec = new Vector3();
    }

    @Override
    public void update(GameState state) {
        tmpVec.set(direction).scl(state.delta);
        modelInstance.transform.trn(tmpVec);
    }
}
