package com.codenjoy.dojo.battlecity.client.model;

public class CellInfo {
    public int x;
    public int y;
    public int timesVisited;

    public CellInfo(int x, int y) {
        this.x = x;
        this.y = y;
        timesVisited = 0;
    }
}
