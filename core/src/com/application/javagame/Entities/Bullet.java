package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Bullet extends Entity {

    Vector3 direction;
    Vector3 tmpVec;

    public Bullet(Vector3 position, Vector3 direction, float speed) {
        super(position);
        this.direction = direction.nor().cpy().scl(speed);


        AssetManager assetManager = Assets.GetManager();

        assetManager.finishLoadingAsset("player.glb");
        scene = new Scene(assetManager.get("player.glb", SceneAsset.class).scene);

        tmpVec = new Vector3();
    }

    @Override
    public void update(GameState state) {
        tmpVec.set(direction).scl(state.delta);
        position.add(tmpVec);
        scene.modelInstance.transform.setTranslation(position);
    }
}
