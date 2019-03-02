package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.services.Direction;

public interface IPathFinder {
    Direction findPathTo(int x, int y, double[][] heatMap, Direction needForShot);
}
