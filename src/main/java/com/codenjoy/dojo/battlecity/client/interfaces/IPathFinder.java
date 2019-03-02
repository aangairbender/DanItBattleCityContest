package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.services.Direction;
import org.apache.commons.lang3.tuple.Pair;

public interface IPathFinder {
    Pair<Direction, Double> findPathTo(int x, int y, Direction needForShot);
}
