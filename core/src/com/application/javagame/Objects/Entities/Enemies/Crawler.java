package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Utils.Utils3D;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.collision.*;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Crawler extends Enemy {

    private static final String NAME = "Crawler";
    private static final float DAMAGE = 2;
    private static final float LIFE = 10;

    private final float speed;
    btRigidBody body;

    public Crawler(Vector3 p) {
        super(Assets.<SceneAsset>Get("crawler.glb").scene, p, LIFE, DAMAGE, NAME);

        speed = 2000;

        int collisionShapeIndex = getCollisionNodeShape();
        btCollisionShape shape;

        if (collisionShapeIndex == -1) {
            BoundingBox bb = new BoundingBox();
            scene.modelInstance.calculateBoundingBox(bb);
            shape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));
        } else {
            shape = Bullet.obtainStaticNodeShape(
                    scene.modelInstance.nodes.get(collisionShapeIndex),
                    false);
            shape.setLocalScaling(tmpVector.set(1f, 1f, 1f));
            scene.modelInstance.nodes.removeIndex(collisionShapeIndex);
        }

        float mass = 10f;
        Vector3 inertia = Vector3.Zero;
        shape.calculateLocalInertia(mass, inertia);

        body = new btRigidBody(mass, null, shape, inertia);
        body.translate(p);
        body.userData = this;

        scene.animations.playAll(true);
    }

    @Override
    public void update(GameState state) {

        Vector3 playerPos = state.getPlayer().getPosition();
        Vector3 myPos = (body.getCenterOfMassPosition()).cpy();

        Vector3 toPlayer = tmpVector.set(playerPos).sub(myPos).nor().scl(1);

        /*
         * Utils3D.printVector3("Player Position", playerPos);
         * Utils3D.printVector3("My Position", myPos);
         * Utils3D.printVector3("toPlayer", toPlayer);
         * System.out.println("---------------");
         */
        scene.modelInstance.transform.setToLookAt(toPlayer, state.getPlayer().getCamera().up);
        body.setLinearVelocity(new Vector3(toPlayer.x, body.getLinearVelocity().y, toPlayer.z));

        body.getWorldTransform(scene.modelInstance.transform);
        scene.animations.update(state.delta);
    }

    public btRigidBody getBody() {
        return body;
    }

    public void wave() {
        System.out.println("Acenando a alguém");
    }
}
