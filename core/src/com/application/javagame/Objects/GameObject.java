package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class GameObject extends Scene implements Disposable {

    protected Vector3 tmpVector;

    protected GameObject(SceneModel sceneModel, Vector3 p) {
        super(sceneModel);
        tmpVector = new Vector3();
        modelInstance.transform.setTranslation(p);
    }

    protected btCollisionShape loadCollision() {
        btCollisionShape shape = new btBoxShape(Vector3.Zero);

        int index = 0;
        boolean foundCollisionShape = false;
        for (Node node : modelInstance.nodes) {
            if (node.id.equals("collision")) {
                shape = Bullet.obtainStaticNodeShape(node, true);
                shape.setLocalScaling(tmpVector.set(0.5f, 0.5f, 0.5f));
                foundCollisionShape = true;
            } else index++;
        }

        if(foundCollisionShape)
            modelInstance.nodes.removeIndex(index);
        else {
            BoundingBox bb = new BoundingBox();
            modelInstance.calculateBoundingBox(bb);
            shape = new btBoxShape(bb.getDimensions(tmpVector));
        }

        return shape;
    }

    public abstract void update(GameState state);
    @Override public void dispose() {}
}
