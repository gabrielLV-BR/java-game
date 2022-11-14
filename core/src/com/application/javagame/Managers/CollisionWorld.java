package com.application.javagame.Managers;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;

public class CollisionWorld implements Disposable {

  btCollisionConfiguration collisionConfig;
  btDispatcher dispatcher;

  //
  private CollisionObjectWrapper co1;
  private CollisionObjectWrapper co2;
  private btCollisionAlgorithm algorithm;
  btDispatcherInfo dispatcherInfo;
  btManifoldResult result;

  public CollisionWorld() {
    collisionConfig = new btDefaultCollisionConfiguration();
    dispatcher = new btCollisionDispatcher(collisionConfig);
    dispatcherInfo = new btDispatcherInfo();
    result = new btManifoldResult();
  }

  // public boolean CheckCollision(GameObject a, GameObject b) {
  //   co1 = new CollisionObjectWrapper(a.getCollisionObject());
  //   co2 = new CollisionObjectWrapper(b.getCollisionObject());

  //   algorithm = dispatcher.findAlgorithm(
  //     co1.wrapper, co2.wrapper, 
  //     null, 
  //     ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS
  //   );

  //   result.setBody0Wrap(co1.wrapper);
  //   result.setBody1Wrap(co2.wrapper);

  //   dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());

  //   btPersistentManifold persistentManifold = result.getPersistentManifold();

  //   return persistentManifold != null && persistentManifold.getNumContacts() > 0;
  // }

  @Override
  public void dispose() {
    co1.dispose();
    co2.dispose();
  }
}