package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class GameObject implements Disposable {

    protected Scene scene;
    protected Vector3 tmpVector;

    protected GameObject(SceneModel sceneModel, Vector3 p) {
        tmpVector = new Vector3();
        scene = new Scene(sceneModel);
        scene.modelInstance.transform.setTranslation(p);
        // collisionObject = new btCollisionObject();
        // collisionObject.setCollisionShape(new btSphereShape(1));
    }

    protected GameObject() {
        tmpVector = new Vector3();
        scene = null;
        // collisionObject = new btCollisionObject();
        // collisionObject.setCollisionShape(new btSphereShape(1));
    }

    public Scene getScene() {
        return scene;
    }

    // public btCollisionObject getCollisionObject() {
    //     return collisionObject;
    // }

    protected int getCollisionNodeShape() {
        int index = 0;
        for (Node node : scene.modelInstance.nodes) {
            if (node.id.equals("COLLISION")) {
                return index;
            }
            index++;
        }
        return -1;
    }
    protected btCollisionShape loadCollision() {
        btCollisionShape out = new btBoxShape(Vector3.Zero);

        int index = getCollisionNodeShape();

        if(index != -1)
            scene.modelInstance.nodes.removeIndex(index);
        else {
            BoundingBox bb = new BoundingBox();
            scene.modelInstance.calculateBoundingBox(bb);
            out = new btBoxShape(bb.getDimensions(tmpVector));
        }

        return out;
    }

    public abstract void update(GameState state);
    @Override public void dispose() {}
}
