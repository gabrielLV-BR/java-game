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
import net.mgsx.gltf.scene3d.scene.SceneModel;

public class Crawler extends GameObject {

    btRigidBody body;

    public Crawler(Vector3 p) {
        super(Assets.<SceneAsset>Get("demon.gltf").scene, p);

        int collisionShapeIndex = getCollisionNodeShape();
        btCollisionShape shape;

        collisionShapeIndex = -1;

        if(collisionShapeIndex == -1) {
            BoundingBox bb = new BoundingBox();
            modelInstance.calculateBoundingBox(bb);
            shape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));
        } else {
            shape = Bullet.obtainStaticNodeShape(
                modelInstance.nodes.get(collisionShapeIndex),
                false
            );
            shape.setLocalScaling(tmpVector.set(0.5f, 0.5f, 0.5f));
            modelInstance.nodes.removeIndex(collisionShapeIndex);
        }

        float mass = 10f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.translate(p);
    }

    @Override
    public void update(GameState state) {
        body.getWorldTransform(modelInstance.transform);
    }

    public btRigidBody getBody() {
        return body;
    }
}
