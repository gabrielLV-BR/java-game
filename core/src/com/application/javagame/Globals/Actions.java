package com.application.javagame.Globals;

import com.badlogic.gdx.Input;

public class Actions {
    public static class Player {
        public static int FORWARD = Input.Keys.W;
        public static int BACKWARD = Input.Keys.S;
        public static int LEFT = Input.Keys.A;
        public static int RIGHT = Input.Keys.D;

        public static int JUMP = Input.Keys.SPACE;

        public static int[] getButtons() {
            return new int[]{FORWARD, BACKWARD, LEFT, RIGHT, JUMP};
        }
    }
}
