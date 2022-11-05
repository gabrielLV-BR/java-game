package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Managers.PhysicsWorld;
import com.application.javagame.Objects.Entities.Ball;
import com.application.javagame.Objects.Entities.Bullet;
import com.application.javagame.Objects.Entities.Floor;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;


public class GameState implements Disposable {

    private final SceneManager sceneManager;
    public final PhysicsWorld physicsWorld;

    public final ArrayList<GameObject> gameObjects;
    private final ArrayList<GameObject> gameObjectsToRemove;
    private final ArrayList<GameObject> gameObjectsToAdd;

    public float delta;

    public GameState() {
        delta = 0;
        physicsWorld = new PhysicsWorld();

        gameObjects = new ArrayList<>();
        gameObjectsToAdd = new ArrayList<>();
        gameObjectsToRemove = new ArrayList<>();

        preloadAssets();

        sceneManager = new SceneManager();
        sceneManager.setAmbientLight(0.3f);
        sceneManager.updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Player player = new Player();
        sceneManager.setCamera(player.getCamera());

        addGameObject(player);
        addGameObject(new Bullet(new Vector3(0, 0, 10), Vector3.Zero, 0));

        Ball ball = new Ball(new Vector3(0, 10, 0));
        physicsWorld.addBody(ball.getBody());
        addGameObject(ball);

        Ball ball2 = new Ball(new Vector3(0, 0, 0));
        physicsWorld.addCollision(ball2.getObj());
        addGameObject(ball2);

        Floor floor = new Floor(Vector3.Zero);
        physicsWorld.addCollision(floor.getBody());
        addGameObject(floor);
    }

    public void addGameObject(GameObject object) {
        gameObjectsToAdd.add(object);
        sceneManager.addScene(object);
    }

    public void removeGameObject(GameObject object) {
        gameObjectsToRemove.add(object);
        sceneManager.removeScene(object);
    }

    public void update(float delta) {
        this.delta = delta;

        if(!gameObjectsToAdd.isEmpty()) {
            gameObjects.addAll(gameObjectsToAdd);
            gameObjectsToAdd.clear();
        }

        if(!gameObjectsToRemove.isEmpty()) {
            gameObjects.removeAll(gameObjectsToRemove);
            gameObjectsToRemove.clear();
        }

        physicsWorld.update(delta);

        for (GameObject object : gameObjects) {
            object.update(this);
        }
    }

    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.camera.update();
        sceneManager.update(delta);
        sceneManager.render();
    }

    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    private void preloadAssets() {
        AssetManager manager = Assets.GetManager(); 
		manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
        manager.load("player.glb", SceneAsset.class);
        manager.load("bullet.gltf", SceneAsset.class);
    }

    @Override public void dispose() {
        for(GameObject object : gameObjects) {
            object.dispose();
        }
    }
}
