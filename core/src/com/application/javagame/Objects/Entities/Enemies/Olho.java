package com.application.javagame.Objects.Entities.Enemies;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Olho extends Enemy {

    private static final String NAME = "Vesper";
    private static final float DAMAGE = 1;
    private static final float LIFE = 5;

    private final btRigidBody body;
    private final float speed;

    public Olho(Vector3 p) {
        super(NAME, Assets.<SceneAsset>Get("vesper.glb").scene, p, LIFE, DAMAGE);

        speed = 2;

        ArrayList<Integer> collisionShapeIndexes = getCollisionNodesIndexes();
        btCollisionShape shape;

        if (collisionShapeIndexes.size() == 0) {
            BoundingBox bb = new BoundingBox();
            scene.modelInstance.calculateBoundingBox(bb);
            shape = new btSphereShape(bb.getDimensions(tmpVector).scl(0.5f).x);
        } else {
            shape = Bullet.obtainStaticNodeShape(
                    scene.modelInstance.nodes.get(collisionShapeIndexes.get(0)),
                    false);
            shape.setLocalScaling(tmpVector.set(1f, 1f, 1f));
            scene.modelInstance.nodes.removeIndex(collisionShapeIndexes.get(0));
        }

        float mass = 10f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.setAngularFactor(0);
        body.translate(p);
        body.userData = this;

        scene.animations.playAll(true);
    }

    @Override
    public void update(GameState state) {
        Vector3 dir = new Vector3(state.getPlayer().getPosition())
            .sub(body.getCenterOfMassPosition())
            .nor();

        tmpVector.set(dir);
    
        body.setLinearVelocity(tmpVector.scl(speed));
        scene.modelInstance.transform.setToLookAt(dir, Vector3.Y).setTranslation(body.getCenterOfMassPosition());
    }

    @Override
    public void register(GameState state) {
        state.addGameObject(this);
        state.physicsWorld.addBody(body);
    }
    
}
