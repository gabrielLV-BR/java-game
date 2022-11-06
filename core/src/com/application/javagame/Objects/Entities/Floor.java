package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class Floor extends GameObject {

    btCollisionShape shape;
    btCollisionObject obj;
    btRigidBody body;

    boolean usingShape = false;

    public Floor(Vector3 p){
        super(Assets.<SceneAsset>Get("bullet.gltf").scene, p);

        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(
                100, 1, 100,
                new Material(ColorAttribute.createDiffuse(1, 0, 0, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        modelInstance = new ModelInstance(model, p);

        shape = new btBoxShape(new Vector3(100, 1, 100).scl(0.5f));

        float mass = 0f;
//        Vector3 inertia = new Vector3();
//        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, Vector3.Zero);
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
