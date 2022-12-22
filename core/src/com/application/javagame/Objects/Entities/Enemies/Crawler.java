package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.collision.*;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Crawler extends Enemy {

    private static final String NAME = "Crawler";
    private static final float DAMAGE = 2;
    private static final float LIFE = 10;

    private final float speed;

    public Crawler(Vector3 p) {
        super(NAME, Assets.<SceneAsset>Get("crawler.glb").scene, p, LIFE, DAMAGE, 10);

        speed = 2;

        BoundingBox bb = new BoundingBox();
        scene.modelInstance.calculateBoundingBox(bb);
        btCollisionShape shape = new btBoxShape(bb.getDimensions(tmpVector));

        float mass = 90f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.setAngularFactor(0);
        body.translate(p);
        body.userData = this;
    }

    @Override
    public void register(GameState state) {
        // register
        state.addGameObject(this);
        state.physicsWorld.addBody(this.getBody());
    }

    @Override
    public void update(GameState state) {
        if(life < 0) {
            die(state);
        }

        dir.set(state.getPlayer().getPosition())
            .sub(body.getCenterOfMassPosition())
            .nor();

        if(body.getLinearVelocity().len() < speed) {
            body.applyCentralImpulse(tmpVector.set(dir).scl(speed));
        }

        tmpVector.set(body.getLinearVelocity());
        tmpVector.y = 0;
        body.setLinearVelocity(tmpVector);
        // body.setLinearVelocity(tmpVector);
        // body.applyCentralImpulse(tmpVector);

        scene.modelInstance.transform
            .setToLookAt(state.getPlayer().getPosition(), Vector3.Y)
            .setTranslation(body.getCenterOfMassPosition());

        scene.animations.update(state.delta);
    }

    public btRigidBody getBody() {
        return body;
    }

    public void wave() {
        System.out.println("Acenando a alguém");
    }
}
