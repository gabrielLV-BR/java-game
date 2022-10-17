package com.application.javagame.Entities;

import com.application.javagame.Globals.Constantes;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Player extends Entity implements Disposable {

    // ModelBuilder modelBuilder;

    // Model model;
    // ModelInstance instance;

    public Player() {
        position = new Vector3(Constantes.ALTURA / 2.0f, Constantes.LARGURA / 2.0f, 0);

        AssetManager manager = Assets.getManager();
        manager.finishLoadingAsset("snake.jpg");
        Texture texture = manager.get("snake.jpg");

        sprite = new Sprite(texture);
        sprite.setScale(25);

        createModel();
    }

    private void createModel() {
        // modelBuilder = new ModelBuilder();
    }

    @Override
    public void update(float delta) {
        position.x += delta * 5000;
    }

    @Override
    public void dispose() {
        // model.dispose();
    }
}
