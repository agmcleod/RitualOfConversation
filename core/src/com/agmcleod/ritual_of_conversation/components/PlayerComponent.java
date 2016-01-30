package com.agmcleod.ritual_of_conversation.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class PlayerComponent implements Component {
    public boolean movingLeft;
    public boolean movingRight;

    public boolean setInputKeyState(int keyCode, boolean state) {
        switch (keyCode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                movingLeft = state;
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                movingRight = state;
                return true;
            default:
                return false;
        }
    }
}
