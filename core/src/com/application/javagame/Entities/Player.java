package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Events.MouseListener;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Player extends Entity implements MouseListener {

    Model model;
    ModelInstance instance;
    PerspectiveCamera camera;

    GameState state;

    float speed;
    float mouseSensitivity;

    // Vetor temporário pra não ficarmos instanciando um a cada update
    // Ideia "roubada" da classe PerspectiveCamera
    Vector3 tmpVector;
    // Valor de rotação horizontal da câmera
    float yaw;

    public Player(GameState state) {
        super(Vector3.Zero);

        state.getInputManager().subscribeMouseListener(this);
        this.state = state;

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 3f);
        camera.lookAt(0f,0f,0f);
        camera.near = 0.1f;
        camera.far = 300f;

        AssetManager manager = Assets.GetManager();

        manager.finishLoadingAsset("player.glb");
        scene = new Scene(manager.get("player.glb", SceneAsset.class).scene);

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

        if(Gdx.input.isKeyPressed(Input.Buttons.LEFT))
            fire(state);
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

    void fire(GameState state) {
        System.out.println("Fogo!");
        state.bullets.add(new Bullet(position, camera.direction, 20));
    }

    @Override
    public void onMouseMoved(Vector2 from, Vector2 to) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onMouseDragged(Vector2 from, Vector2 to) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onMouseClicked(Vector2 where, int key, int modifiers) {
        System.out.println("Mouse clicado em " + where.toString());
        fire(state);
    }
    @Override
    public void onMouseReleased(Vector2 where, int key, int modifiers) {
        // TODO Auto-generated method stub
        
    }
}
