package com.application.javagame.Data;

import com.badlogic.gdx.math.Vector2;

public class MouseState {
    public Vector2 position;
    public Vector2 delta;
    public int button;

    public MouseState() {
        position = new Vector2();
        delta = new Vector2();
        button = -1;
    }
}
