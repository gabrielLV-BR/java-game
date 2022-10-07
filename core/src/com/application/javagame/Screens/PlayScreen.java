package com.application.javagame.Screens;

import com.application.javagame.Entities.Player;
import com.application.javagame.Globals.Constantes;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.MyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sun.org.apache.xpath.internal.operations.Or;

public class PlayScreen implements Screen {

    private InputManager input;
    private AssetManager manager;
    private MyGame game;

    private SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private FitViewport viewport;

    Player player;

    public PlayScreen(MyGame game) {
        this.game = game;

        input = InputManager.getManager();
        manager = Assets.getManager();

        spriteBatch = game.getBatch();

        player = new Player();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Constantes.LARGURA, Constantes.ALTURA, camera);
    }

    private void update(float delta) {
        player.update(delta);
        spriteBatch.setTransformMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);
        spriteBatch.begin();
            player.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        input.dispose();
    }
}
