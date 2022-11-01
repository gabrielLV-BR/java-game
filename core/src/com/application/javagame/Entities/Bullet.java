package com.application.javagame.Entities;

import com.application.javagame.GameState;
import com.application.javagame.Managers.ModelManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;

public class Bullet extends Entity {

    Vector3 direction;
    Vector3 tmpVec;

    static Model model = null;
    ModelInstance instance;

    public Bullet(Vector3 position, Vector3 direction, float speed) {
        super(position);
        this.direction = direction.nor().cpy().scl(speed);

        if(Bullet.model == null) {
            ObjLoader loader = new ObjLoader();
            Bullet.model = loader.loadModel(Gdx.files.internal("bala.obj"));
        }

        instance = new ModelInstance(Bullet.model, position);
        tmpVec = new Vector3();
    }

    @Override
    public void update(GameState state) {
        tmpVec.set(direction).scl(state.delta);
        position.add(tmpVec);
        instance.transform.setTranslation(position);
    }

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        batch.render(instance, environment);
    }

    @Override
    public void dispose() {
        Bullet.model.dispose();
    }
}
