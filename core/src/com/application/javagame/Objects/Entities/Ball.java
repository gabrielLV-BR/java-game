package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class Ball extends GameObject {

    btCollisionShape shape;
    btCollisionObject obj;
    btRigidBody body;

    boolean usingShape = false;
    Vector3 velocity;

    public Ball(Vector3 p, Vector3 dir){
        super(Assets.<SceneAsset>Get("player.glb").scene, p);

        float radius = 2f;

        shape = new btSphereShape(radius / 2f);

        float mass = 1f;
        Vector3 inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        obj = new btCollisionObject();
        obj.setCollisionShape(shape);

        body.translate(p);
        velocity = dir;
    }

    public btRigidBody getBody() {
        return body;
    }

    @Override
    public void update(GameState state) {
        if(velocity.len() > 0) {
            body.setLinearVelocity(velocity);
            velocity = Vector3.Zero;
        }
        body.getWorldTransform(scene.modelInstance.transform);
    }
}
