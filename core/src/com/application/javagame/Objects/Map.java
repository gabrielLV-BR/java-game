package com.application.javagame.Objects;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Map extends GameObject {

    btCollisionObject groundObject;

    BoundingBox elevatorBoundingBox;

    btCollisionShape shape;
    btRigidBody body;

    boolean usingShape = false;
    public final ArrayList<Vector3> spawnPoints;
    Door door;

    public Map(String name, Vector3 p, float scale) {
        super(Assets.<SceneAsset>Get(name).scene, p);
        scene.modelInstance.transform.scl(scale);

        btCompoundShape compoundShape = new btCompoundShape();
        
        Node ground = getGroundObject();

        Node elevatorNode = searchForNode("ELEVATOR");
        if (elevatorNode != null) {
            elevatorBoundingBox = new BoundingBox();
            elevatorNode.calculateBoundingBox(elevatorBoundingBox);
        }

        ArrayList<Integer> collisionShapesIndexes = getCollisionNodesIndexes();
        spawnPoints = getSpawnPoints();

        for (int i : collisionShapesIndexes) {
            Node node = scene.modelInstance.nodes.get(i);

            btCollisionShape childShape = Bullet.obtainStaticNodeShape(
                node, false
            );

            childShape.setLocalScaling(node.scale.scl(scale));
            compoundShape.addChildShape(
                new Matrix4().trn(node.translation), 
                childShape
            );
        }

        scene.modelInstance.nodes.removeRange(collisionShapesIndexes.get(0), collisionShapesIndexes.get(collisionShapesIndexes.size() - 1));
        scene.modelInstance.nodes.removeValue(elevatorNode, true);

        shape = compoundShape;

        float mass = 0f;
        body = new btRigidBody(mass, null, shape, Vector3.Zero);
        body.translate(p);
        body.userData = this;

        if(ground != null) {
            BoundingBox groundBB = new BoundingBox();
            ground.calculateBoundingBox(groundBB);
            groundBB.getDimensions(tmpVector);
            btCollisionShape groundShape = new btBoxShape(
                    tmpVector.scl(0.5f));

            groundObject = new btCollisionObject();
            groundObject.setCollisionShape(groundShape);
        }
    }

    @Override
    public void register(GameState state) {
        // registration
        state.addGameObject(this);
        state.physicsWorld.addBody(getBody());
        state.setMap(this);

        if(groundObject != null)
            state.physicsWorld.setGround(groundObject);
        
        door = new Door(spawnPoints.get(1), 1);
        door.register(state);
        door.open(8);
    }

    public btRigidBody getBody() {
        return body;
    }

    public BoundingBox getElevatorBoundingBox() {
        return elevatorBoundingBox;
    }

    @Override
    public void update(GameState state) {
        // body.getWorldTransform(scene.modelInstance.transform);
    }

    public void ready() {
        door.open(1);
    }

    public void reset() {
        door.close(0);
    }
}