package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Wall extends GameObject {

    btRigidBody body;

    boolean usingShape = false;

    float time = 0;

    public Wall(Vector3 p, float scale) {
        super(Assets.<SceneAsset>Get("door.glb").scene, p);
        scene.modelInstance.transform.scl(scale);

        BoundingBox bb = new BoundingBox();
        scene.modelInstance.calculateBoundingBox(bb);
        btBoxShape boxShape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));

        float mass = 0f;
        body = new btRigidBody(mass, null, boxShape, Vector3.Zero);
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
        time += state.delta;

        if(time > 4) {
            body.setWorldTransform(new Matrix4().translate(body.getCenterOfMassPosition().add(new Vector3(0, 0.7f, 0))));
        } 
        if (time > 8) {
            state.removeGameObject(this);
            state.physicsWorld.removeBody(body);
        }
        body.getWorldTransform(scene.modelInstance.transform);
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