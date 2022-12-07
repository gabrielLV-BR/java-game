package com.application.javagame.Objects;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;
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

    protected ArrayList<Integer> getCollisionNodesIndexes() {
        int index = 0;
        ArrayList<Integer> indexes = new ArrayList<>(5);

        for (Node node : scene.modelInstance.nodes) {
            if (node.id.startsWith("COLLISION")) {
                indexes.add(index);
            }
            index++;
        }
        indexes.trimToSize();
        return indexes;
    }
    protected btCollisionShape loadCollision() {
        btCollisionShape out = new btBoxShape(Vector3.Zero);

        ArrayList<Integer> indexes = getCollisionNodesIndexes();
        int index = indexes.get(0);

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
