package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Door extends GameObject {

    private btRigidBody body;
    private float time; 

    private final float FINAL_TIME;
    private boolean isOpening;

    private final Vector3 closedPosition, openedPosition;

    public Door(Vector3 p, float scale) {
        super(Assets.<SceneAsset>Get("door.glb").scene, p);
        scene.modelInstance.transform.scl(scale);

        BoundingBox bb = new BoundingBox();
        scene.modelInstance.calculateBoundingBox(bb);
        btBoxShape boxShape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));

        float mass = 0f;
        body = new btRigidBody(mass, null, boxShape, Vector3.Zero);
        body.translate(p);
        body.userData = this;

        float halfWidth = bb.getHeight() / 2;

        closedPosition = p.cpy();
        openedPosition = p.cpy().add(0, halfWidth * 2, 0);

        FINAL_TIME = 1;
        open(5);
    }

    public void open(float margin) {
        isOpening = true;
        time = -margin;
    }

    public void close(float margin) {
        isOpening = false;
        time = -margin;
    }

    @Override
    public void register(GameState state) {
        // registration
        state.addGameObject(this);
        state.physicsWorld.addBody(getBody());
    }

    public btRigidBody getBody() {
        return body;
    }

    @Override
    public void update(GameState state) {
        if(time >= FINAL_TIME) {
            return;
        }

        time += state.delta;

        if(time < 0) return;

        float norm_time = time / FINAL_TIME;
        float y = 0;

        if(!isOpening) {
            norm_time = 1 - norm_time;
        }          

        y = closedPosition.y + (openedPosition.y - closedPosition.y) * norm_time;

        body.setWorldTransform(
            new Matrix4().setToTranslation(
                body.getCenterOfMassPosition().x,
                y,
                body.getCenterOfMassPosition().z
            )
        );
        body.getWorldTransform(scene.modelInstance.transform);
    }
}
