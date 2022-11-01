package com.application.javagame.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class InputManager implements Disposable, InputProcessor {

    private final Vector3 movement;
    private final Vector2 mouseDelta;

    boolean usingController = false;
    boolean mouseMoved = false;

    private final Vector2 _prevMousePos;

    public InputManager() {
        movement = Vector3.Zero;
        mouseDelta = Vector2.Zero;
        usingController = false;

        _prevMousePos = new Vector2(0, 0);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    // Getters

    public Vector2 getMouseDelta() {
        if(mouseMoved) {
            this.mouseMoved = false;
            return mouseDelta;
        }
        System.out.println("Mouse has not moved");
        return new Vector2(0, 0);
    }

    public Vector3 getMovement() {
        return movement.nor();
    }

    public Vector2 getMousePosition() {
        return _prevMousePos;
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
        mouseDelta.set(screenX, screenY).sub(_prevMousePos);
        _prevMousePos.set(screenX, screenY);
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
        Gdx.input.setCursorCatched(true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
