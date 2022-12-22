package com.application.javagame.Managers;

import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.ContactResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectArray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btPersistentManifold;
import com.badlogic.gdx.physics.bullet.collision.ebtDispatcherQueryType;
import com.badlogic.gdx.physics.bullet.dynamics.btActionInterface;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;

public class PhysicsWorld implements Disposable {

    public final btDynamicsWorld dynamicsWorld;

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

    CollisionObjectWrapper co0, co1;
    btCollisionAlgorithm algorithm;
    btDispatcherInfo info;
    btManifoldResult result;
    btPersistentManifold man;

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

        contactResultCallback = new ContactResultCallback() {
            @Override
            public float addSingleResult(btManifoldPoint cp, 
                btCollisionObjectWrapper objWrapper1, int partId0,
                int index0, btCollisionObjectWrapper objWrapper2, int partId1, 
                int index1) {
                    btCollisionObject obj1 = objWrapper1.getCollisionObject();
                    btCollisionObject obj2 = objWrapper2.getCollisionObject();

                    if(obj1.userData instanceof GameObject) {
                        ((GameObject)(obj1.userData)).collideWith(obj2);
                    }

                    if(obj2.userData instanceof GameObject) {
                        ((GameObject)(obj2.userData)).collideWith(obj1);
                    }

                    return 0;
                }
        };

        dynamicsWorld.setForceUpdateAllAabbs(true);
        dynamicsWorld.setGravity(new Vector3(0, -100, 0));
        //
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta, 6);
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

    // public boolean isGrounded(btCollisionObject obj) {
    //     return ground != null && checkCollision(obj, ground);
    // } 

    /*
     * Código pego daqui -> https://github.com/JamesTKhan/libgdx-bullet-tutorials/blob/master/core/src/com/jpcodes/physics/screens/BasicCollisionDetection.java
     * Alterado um pouco
     */
    public void performCollisionCheck (btCollisionObject b1, btCollisionObject b2) {
        btCollisionWorld world = new btCollisionWorld(
            dispatcher, 
            broadphaseInterface, 
            collisionConfiguration
        );

        world.addCollisionObject(b1);
        world.addCollisionObject(b2);

        boolean hasCollided = false;

        world.contactPairTest(b1, b2, contactResultCallback);

        // co0 = new CollisionObjectWrapper(b1);
        // co1 = new CollisionObjectWrapper(b2);

        // // For each pair of shape types, Bullet will dispatch a certain collision algorithm, by using the dispatcher.
        // // So we use the dispatcher here to find the algorithm needed for the two shape types being checked, ex. btSphereBoxCollisionAlgorithm
        // algorithm = dispatcher.findAlgorithm(
        //     co0.wrapper, 
        //     co1.wrapper, 
        //     null,
        //     ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS
        // );

        // info = new btDispatcherInfo();
        // result = new btManifoldResult(co0.wrapper, co1.wrapper);

        // // Execute the algorithm using processCollision, this stores the result (the contact points) in the btManifoldResult
        // algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        // // Free the algorithm back to a pool for reuse later
        // dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());

        // boolean r = false;

        // // btPersistentManifold is a contact point cache to store contact points for a given pair of objects.
        // man = result.getPersistentManifold();
        // if (man != null) {
        //     // If the number of contact points is more than zero, then there is a collision.
        //     r = man.getNumContacts() > 0;
        // }
        // System.out.println("Num contacts: " + man.getNumContacts());

        // return r;
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
        result.dispose();
        info.dispose();
        co1.dispose();
        co0.dispose();
        broadphaseInterface.dispose();
        callback.dispose();
        collisionConfiguration.dispose();
        constraintSolver.dispose();
        dispatcher.dispose();
        debugDrawer.dispose();
        dynamicsWorld.dispose();
    }
}
