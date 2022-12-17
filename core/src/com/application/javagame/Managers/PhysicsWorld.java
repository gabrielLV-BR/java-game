package com.application.javagame.Managers;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btActionInterface;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;

public class PhysicsWorld implements Disposable {

    public final btDynamicsWorld dynamicsWorld;
    private final btCollisionWorld collisionWorld;

    private final btCollisionConfiguration collisionConfiguration;
    private final btDispatcher dispatcher;

    private final btBroadphaseInterface broadphaseInterface;
    private final btConstraintSolver constraintSolver;

    // Debug
    private final DebugDrawer debugDrawer;
    //

    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static ClosestRayResultCallback callback;
    private ContactResultCallback contactResultCallback;

    private btCollisionObject ground;

    public PhysicsWorld() {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);

        broadphaseInterface = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(
                dispatcher,
                broadphaseInterface,
                constraintSolver,
                collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3(0, -100, 0));

        collisionWorld = new btCollisionWorld(
                dispatcher,
                broadphaseInterface,
                collisionConfiguration);

        contactResultCallback = new ContactResultCallback();
        //
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta);
    }

    public void setGround(btCollisionObject ground) {
        this.ground = ground;
    }

    public btCollisionObject getGround() {
        return ground;
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

    public boolean IsGrounded(btRigidBody body) {
        if(ground == null) return false;

        collisionWorld.addCollisionObject(body);
        collisionWorld.addCollisionObject(ground);

        // Executa o teste de contato entre o corpo rígido 1 e todos os outros corpos
        // rígidos

        collisionWorld.contactPairTest(body, ground, contactResultCallback);

        boolean hasCollided = callback.hasHit() && 
            callback.getCollisionObject().equals(body);

        collisionWorld.removeCollisionObject(body);
        collisionWorld.removeCollisionObject(ground);
        
        return hasCollided;
    }

    /*
     * Código pego daqui ->
     * https://stackoverflow.com/questions/24988852/raycasting-in-libgdx-3d/24989069
     * #24989069
     */
    public ClosestRayResultCallback rayCast(Ray ray) {
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(5000f).add(rayFrom);

        callback = new ClosestRayResultCallback(rayFrom, rayTo);
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1f);

        dynamicsWorld.rayTest(rayFrom, rayTo, callback);

        return callback;
    }

    public void dispose() {
        broadphaseInterface.dispose();
        callback.dispose();
        collisionConfiguration.dispose();
        constraintSolver.dispose();
        dispatcher.dispose();
        debugDrawer.dispose();
        dynamicsWorld.dispose();
    }
}
