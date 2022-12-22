package com.application.javagame.Objects;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class GameObject implements Disposable {

    protected Scene scene;
    protected Vector3 tmpVector;

    private boolean isColliding;
    private btCollisionObject collidingObject;

    protected GameObject(SceneModel sceneModel, Vector3 p) {
        tmpVector = new Vector3();
        scene = new Scene(sceneModel);
        scene.modelInstance.transform.setTranslation(p);
        isColliding = false;
        // collisionObject = new btCollisionObject();
        // collisionObject.setCollisionShape(new btSphereShape(1));
    }

    protected GameObject() {
        tmpVector = new Vector3();
        scene = null;
        isColliding = false;
        // collisionObject = new btCollisionObject();
        // collisionObject.setCollisionShape(new btSphereShape(1));
    }

    public void collideWith(btCollisionObject obj) {
        isColliding = true;
        collidingObject = obj;
    }

    protected boolean isColliding() {
        return isColliding;
    }

    protected btCollisionObject getCollidedObject() {
        return collidingObject;
    }

    public Scene getScene() {
        return scene;
    }

    // public btCollisionObject getCollisionObject() {
    //     return collisionObject;
    // }

    protected ArrayList<Vector3> getSpawnPoints() {
        ArrayList<Vector3> vectors = new ArrayList<>(5);

        for (Node node : scene.modelInstance.nodes) {
            if (node.id.startsWith("SPAWNPOINT")) {
                vectors.add(node.translation);
            }
        }
        vectors.trimToSize();
        return vectors;
    }

    protected Node searchForNode(String name) {
        for(Node node : scene.modelInstance.nodes) {
            if(node.id.equals(name)) return node;
        }
        return null;
    }

    protected ArrayList<Integer> getCollisionNodesIndexes() {
        int index = 0;
        ArrayList<Integer> indexes = new ArrayList<>(5);

        for (Node node : scene.modelInstance.nodes) {
            System.out.println(node.id);
            if (node.id.startsWith("COLLISION")) {
                indexes.add(index);
            }
            index++;
        }
        indexes.trimToSize();
        return indexes;
    }

    protected Node getGroundObject() {
        for (Node node : scene.modelInstance.nodes) {
            if (node.id.endsWith("GROUND")) {
                return node;
            }
        }
        return null;
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
    public abstract void register(GameState state);
    @Override public void dispose() {}
}
