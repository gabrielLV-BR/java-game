package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Vesper extends Enemy {

    private static final String NAME = "Vesper";
    private static final float DAMAGE = 1;
    private static final float LIFE = 5;

    private final float speed;

    private Vector3 dir;

    public Vesper(Vector3 p) {
        super(NAME, Assets.<SceneAsset>Get("vesper.glb").scene, p, LIFE, DAMAGE, 2);

        speed = 20;

        BoundingBox bb = new BoundingBox();
        scene.modelInstance.calculateBoundingBox(bb);
        btCollisionShape shape = new btSphereShape(bb.getDimensions(tmpVector).scl(0.5f).x);

        float mass = 10f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.setAngularFactor(0);
        body.translate(p);
        body.userData = this;

        scene.animationController.action("ACTION", 10000, 1, null, 0);

        dir = new Vector3();
    }

    @Override
    public void damage(float damage) {
        life -= damage;

        body.setLinearVelocity(tmpVector.set(dir).scl(-5));
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

        scene.modelInstance.transform
            .setToLookAt(state.getPlayer().getPosition(), Vector3.Y)
            .setTranslation(body.getCenterOfMassPosition());
    }

    @Override
    public void register(GameState state) {
        state.addGameObject(this);
        state.physicsWorld.addBody(body);
    }
    
}
