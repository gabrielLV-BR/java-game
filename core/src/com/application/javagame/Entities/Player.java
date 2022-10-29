package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {

    Model model;
    ModelInstance instance;
    PerspectiveCamera camera;

    float mouseSensitivity;

    // Vetor temporário pra não ficarmos instanciando um a cada update
    // Ideia "roubada" da classe PerspectiveCamera
    Vector3 tmpVector;
    // Valor de rotação horizontal da câmera
    float yaw;

    public Player() {
        super(Vector3.Zero);

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near =0.1f;
        camera.far = 300f;

        ObjLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal("player.obj"));
        instance = new ModelInstance(model,0,0,0);

        mouseSensitivity = 0.7f;
        yaw = 0f;

        tmpVector = new Vector3();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public void update(GameState state) {
        rotateCamera(state);
        Vector3 mov = state.getInputManager().getMovement();

        Vector3 forward = camera.direction.cpy();
        Vector3 right = tmpVector.set(camera.direction).crs(Vector3.Y);

        forward.x *= mov.z;
        forward.y *= mov.z;
        forward.z *= mov.z;

        right.x *= mov.x;
        right.y *= mov.x;
        right.z *= mov.x;

        mov.setZero().add(forward).add(right);

        camera.position.add(mov);
        state.getInputManager().update();
    }

    /*
        Código base pego daqui -> https://stackoverflow.com/a/34058580
        Foi alterado para que se encaixasse com a estruturação atual
    */
    void rotateCamera(GameState state) {
        Vector2 mouseDelta = state.getInputManager().getMouseDelta();

        float x = mouseDelta.x ;

        camera.rotate(Vector3.Y,-x * mouseSensitivity);
        yaw += -x * mouseSensitivity;

        float y = - (float) Math.sin(mouseDelta.y/180f);
        if (Math.abs(camera.direction.y + y * (mouseSensitivity*5.0f)) < Math.PI) {
            camera.direction.y +=  y * (mouseSensitivity*5.0f) ;
        }

        /*
        * int magX = Math.abs(mouseX - screenX);
    int magY = Math.abs(mouseY - screenY);

    if (mouseX > screenX) {
        cam.rotate(Vector3.Y, 1 * magX * rotSpeed);
        cam.update();
    }

    if (mouseX < screenX) {
        cam.rotate(Vector3.Y, -1 * magX * rotSpeed);
        cam.update();
    }

    if (mouseY < screenY) {
        if (cam.direction.y > -0.965)
            cam.rotate(cam.direction.cpy().crs(Vector3.Y), -1 * magY * rotSpeed);
        cam.update();
    }

    if (mouseY > screenY) {

        if (cam.direction.y < 0.965)
            cam.rotate(cam.direction.cpy().crs(Vector3.Y), 1 * magY * rotSpeed);
        cam.update();
    }

    mouseX = screenX;
    mouseY = screenY;

    return false;*/
    }

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        camera.update();
        batch.render(instance, environment);
    }

    @Override public void dispose() {
        model.dispose();
    }
}
