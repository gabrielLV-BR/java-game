package com.application.javagame.Managers;

import com.application.javagame.Data.MouseState;
import com.application.javagame.Globals.Actions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

public class InputManager implements Disposable, InputProcessor {

    private final MouseState mouseState;
    boolean usingController = false;

    private final HashMap<Integer, Float> inputMap;

    // Auxiliares
    private final Vector2 prevMousePosition;
    private boolean mouseMoved = false;

    private static InputManager inputManager;

    private InputManager() {
        mouseState = new MouseState();
        inputMap = new HashMap<>();

        // Guardando as ações possíveis
        for(int button : Actions.Player.getButtons()) {
            inputMap.put(button, 0f);
        }
        //

        usingController = false;
        prevMousePosition = new Vector2(0, 0);
    }

    public static void Initialize() {
        inputManager = new InputManager();
    }

    public static InputManager GetInputManager() {
        return inputManager;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    // Getters

    public float keyStrength(int key) {
        return inputMap.get(key);
    }

    public MouseState getMouseState() {
        if(!mouseMoved) {
            mouseState.delta.setZero();
        }
        return mouseState;
    }

    public boolean isUsingController() {
        return usingController;
    }

    // Teclado/Mouse
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            Gdx.input.setCursorCatched(false);
        }

        if(inputMap.containsKey(keycode))
            inputMap.put(keycode, 1f);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(inputMap.containsKey(keycode))
            inputMap.put(keycode, 0f);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        dispatchMouseMoved(prevMousePosition, tmpVector.set(screenX, screenY));
        mouseState.delta.set(screenX, screenY).sub(prevMousePosition);
        prevMousePosition.set(screenX, screenY);
        mouseMoved = true;
        return false;
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
