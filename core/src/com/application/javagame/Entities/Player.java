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

    float speed;
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
        rotateCamera(state);

        Vector3 input = state.getInputManager().getMovement();
        tmpVector.setZero()
            .add(camera.direction.cpy().scl(-input.z * state.delta * speed))
            .add(camera.direction.cpy().crs(Vector3.Y).scl(input.x * state.delta * speed));

        camera.position.add(tmpVector);
        state.getInputManager().update();
    }

    /*
        Código base pego daqui -> https://stackoverflow.com/a/34058580
        Foi alterado para que se encaixasse com a estruturação atual
    */
    void rotateCamera(GameState state) {
        Vector2 mouseDelta = state.getInputManager().getMouseDelta();

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

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        camera.update();
        batch.render(instance, environment);
    }

    @Override public void dispose() {
        model.dispose();
    }
}
