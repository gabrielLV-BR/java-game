package com.application.javagame.Objects;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Map extends GameObject {

    btCollisionShape shape;
    btRigidBody body;

    boolean usingShape = false;

    public Map(String name, Vector3 p, float scale) {
        super(Assets.<SceneAsset>Get(name).scene, p);
        scene.modelInstance.transform.scl(scale);

        btCompoundShape compoundShape = new btCompoundShape();
        ArrayList<Integer> collisionShapesIndexes = getCollisionNodesIndexes();

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

        shape = compoundShape;

        float mass = 0f;
        body = new btRigidBody(mass, null, shape, Vector3.Zero);
        body.translate(p);
        body.userData = this;
    }

    @Override
    public void register(GameState state) {
        // registration
        state.addGameObject(this);
        state.physicsWorld.addBody(getBody());
    }

    public btRigidBody getBody() {
        return body;
    }

    @Override
    public void update(GameState state) {
        // body.getWorldTransform(scene.modelInstance.transform);
    }
}

/*
 * 
 * for(int collisionShapeIndex : collisionShapesIndexes) {
 * 
 * Node node = scene.modelInstance.nodes.get(collisionShapeIndex);
 * btCollisionShape childShape = Bullet.obtainStaticNodeShape(
 * node, false
 * );
 * 
 * System.out.print("Child shape #" + collisionShapeIndex + "is ");
 * System.out.println(childShape.isConvex() ? "convex (GOOD)" :
 * "concave (BAD)");
 * compoundShape.addChildShape(node.localTransform.scl(size), childShape);
 * scene.modelInstance.nodes.removeIndex(collisionShapeIndex);
 * }
 * 
 * shape = compoundShape;
 * 
 * System.out.println("Shape is compound with " +
 * compoundShape.getNumChildShapes()
 * + " children");
 * }
 * 
 */