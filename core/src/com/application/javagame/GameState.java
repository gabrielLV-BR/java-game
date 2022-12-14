package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Managers.PhysicsWorld;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Entities.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
    private final ArrayList<Sprite> spritesToDraw;

    public final Game game;

    private Player player = null;
    public float delta;

    private Sprite crosshair;
    private BitmapFont doomFont, doomFontBig;

    public GameState(Game g) {
        delta = 0;
        physicsWorld = new PhysicsWorld();
        decalBatch = new DecalBatch(new SimpleOrthoGroupStrategy());
        spriteBatch = new SpriteBatch();

        spritesToDraw = new ArrayList<>();
        gameObjects = new ArrayList<>();
        gameObjectsToAdd = new ArrayList<>();
        gameObjectsToRemove = new ArrayList<>();

        game = g;

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
        addSprite(crosshair);
        updateCrosshairPosition();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/eternal.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        parameter.size = 12; // font size
        doomFont = generator.generateFont(parameter);

        parameter.size = 24;
        parameter.color = new Color(0.8f, 0.1f, 0.2f, 1);
        doomFontBig = generator.generateFont(parameter);
    }

    public void setPlayer(Player p) {
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
        light.set(Color.BLACK, new Vector3(1, -1, 0).nor());
        light.intensity = 0.2f;
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

    public void addSprite(Sprite s) {
        spritesToDraw.add(s);
    }

    public void removeSprite(Sprite s) {
        spritesToDraw.remove(s);
    }

    public void update(float delta) {
        Assets.GetManager().update();
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

    float timeLeft = 360;

    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.camera.update();
        sceneManager.update(delta);
        sceneManager.render();

        timeLeft -= delta;

        Gdx.gl20.glDepthMask(false);
        decalBatch.flush();

        spriteBatch.begin();
        for(Sprite s : spritesToDraw) s.draw(spriteBatch);

        doomFont.draw(spriteBatch, "POINTS: " + getPlayer().getPoints(), 10, Gdx.graphics.getHeight() - 20);
        doomFontBig.draw(spriteBatch, "" + (int)timeLeft, Gdx.graphics.getWidth() / 2 - 35, Gdx.graphics.getHeight() - 20);
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

        manager.load("map.glb", SceneAsset.class);
        manager.load("map2.glb", SceneAsset.class);
        manager.load("crawler.glb", SceneAsset.class);
        manager.load("vesper.glb", SceneAsset.class);
        manager.load("door.glb", SceneAsset.class);
        manager.load("guns/handgun.png", Texture.class);
        manager.load("guns/shotgun.png", Texture.class);

        manager.load("crosshair.png", Texture.class);
        manager.load("hole.png", Texture.class);
        manager.load("explosion.png", Texture.class);
        manager.load("hellrise.png", Texture.class);
        manager.load("hellrise black.png", Texture.class);
        manager.load("white.png", Texture.class);

        manager.load("sounds/shotgun.mp3", Sound.class);
        manager.load("sounds/activate.mp3", Sound.class);
        manager.load("sounds/next.mp3", Sound.class);
        // manager.load("fonts/eternal.ttf", BitmapFont.class);
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
