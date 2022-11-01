package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Entities.Bullet;
import com.application.javagame.Entities.Entity;
import com.application.javagame.Entities.Player;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class GameState implements Disposable {

    private InputManager inputManager;
    private MyGame game;
    private ModelBatch batch;

    public Player player;
    public ArrayList<Entity> entities;
    public ArrayList<Bullet> bullets;
    public float delta;

    public GameState(MyGame g) {
        inputManager = new InputManager();
        game = g;
        delta = 0;
        entities = new ArrayList<>();
        bullets = new ArrayList<>();
        batch = new ModelBatch();

        preloadAssets();
    }

    private void preloadAssets() {
        AssetManager manager = Assets.GetManager(); 
		manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
        manager.load("player.glb", SceneAsset.class);
        manager.load("bullet.glb", SceneAsset.class);
    }

    public MyGame getGame() {
        return game;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public ModelBatch getBatch() {
        return batch;
    }

    @Override public void dispose() {
        game = null;
        inputManager.dispose();
        batch.dispose();
        player.dispose();

        for (Entity e: entities) {
            e.dispose();
        }
    }
}
