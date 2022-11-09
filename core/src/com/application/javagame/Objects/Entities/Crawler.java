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
    btCollisionShape shape;

    public Crawler(Vector3 p) {
        super(Assets.<SceneAsset>Get("demon.gltf").scene, p);
        shape = loadCollision();

        float mass = 0f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
    }

    @Override
    public void update(GameState state) {
        
    }

    public btRigidBody getBody() {
        return body;
    }
}
