package com.application.javagame.Managers;

import javax.management.DynamicMBean;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

public class PhysicsWorld {

    private final btDynamicsWorld dynamicsWorld;

    private final btCollisionConfiguration collisionConfiguration;
    private final btDispatcher dispatcher;

    private final btBroadphaseInterface broadphaseInterface;
    private final btConstraintSolver constraintSolver;

    // Debug
    private final DebugDrawer debugDrawer;
    //

    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);


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

        //
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta);
    }

    public void debug_render(Camera camera) {
        debugDrawer.begin(camera);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    public void addBody(btRigidBody body) {
        dynamicsWorld.addRigidBody(body);
    }
    
    public void removeBody(btRigidBody body) {
        dynamicsWorld.removeRigidBody(body);
    }
    
    public void addController(btActionInterface action) {
        dynamicsWorld.addAction(action);
    }

    public void removeController(btActionInterface action) {
        dynamicsWorld.removeAction(action);
    }

    /*
        Código pego daqui -> https://stackoverflow.com/questions/24988852/raycasting-in-libgdx-3d/24989069#24989069
    */
    public btCollisionObject rayCast(Ray ray) {
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(50f).add(rayFrom);
    
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1f);
        callback.getRayFromWorld(rayFrom);
        callback.getRayToWorld(rayTo);
    
        dynamicsWorld.rayTest(rayFrom, rayTo, callback);
    
        if (callback.hasHit()) {
            return callback.getCollisionObject();
        }
    
        return null;
    }
}
