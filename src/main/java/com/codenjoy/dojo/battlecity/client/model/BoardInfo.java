package com.codenjoy.dojo.battlecity.client.model;

import com.codenjoy.dojo.services.Direction;

import java.awt.*;
import java.util.Map;

public class BoardInfo {
    public int Tick;
    public Map<Point, Direction> Bullets;
    public Map<Point, Direction> Tanks;
    public Map<Point, Integer> TankCooldowns;
    public Map<Point, Double> PopularCells;
    public com.codenjoy.dojo.battlecity.client.model.Board Board;
}
