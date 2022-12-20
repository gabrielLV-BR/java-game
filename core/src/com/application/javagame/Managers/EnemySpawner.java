package com.application.javagame.Managers;

import java.util.ArrayList;
import java.util.List;

import com.application.javagame.GameState;
import com.application.javagame.Objects.Entities.Enemies.*;
import com.application.javagame.Utils.Utils3D;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class EnemySpawner {

    ArrayList<Vector3> positions;

    final float MAX_TIMER = 5;
    private float timer;

    public EnemySpawner() {
        this.positions = new ArrayList<>();

        timer = MAX_TIMER;
    }

    public void setSpawnpoints(List<Vector3> list) {
        positions.addAll(list);
    }

    public void populate(GameState state, int num) {
        num = num - state.getRemainingEnemies();

        for(; num > 0; num--) {
            Vector3 p = positions.get(MathUtils.random(0, positions.size() - 1));

            int r = MathUtils.random(0, 1);
            switch (r) {
                case 1: {
                    Crawler c = new Crawler(p);
                    c.register(state);
                    break;
                }
                case 2: {
                    Vesper v = new Vesper(p);
                    v.register(state);
                    break;
                }
            }
        }
    }

    public void update(GameState state) {
        timer -= state.delta;
        if(timer <= 0) {
            timer = MAX_TIMER;

            Vector3 p = positions.get(MathUtils.random(0, positions.size() - 1));
            Utils3D.printVector3("OMG! spawned at ", p);
        
           int r = MathUtils.random(0, 1);
           r = 2;
           switch(r) {
            case 1: {
                Crawler c = new Crawler(p);
                c.register(state);
                break;
            }
            case 2: {
                Vesper v = new Vesper(p);
                v.register(state);
                break;
            }
           } 
        }
    }
}