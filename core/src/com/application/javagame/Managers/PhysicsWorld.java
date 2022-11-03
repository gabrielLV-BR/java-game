package com.application.javagame.Managers;

import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class PhysicsWorld {

    private final btDynamicsWorld dynamicsWorld;

    private final btCollisionConfiguration collisionConfiguration;
    private final btDispatcher dispatcher;

    private final btBroadphaseInterface broadphaseInterface;
    private final btConstraintSolver constraintSolver;

    public PhysicsWorld() {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);

        broadphaseInterface = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(
            dispatcher,
            broadphaseInterface,
            constraintSolver,
            collisionConfiguration
        );
    }
    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta);
    }
    public void addBody(btRigidBody body) {
        dynamicsWorld.addRigidBody(body);
    }
    public void removeBody(btRigidBody body) {
        dynamicsWorld.removeRigidBody(body);
    }
    public void addCollision(btCollisionObject obj) {
        dynamicsWorld.addCollisionObject(obj);
    }
}
