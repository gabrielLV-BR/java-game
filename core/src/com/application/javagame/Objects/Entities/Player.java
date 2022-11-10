package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Globals.Actions;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Entities.Crawler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Player extends GameObject {

    PerspectiveCamera camera;
    btRigidBody body;

    float speed; // velocidade
    float mouseSensitivity; // sensitividade do mouse
    float yaw; // rotação horizontal da câmera

    boolean fired = false;
    Vector3 tmpVector;

    public Player(Vector3 position) {
        super (Assets.<SceneAsset>Get("player.glb").scene, position);

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 300f;

        speed = 50f;
        mouseSensitivity = 0.7f;
        yaw = 0f;

        tmpVector = new Vector3();

        BoundingBox bb = new BoundingBox();
        modelInstance.calculateBoundingBox(bb);
        btCollisionShape shape = new btBoxShape(
                bb.getDimensions(tmpVector).scl(0.5f)
        );

        float mass = 10f;
        tmpVector.set(0, 0, 0);
        shape.calculateLocalInertia(mass, tmpVector);

        body = new btRigidBody(mass, null, shape, tmpVector);
        body.translate(position);
    }

    public btRigidBody getBody() {
        return body;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public void update(GameState state) {
        rotateCamera();

        move(state.delta);

        if(InputManager.getInputManager().getMouseState().button == Input.Buttons.LEFT) {
            if (!fired) fire(state);
        } else fired = false;

//        body.getWorldTransform(modelInstance.transform);
        camera.position.set(body.getCenterOfMassPosition());
    }

    private void move(float delta) {
        InputManager inMan = InputManager.getInputManager();

        Vector3 input = tmpVector.set(
            inMan.keyStrength(Actions.Player.RIGHT) - inMan.keyStrength(Actions.Player.LEFT),
            0,
            inMan.keyStrength(Actions.Player.BACKWARD) - inMan.keyStrength(Actions.Player.FORWARD)
        );

        Vector3 movement = Vector3.Zero
            .add(camera.direction.cpy().scl(-input.z * delta * speed))
            .add(camera.direction.cpy().crs(Vector3.Y).scl(input.x * delta * speed));

        body.setLinearVelocity(movement);
    }


    /*
        Código base pego daqui -> https://stackoverflow.com/a/34058580
        Foi alterado para que se encaixasse com a estruturação atual
    */
    void rotateCamera() {
        Vector2 mouseDelta = InputManager.getInputManager().getMouseState().delta;

        float x = mouseDelta.x ;
        if(x != 0) {
            camera.rotate(Vector3.Y,-x * mouseSensitivity);
            yaw += -x * mouseSensitivity;
        }

        float y = - (float) Math.sin(mouseDelta.y/180f);
        if (y != 0 && Math.abs(camera.direction.y + y * (mouseSensitivity*5.0f)) < Math.PI) {
            camera.direction.y += y * (mouseSensitivity*5.0f) ;
        }
    }

    void fire(GameState state) {
        btCollisionObject hit = state.physicsWorld.rayCast(new Ray(camera.position, camera.direction));
        
        if(hit != null) {
            if(hit.userData instanceof Crawler) {
                Crawler enemy = (Crawler) hit.userData;
                System.out.println("Acertou o inimigo!");

                state.removeGameObject(enemy);
                state.physicsWorld.removeBody(enemy.getBody());
            }
        }

        fired = true;
        Ball ball = new Ball(camera.position.cpy(), camera.direction.cpy().scl(30));
        state.addGameObject(ball);
        state.physicsWorld.addBody(ball.getBody());
    }
}
