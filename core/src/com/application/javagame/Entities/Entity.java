package com.application.javagame.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Entity {
    public Vector3 position;
    Sprite sprite;

    public void update(float delta) {}

    public void draw(SpriteBatch spriteBatch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(spriteBatch);
    }
}
