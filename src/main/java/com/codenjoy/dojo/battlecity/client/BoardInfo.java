package com.codenjoy.dojo.battlecity.client;

import com.codenjoy.dojo.services.Direction;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public class BoardInfo {
    public int Tick;
    public Map<Point, Direction> Bullets;
    public Map<Point, Direction> Tanks;
    public Map<Point, Integer> TankCooldowns;
    public Map<Point, Double> PopularCells;
    public Board Board;
}
