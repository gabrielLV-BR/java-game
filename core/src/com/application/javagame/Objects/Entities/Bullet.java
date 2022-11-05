package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Bullet extends GameObject {

    Vector3 direction;
    Vector3 tmpVec;

    btRigidBody body;


    public Bullet(Vector3 position, Vector3 direction, float speed) {
        super(Assets.<SceneAsset>Get("bullet.gltf").scene, position);
        this.direction = direction.nor().cpy().scl(speed);
        tmpVec = new Vector3();

        btCollisionShape shape = com.badlogic.gdx.physics.bullet.Bullet.obtainStaticNodeShape(modelInstance.nodes);

        float mass = 1f;
        Vector3 inertia = direction.scl(speed);
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.translate(position);
    }

    public btRigidBody getBody() {
        return body;
    }

    @Override
    public void update(GameState state) {
//        tmpVec.set(direction).scl(state.delta);
//        modelInstance.transform.trn(tmpVec);
        modelInstance.transform.set(body.getWorldTransform());
    }
}
