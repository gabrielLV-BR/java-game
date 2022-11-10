package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.collision.*;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Crawler extends GameObject {

    btRigidBody body;

    public Crawler(Vector3 p) {
        super(Assets.<SceneAsset>Get("demon.gltf").scene, p);

        int collisionShapeIndex = getCollisionNodeShape();
        btCollisionShape shape;

        if(collisionShapeIndex == -1) {
            BoundingBox bb = new BoundingBox();
            modelInstance.calculateBoundingBox(bb);
            shape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));
        } else {
            shape = Bullet.obtainStaticNodeShape(
                modelInstance.nodes.get(collisionShapeIndex),
                false
            );
            shape.setLocalScaling(tmpVector.set(1f, 1f, 1f));
            modelInstance.nodes.removeIndex(collisionShapeIndex);
        }

        float mass = 10f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.translate(p);

        animations.playAll(true);
    }

    @Override
    public void update(GameState state) {
        body.getWorldTransform(modelInstance.transform);
        animations.update(state.delta);
    }

    public btRigidBody getBody() {
        return body;
    }
}
