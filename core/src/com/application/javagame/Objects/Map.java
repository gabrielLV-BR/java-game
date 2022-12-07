package com.application.javagame.Objects;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleIndexVertexArray;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Map extends GameObject {

    btCollisionShape shape;
    btRigidBody body;

    boolean usingShape = false;

    public Map(GameState state, Vector3 p, Vector3 size){
        super(Assets.<SceneAsset>Get("map.glb").scene, p);
        scene.modelInstance.transform.scl(size);

        ArrayList<Integer> collisionShapesIndexes = 
            getCollisionNodesIndexes(); 

        System.out.println("Number of collision shapes: " + collisionShapesIndexes);
        btCollisionShape shape = new btSphereShape(10);

        if (collisionShapesIndexes.size() == 0) {
            BoundingBox bb = new BoundingBox();
            scene.modelInstance.calculateBoundingBox(bb);
            shape = new btBoxShape(bb.getDimensions(tmpVector).scl(0.5f));
        } else {
            //btCompoundShape compoundShape = new btCompoundShape();
            /*
            for(int collisionShapeIndex : collisionShapesIndexes) {
                
                Node node = scene.modelInstance.nodes.get(collisionShapeIndex);
                btCollisionShape childShape = Bullet.obtainStaticNodeShape(
                    node, false
                );
                
                System.out.print("Child shape #" + collisionShapeIndex + "is ");
                System.out.println(childShape.isConvex() ? "convex (GOOD)" : "concave (BAD)");
                compoundShape.addChildShape(node.globalTransform.scl(size), childShape);

               // scene.modelInstance.nodes.removeIndex(collisionShapeIndex);
            }
            */

            for(int collisionShapeIndex : collisionShapesIndexes) {
                Node node = 
                    scene.modelInstance.nodes.get(collisionShapeIndex);

                // Create a triangle mesh shape
                btTriangleIndexVertexArray triMesh = new btTriangleIndexVertexArray();
                triMesh.addNodeParts(node.parts);

                // Create a collision shape from the triangle mesh
                btBvhTriangleMeshShape bizarreShape = 
                    new btBvhTriangleMeshShape(triMesh, true);

                bizarreShape.setLocalScaling(size);

                float mass = 0f;
                Vector3 inertia = new Vector3(10, 0, 0);

                btRigidBody body2 = new btRigidBody(mass, null, bizarreShape, inertia);
                body2.translate(p);
                body2.userData = this;

                state.physicsWorld.addBody(body2);

                // compoundShape.addChildShape(node.globalTransform.scl(size), bizarreShape);

                // shape = bizarreShape;
                // break;

            }

            // shape = compoundShape;

            //System.out.println("Shape is compound with " +
            //        compoundShape.getNumChildShapes()
            //        + " children");
        }
                /*
        float mass = 0f;

        body = new btRigidBody(mass, null, shape, Vector3.Zero);
        body.translate(p);
        body.userData = this;
            */
        state.addGameObject(this);
    }

    public btRigidBody getBody() {
        return body;
    }

    @Override
    public void update(GameState state) {
        //body.getWorldTransform(scene.modelInstance.transform);
    }
}
