package com.application.javagame.Screens;

import com.application.javagame.Entities.Player;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.MyGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayScreen implements Screen {

    private InputManager input;
    private AssetManager manager;
    private MyGame game;

    private SpriteBatch spriteBatch;

    Player player;

    public PlayScreen(MyGame game) {
        this.game = game;

        input = new InputManager();
        manager = Assets.getManager();

        spriteBatch = game.getBatch();

        player = new Player();
    }

    private void update(float delta) {

    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();

        player.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override public void show() {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void hide() {

    }

    @Override
    public void dispose() {

    }
}
