package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Managers.PhysicsWorld;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRDepthShaderProvider;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class GameState implements Disposable {

    public final SceneManager sceneManager;
    public final PhysicsWorld physicsWorld;
    public final DecalBatch decalBatch;
    private final SpriteBatch spriteBatch;

    public final ArrayList<GameObject> gameObjects;
    private final ArrayList<GameObject> gameObjectsToAdd;
    private final ArrayList<GameObject> gameObjectsToRemove;

    private Player player = null;
    public float delta;

    private Sprite crosshair;

    public GameState() {
        delta = 0;
        physicsWorld = new PhysicsWorld();
        decalBatch = new DecalBatch(new SimpleOrthoGroupStrategy());
        spriteBatch = new SpriteBatch();

        gameObjects = new ArrayList<>();
        gameObjectsToAdd = new ArrayList<>();
        gameObjectsToRemove = new ArrayList<>();

        PBRShaderConfig shaderConfig = PBRShaderProvider.createDefaultConfig();
        shaderConfig.numBones = 60;
        shaderConfig.numDirectionalLights = 1;
        shaderConfig.numPointLights = 0;

        com.badlogic.gdx.graphics.g3d.shaders.DepthShader.Config depthConfig = 
            PBRShaderProvider.createDefaultDepthConfig();
        
        depthConfig.numBones = shaderConfig.numBones;

        sceneManager = new SceneManager(
            new PBRShaderProvider(shaderConfig),
            new PBRDepthShaderProvider(depthConfig)
        );

        preloadAssets();
        configSceneManager();
        setupIBL();

        crosshair = new Sprite(Assets.<Texture>Get("crosshair.png"));
        updateCrosshairPosition();
    }

    public void setPlayer(Player p ) {
        this.player = p;
        decalBatch.setGroupStrategy(new CameraGroupStrategy(p.getCamera()));
    }

    public Player getPlayer() {
        return player;
    }

    private void configSceneManager() {
        sceneManager.setAmbientLight(0.3f);
        sceneManager.updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupIBL() {
        DirectionalLightEx light = new DirectionalLightEx();
        light.set(Color.WHITE, new Vector3(1, -1, 0).nor());
        light.intensity = 10f;
        sceneManager.environment.add(light);

        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentMap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseMap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularMap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseMap));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularMap));

        SceneSkybox skybox = new SceneSkybox(environmentMap);
        sceneManager.setSkyBox(skybox);
    }

    public void addGameObject(GameObject object) {
        gameObjectsToAdd.add(object);
        if(object.getScene() != null) 
            sceneManager.addScene(object.getScene());
    }

    public void removeGameObject(GameObject object) {
        gameObjectsToRemove.add(object);
        if(object.getScene() != null) 
            sceneManager.removeScene(object.getScene());
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
        decalBatch.flush();

        spriteBatch.begin();
        crosshair.draw(spriteBatch);    
        spriteBatch.end();

        physicsWorld.debug_render(sceneManager.camera);
    }

    public void resize(int width, int height) {
        updateCrosshairPosition();
        sceneManager.updateViewport(width, height);
    }

    private void preloadAssets() {
        AssetManager manager = Assets.GetManager(); 
		manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
        manager.load("player.glb", SceneAsset.class);
        manager.load("crawler.glb", SceneAsset.class);
        manager.load("cube.glb", SceneAsset.class);
        manager.load("sphere.glb", SceneAsset.class);
        manager.load("map.glb", SceneAsset.class);
        manager.load("map2.glb", SceneAsset.class);

        manager.load("sounds/shotgun.mp3", Sound.class);
        manager.load("crosshair.png", Texture.class);
        manager.load("explosion.png", Texture.class);
    }

    private void updateCrosshairPosition() {
        crosshair.setPosition(
            Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2 
        );
    }

    @Override public void dispose() {
        for(GameObject object : gameObjects) {
            object.dispose();
        }

        Assets.GetManager().dispose();
        InputManager.GetInputManager().dispose();

        decalBatch.dispose();
        sceneManager.dispose();
        physicsWorld.dispose();
    }
}
