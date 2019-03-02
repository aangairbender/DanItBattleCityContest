package com.codenjoy.dojo.battlecity.client.model;

import com.codenjoy.dojo.services.Direction;

public class Bullet {
    public int x, y;
    public Direction dir;

    public Bullet() {
        x = y = 0;
        dir = Direction.UP;
    }

    public Bullet(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}
