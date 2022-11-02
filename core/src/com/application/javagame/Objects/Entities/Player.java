package com.application.javagame.Objects.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Globals.Actions;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Player extends GameObject {

    PerspectiveCamera camera;

    float speed; // velocidade
    float mouseSensitivity; // sensitividade do mouse
    float yaw; // rotação horizontal da câmera

    Vector3 tmpVector;

    public Player() {
        super (Assets.<SceneAsset>Get("player.glb").scene, Vector3.Zero);

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 300f;

        speed = 50f;
        mouseSensitivity = 0.7f;
        yaw = 0f;

        tmpVector = new Vector3();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public void update(GameState state) {
        rotateCamera();

        move(state.delta);

        if(InputManager.getInputManager().getMouseState().button == Input.Buttons.LEFT)
            fire(state);

        System.out.println("Camera position: (" + camera.position.x + ", " + camera.position.y + ")");
    }

    private void move(float delta) {
        InputManager inMan = InputManager.getInputManager();

        Vector3 input = tmpVector.set(
            inMan.keyStrength(Actions.Player.RIGHT) - inMan.keyStrength(Actions.Player.LEFT),
            0,
            inMan.keyStrength(Actions.Player.BACKWARD) - inMan.keyStrength(Actions.Player.FORWARD)
        );

        camera.position
            .add(camera.direction.cpy().scl(-input.z * delta * speed))
            .add(camera.direction.cpy().crs(Vector3.Y).scl(input.x * delta * speed));
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
        System.out.println("Fogo!");
        Bullet bullet = new Bullet(camera.position.cpy(), camera.direction.cpy(), 20);
        state.addGameObject(bullet);
    }
}
