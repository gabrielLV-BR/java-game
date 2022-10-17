package com.application.javagame.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class InputManager implements Disposable, InputProcessor {

    private static InputManager manager;

    private Vector2 movement;
    boolean usandoControle = false;

    private InputManager() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void dispose() {
    }

    // Getters

    public Vector2 getMovimento() {
        return movement.nor();
    }

    public boolean estaUsandoControle() {
        return usandoControle;
    }

    // Singleton

    public static InputManager getManager() {
        if(manager == null) manager = new InputManager();
        return manager;
    }

    // Teclado/Mouse

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W: movement.y =  1; break;
            case Input.Keys.A: movement.x = -1; break;
            case Input.Keys.S: movement.y = -1; break;
            case Input.Keys.D: movement.x =  1; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.S:
                movement.y = 0; break;
            case Input.Keys.A:
            case Input.Keys.D:
                movement.x = 0; break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
