package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.GameObject;
import com.application.javagame.Objects.Entities.Floor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Crawler extends GameObject {

  btRigidBody body;

  public Crawler(Vector3 p) {
    super(Assets.<SceneAsset>Get("diabo.gltf").scene, p);

    BoundingBox bb = new BoundingBox();
    modelInstance.calculateBoundingBox(bb);

    btCollisionShape shape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));
//    btCollisionShape shape = Bullet.obtainStaticNodeShape(modelInstance.nodes);

    collisionObject = new btCollisionObject();
    collisionObject.setCollisionShape(shape);

    body = new btRigidBody(0, null, shape, Vector3.Zero);
    body.translate(p);
    body.userData = this;
  }

  @Override
  public void update(GameState state) {
    for (GameObject object : state.gameObjects) {
      if(object == this) continue;
      if(object instanceof Floor) {
        if(state.collisionWorld.CheckCollision(this, object))
          System.out.println("COLISÃO");
      }
    }
  }
  
  public btRigidBody getBody() {
    return body;
  }

}
