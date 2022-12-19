package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Globals.Actions;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Weapons.Handgun;
import com.application.javagame.Objects.Weapons.Shotgun;
import com.application.javagame.Objects.Weapons.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.limiters.FullLimiter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Player extends GameObject {

    PerspectiveCamera camera;
    btRigidBody body;

    Vector3 acceleration;

    Weapon weapon;

    float speed; // velocidade
    float mouseSensitivity; // sensitividade do mouse
    float yaw; // rotação horizontal da câmera

    final float MAX_JETPACK_FUEL;
    float jetpackFuel;

    public Player(Vector3 position) {
        super();
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 100000f;

        acceleration = new Vector3(0, 0, 0);

        speed = 50f;
        mouseSensitivity = 0.7f;
        yaw = 0f;

        float mass = 90f;
        float radius = 3f;

        btCollisionShape shape = new btCylinderShape(new Vector3(radius, radius * 2, radius));

        tmpVector.set(0, 0, 0);
        shape.calculateLocalInertia(mass, tmpVector);

        body = new btRigidBody(mass, null, shape, tmpVector);
        body.setAngularFactor(0);
        body.translate(position);

        weapon = new Handgun();

        MAX_JETPACK_FUEL = 0.6f;
        jetpackFuel = MAX_JETPACK_FUEL;
    }

    @Override
    public void register(GameState state) {
        // registration
        state.sceneManager.setCamera(getCamera());

        state.setPlayer(this);
        state.addGameObject(this);
        state.physicsWorld.addBody(getBody());

        state.addSprite(weapon.getSprite());
    }

    public btRigidBody getBody() {
        return body;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
    
    public Vector3 getDirection() {
        return tmpVector.set(camera.direction);
    }

    @Override
    public void update(GameState state) {
        rotateCamera();

        move(state.delta);
        
        if(weapon.IS_SINGLE_ACTION) {
            if(Gdx.input.isButtonJustPressed(Buttons.LEFT)) fire(state);
        } else {
            if(Gdx.input.isButtonPressed(Buttons.LEFT)) fire(state);
        }
        weapon.update(state);
        camera.position.set(body.getCenterOfMassPosition());
    }

    private void move(float delta) {
        InputManager inMan = InputManager.GetInputManager();

        Vector3 input = tmpVector.set(
                inMan.keyStrength(Actions.Player.RIGHT) - inMan.keyStrength(Actions.Player.LEFT),
                0,
                inMan.keyStrength(Actions.Player.BACKWARD) - inMan.keyStrength(Actions.Player.FORWARD));

        Vector3 movement = Vector3.Zero.setZero()
                .add(camera.direction.cpy().scl(-input.z * delta * speed))
                .add(camera.direction.cpy().crs(Vector3.Y).scl(input.x * delta * speed))
                .scl(170);
        movement.y = 0;

        if (!movement.equals(tmpVector.setZero())) {
            body.activate(true);
            body.setLinearVelocity(movement.set(movement.x, body.getLinearVelocity().y, movement.z));
            // body.applyCentralImpulse(movement);
        } else {
            body.setLinearVelocity(tmpVector.set(0, body.getLinearVelocity().y, 0));
        }

        if(Gdx.input.isKeyPressed(Keys.SPACE) && jetpackFuel > 0) {
            // System.out.println("JETPACK");
            body.applyCentralImpulse(tmpVector.set(0, 900, 0));
            
            jetpackFuel -= delta;
        } else {
            jetpackFuel += delta;
            if(jetpackFuel > MAX_JETPACK_FUEL) jetpackFuel = MAX_JETPACK_FUEL;
        }

    }

    /*
     * Código base pego daqui -> https://stackoverflow.com/a/34058580
     * Foi alterado para que se encaixasse com a estruturação atual
     */
    void rotateCamera() {
        Vector2 mouseDelta = InputManager.GetInputManager().getMouseState().delta;

        float x = mouseDelta.x;
        if (x != 0) {
            camera.rotate(Vector3.Y, -x * mouseSensitivity);
            yaw += -x * mouseSensitivity;
        }

        float y = -(float) Math.sin(mouseDelta.y / 180f);
        if (y != 0 && Math.abs(camera.direction.y + y * (mouseSensitivity * 5.0f)) < Math.PI) {
            camera.direction.y += y * (mouseSensitivity * 5.0f);
        }
    }

    void fire(GameState state) {
        Ray ray = new Ray(camera.position, camera.direction);
        weapon.fire(ray, state);
    }

    // Getters
    public final Vector3 getPosition() {
        return tmpVector.set(body.getCenterOfMassPosition());
    }
}
