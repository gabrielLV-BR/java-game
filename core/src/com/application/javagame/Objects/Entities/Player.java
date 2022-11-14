package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Globals.Actions;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Entities.Enemies.Crawler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Player extends GameObject {

    PerspectiveCamera camera;
    btRigidBody body;

    float speed; // velocidade
    float mouseSensitivity; // sensitividade do mouse
    float yaw; // rotação horizontal da câmera

    boolean fired = false;

    public Player(Vector3 position) {
        super();
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 300f;

        speed = 50f;
        mouseSensitivity = 0.7f;
        yaw = 0f;

        float mass = 10f;
        float radius = 5f;
        btCollisionShape shape = new btBoxShape(new Vector3(radius, radius, radius));
        tmpVector.set(0, 0, 0);
        shape.calculateLocalInertia(mass, tmpVector);

        body = new btRigidBody(mass, null, shape, tmpVector);
        body.setFriction(1.0f);
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

        camera.position.set(body.getCenterOfMassPosition());
    }

    private void move(float delta) {
        InputManager inMan = InputManager.getInputManager();

        Vector3 input = tmpVector.set(
            inMan.keyStrength(Actions.Player.RIGHT) - inMan.keyStrength(Actions.Player.LEFT),
            0,
            inMan.keyStrength(Actions.Player.BACKWARD) - inMan.keyStrength(Actions.Player.FORWARD)
        );

        Vector3 movement = Vector3.Zero.setZero()
            .add(camera.direction.cpy().scl(-input.z * delta * speed))
            .add(camera.direction.cpy().crs(Vector3.Y).scl(input.x * delta * speed))
            .scl(100);
        movement.y = 0;

        if(!movement.equals(tmpVector.setZero())) {
            body.setLinearVelocity(movement.set(movement.x, body.getLinearVelocity().y, movement.z));
        } else {
            body.setAngularVelocity(tmpVector);
            body.setLinearVelocity(tmpVector.set(0, body.getLinearVelocity().y, 0));
        }
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
        RayResultCallback resultCallback = state.physicsWorld.rayCast(new Ray(camera.position, camera.direction));
        
        System.out.println("Acertou: " + resultCallback.getCollisionObject());

        if(resultCallback.hasHit()) {
            if(resultCallback.getCollisionObject().userData instanceof Crawler) {
                Crawler enemy = (Crawler)resultCallback.getCollisionObject().userData;
                System.out.println("Acertou o inimigo!");
                enemy.wave();

                state.removeGameObject(enemy);
                state.physicsWorld.removeBody(enemy.getBody());
            }
        }

        // fired = true;
        // Ball ball = new Ball(camera.position.cpy(), camera.direction.cpy().scl(30));
        // state.addGameObject(ball);
        // state.physicsWorld.addBody(ball.getBody());
    }
}
