package com.codenjoy.dojo.battlecity.client.utils;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

public class DirectionHelper {
    public static Direction pointToDirection(Point point) {
        var x = point.getX();
        var y = point.getY();
        if (Math.abs(x) + Math.abs(y) != 1)
            return Direction.STOP;

        if (x == -1) return Direction.LEFT;
        if (x == 1) return Direction.RIGHT;
        if (y == -1) return Direction.DOWN;
        if (y == 1) return Direction.UP;

        return Direction.STOP;
    }

    public static Point directionToPoint(Direction dir) {
        switch (dir) {
            case LEFT: return new PointImpl(-1, 0);
            case RIGHT: return new PointImpl(1, 0);
            case DOWN: return new PointImpl(0, -1);
            case UP: return new PointImpl(0, 1);
            default: return new PointImpl(0, 0);
        }
    }

    public static Collection<Direction> getBasicDirections() {
        return Arrays.asList(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
    }
}
