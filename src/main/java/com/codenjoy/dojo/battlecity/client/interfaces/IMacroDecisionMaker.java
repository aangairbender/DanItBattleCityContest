package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.services.Point;

public interface IMacroDecisionMaker {
    Point findBestPosition(IPathFinder pathFinder, double[][] heatMap, int size);
}
