package com.application.javagame;

import java.util.ArrayList;

import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.EnemySpawner;
import com.application.javagame.Managers.InputManager;
import com.application.javagame.Managers.PhysicsWorld;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Map;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.Entities.Enemies.Enemy;
import com.application.javagame.Screens.DeathScreen;
import com.application.javagame.Utils.Utils3D;
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
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.SimpleOrthoGroupStrategy;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.attributes.FogAttribute;
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

    public int points;
    public int survivedRounds;
    private String statusMessage;
    private boolean restarting;

    private Player player = null;
    public float delta;

    private float timeLeft;
    private final float ROUND_TIME;

    private Map map;
    private int tries;
    private int target;

    private Sprite crosshair;
    private BitmapFont doomFont, doomFontBig;

    private EnemySpawner enemySpawner;

    private final int TARGET = 1;

    public GameState(Game g) {
        points = 0;
        survivedRounds = 0;

        ROUND_TIME = 60;
        timeLeft = ROUND_TIME;
        restarting = false;

        tries = 3;
        delta = 0;
        target = TARGET;
        physicsWorld = new PhysicsWorld();
        decalBatch = new DecalBatch(new SimpleOrthoGroupStrategy());
        spriteBatch = new SpriteBatch();

        spritesToDraw = new ArrayList<>();
        gameObjects = new ArrayList<>();
        gameObjectsToAdd = new ArrayList<>();
        gameObjectsToRemove = new ArrayList<>();

        game = g;

        enemySpawner = new EnemySpawner();

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

    public int getRemainingEnemies() {
        int count = 0;
        for(GameObject obj : gameObjects) {
            if(obj instanceof Enemy) count++;
        }
        return count;
    }

    public void setPlayer(Player p) {
        this.player = p;
        decalBatch.setGroupStrategy(new CameraGroupStrategy(p.getCamera()));
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    private void configSceneManager() {
        sceneManager.setAmbientLight(0.3f);
        sceneManager.updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void enemyDied() {
        if(target > 0) target--;
        enemySpawner.spawnAnother(this);
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
        
        sceneManager.environment.set(FogAttribute.createFog(0, 10, 6));
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseMap));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularMap));

        SceneSkybox skybox = new SceneSkybox(environmentMap);
        sceneManager.setSkyBox(skybox);
    }

    public void newRound() {
        int p = points;

        for(GameObject object : gameObjects) {
            if(object instanceof Enemy)
             ((Enemy) object).die(this);
        }

        points = p;
        survivedRounds++;
        timeLeft = ROUND_TIME;
        target = Math.abs(TARGET + survivedRounds);
        map.ready();

        // player.randomizeWeapon();
        
        enemySpawner.populate(this, (int)(target * 1.5));
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

        timeLeft -= delta;
        
        statusMessage = "" + (int) timeLeft; 

        if(timeLeft <= 0 && target > 0) {
            tries--;
            game.setScreen(new DeathScreen(this));
            return;
        } else if (target <= 0) {
            handleRestart();
        } 

        for (GameObject object : gameObjects) {
            object.update(this);
        }

        enemySpawner.update(this);
        // System.out.println("Inimigos: " + getRemainingEnemies());
    }

    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.camera.update();
        sceneManager.update(delta);
        sceneManager.render();

        Gdx.gl20.glDepthMask(false);
        decalBatch.flush();

        spriteBatch.begin();
        for(Sprite s : spritesToDraw) s.draw(spriteBatch);

        doomFont.draw(spriteBatch, "POINTS: " + getPoints(), 10, Gdx.graphics.getHeight() - 20);

        if(!restarting) {
            doomFontBig.draw(
                spriteBatch, 
                statusMessage, 
                Gdx.graphics.getWidth() / 2 - (statusMessage.length() * 10),
                Gdx.graphics.getHeight() - 20
            );
            doomFont.draw(
                spriteBatch, 
                "" + target, 
                Gdx.graphics.getWidth() - 20,
                20
            );
        }

        spriteBatch.end();

        physicsWorld.debug_render(sceneManager.camera);
    }

    public void reset() {
        points = 0;
        survivedRounds = 0;
        timeLeft = ROUND_TIME;
        restarting = false;
        tries = 3;
        delta = 0;
        target = 5;
        gameObjects.clear();
        gameObjectsToAdd.clear();
        gameObjectsToRemove.clear();
    }

    public void resize(int width, int height) {
        updateCrosshairPosition();
        sceneManager.updateViewport(width, height);
    }

    public int getTries() {
        return tries;
    }

    private void preloadAssets() {
        AssetManager manager = Assets.GetManager(); 
  
		manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());

        manager.load("map.glb", SceneAsset.class);
        manager.load("map2.glb", SceneAsset.class);
        manager.load("crawler.glb", SceneAsset.class);
        manager.load("vesper.glb", SceneAsset.class);
        manager.load("vesper2.glb", SceneAsset.class);
        manager.load("door.glb", SceneAsset.class);
        manager.load("guns/handgun.png", Texture.class);
        manager.load("guns/shotgun.png", Texture.class);
        manager.load("guns/machinegun.png", Texture.class);

        manager.load("crosshair.png", Texture.class);
        manager.load("hole.png", Texture.class);
        manager.load("explosion.png", Texture.class);
        manager.load("hellrise.png", Texture.class);
        manager.load("hellrise black.png", Texture.class);
        manager.load("white.png", Texture.class);

        manager.load("sounds/pistol.mp3", Sound.class);
        manager.load("sounds/pistol_reload.mp3", Sound.class);
        manager.load("sounds/shotgun.mp3", Sound.class);
        manager.load("sounds/next.mp3", Sound.class);
        // manager.load("fonts/eternal.ttf", BitmapFont.class);
    }

    public void handleRestart() {
        if (!restarting) {
            if (map.getElevatorBoundingBox().contains(player.getPosition())) {
                map.reset();
                statusMessage = "ROUND " + (survivedRounds + 1);
                timeLeft = 6;
                restarting = true;
            } else {
                statusMessage = "VOLTE PRO ELEVADOR";
            }
        } else if (restarting && timeLeft <= 0) {
            restarting = false;
            newRound();
        } 
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
    public Game getGame() {
        return game;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
        enemySpawner.setSpawnpoints(map.spawnPoints.subList(1, map.spawnPoints.size() - 1));
        for (Vector3 p : map.spawnPoints) {
            Utils3D.printVector3("Spawnpoint: ", p);    
        }
    }
}
