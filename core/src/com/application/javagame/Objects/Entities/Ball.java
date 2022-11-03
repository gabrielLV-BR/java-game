package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class Ball extends GameObject {

    btCollisionShape shape;
    btCollisionObject obj;
    btRigidBody body;

    boolean usingShape = false;

    public Ball(Vector3 p){
        super(Assets.<SceneAsset>Get("bullet.gltf").scene, p);

        shape = Bullet.obtainStaticNodeShape(this.modelInstance.nodes);

        float mass = 1f;
        Vector3 inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        obj = new btCollisionObject();
        obj.setCollisionShape(shape);
    }

    public btCollisionObject getObj() {
        usingShape = true;
        return obj;
    }

    public btRigidBody getBody() {
        usingShape = true;
        return body;
    }

    public btCollisionShape getShape() {
        return shape;
    }

    @Override
    public void update(GameState state) {
        if (usingShape) {
            obj.getWorldTransform(modelInstance.transform);
        } else {
            body.getWorldTransform(modelInstance.transform);
        }
    }
}
