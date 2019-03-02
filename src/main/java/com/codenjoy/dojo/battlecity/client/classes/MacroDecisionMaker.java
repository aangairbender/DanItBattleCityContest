package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IBoardAnalyzer;
import com.codenjoy.dojo.battlecity.client.interfaces.IMacroDecisionMaker;
import com.codenjoy.dojo.battlecity.client.interfaces.IMacroMapBuilder;
import com.codenjoy.dojo.battlecity.client.interfaces.IPathFinder;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class MacroDecisionMaker implements IMacroDecisionMaker {


    public Point findBestPosition(IPathFinder pathFinder, double[][] heatMap, int size){
        int bestI = -1, bestJ = -1;
        double bestDelta = -1e18;
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j){
                var path = pathFinder.findPathTo(i, j, Direction.STOP);
                var delta = heatMap[i][j] - path.getRight();
                if (delta > bestDelta) {
                    bestDelta = delta;
                    bestI = i;
                    bestJ = j;
                }
            }
        return new PointImpl(bestI, bestJ);
    }
}
