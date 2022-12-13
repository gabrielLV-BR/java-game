package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Explosion extends Particle {

    public Explosion(Vector3 position, float duration) {
        super(
            Assets.<Texture>Get("explosion.png"),
            position, null, duration
        );

        decal.setDimensions(18, 18);
    }

    @Override
    public void update(GameState state) {
        if(normal == null) {
            Camera camera = state.getPlayer().getCamera();
            decal.lookAt(camera.position, camera.up);
        }
        duration -= state.delta;

        float normalizedDuration = duration / initialDuration;

        decal.setScale(easingFunction(normalizedDuration));

        if(duration <= 0) state.removeGameObject(this);
        else state.decalBatch.add(decal);   
    }

    public float easingFunction(float t) {
        return t;
    }
}