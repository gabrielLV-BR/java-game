package com.application.javagame.Entities;

import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {
    public Player() {
        position = Vector3.Zero;

        AssetManager manager = Assets.getManager();
        manager.finishLoadingAsset("snake.jpg");
        Texture texture = manager.get("snake.jpg");

        sprite = new Sprite(texture);
    }
}
