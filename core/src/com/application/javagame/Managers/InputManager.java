package com.application.javagame.Managers;

import com.application.javagame.Data.MouseState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

//TODO: Guardar estados das teclas importantes
public class InputManager implements Disposable, InputProcessor {

    private final MouseState mouseState;
    private final Vector3 movement;
    boolean usingController = false;

    // Auxiliares
    private final Vector2 tmpVector;
    private final Vector2 prevMousePosition;
    private boolean mouseMoved = false;

    private static InputManager inputManager;

    private InputManager() {
        mouseState = new MouseState();
        movement = new Vector3();
        usingController = false;

        tmpVector = new Vector2();
        prevMousePosition = new Vector2(0, 0);
    }

    public static void Initialize() {
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(inputManager);
    }

    public static InputManager getInputManager() {
        return inputManager;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    // Getters

    public MouseState getMouseState() {
        if(!mouseMoved) {
            mouseState.delta.setZero();
        }
        return mouseState;
    }

    public boolean isUsingController() {
        return usingController;
    }

    //TODO: Remover isso daqui e colocar dentro do player
    public Vector3 getMovement() {
        return movement.nor();
    }
    // Teclado/Mouse

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            Gdx.input.setCursorCatched(false);
        }
        switch (keycode) {
            case Input.Keys.W: movement.z = -1; break;
            case Input.Keys.A: movement.x = -1; break;
            case Input.Keys.S: movement.z =  1; break;
            case Input.Keys.D: movement.x =  1; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.S:
                movement.z = 0; break;
            case Input.Keys.A:
            case Input.Keys.D:
                movement.x = 0; break;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        dispatchMouseMoved(prevMousePosition, tmpVector.set(screenX, screenY));
        mouseState.delta.set(screenX, screenY).sub(prevMousePosition);
        prevMousePosition.set(screenX, screenY);
        mouseMoved = true;
        return true;
    }

    public void update() {
        this.mouseMoved = false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        dispatchMouseClicked(prevMousePosition, pointer, button);
        mouseState.position.set(screenX, screenY);
        mouseState.button = button;
        Gdx.input.setCursorCatched(true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        dispatchMouseReleased(tmpVector.set(screenX, screenY), pointer, button);
        mouseState.position.set(screenX, screenY);
        mouseState.button = -1;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        dispatchMouseDragged(prevMousePosition, tmpVector.set(screenX, screenY));
        mouseMoved(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
