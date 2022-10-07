package com.application.javagame.Managers;

import com.badlogic.gdx.utils.Disposable;

public class InputManager implements Disposable {

    private static InputManager manager;

    private InputManager() {
    }

    public static InputManager getManager() {
        if(manager == null) manager = new InputManager();
        return manager;
    }

    @Override
    public void dispose() {

    }
}
