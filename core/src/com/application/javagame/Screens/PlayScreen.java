package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Objects.Entities.Floor;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.Entities.Enemies.Crawler;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;

public class PlayScreen extends ScreenAdapter {

    private final GameState state;

    public PlayScreen(GameState s) {

        this.state = s;

        Player player = new Player(new Vector3(0, 10, 0));
        state.sceneManager.setCamera(player.getCamera());

        state.setPlayer(player);
        state.addGameObject(player);
        state.physicsWorld.addController(player.getController());
        // state.physicsWorld.addBody(player.getBody());

        // Loading level
        Crawler crawler = new Crawler(new Vector3(0, 20, 0));
        state.addGameObject(crawler);
        // state.physicsWorld.addBody(crawler.getBody());

        Floor floor = new Floor(new Vector3(0, -10, 0), new Vector3(100, 2 ,100));
        state.addGameObject(floor);
        state.physicsWorld.addBody(floor.getBody());

        //{
        //  SceneAsset levelAsset = Assets.Get("bunkers.glb");
        //  for(SceneModel sceneModel : levelAsset.scenes) {
        //      Scene levelScene = new Scene(sceneModel);
        //      state.sceneManager.addScene(levelScene);
        //      btCollisionShape shape = Bullet.obtainStaticNodeShape(levelScene.modelInstance.nodes);
        //      btRigidBody levelBody = new btRigidBody(0, null, shape, Vector3.Zero);
        //      state.physicsWorld.addBody(levelBody);
        //  }
        //}
    }

    @Override
    public void render(float delta) {
        state.update(delta);
        state.render();
    }

    @Override public void resize(int width, int height) {
        state.resize(width, height);
    }
}