package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IMacroMapBuilder;
import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.client.model.BoardInfo;
import com.codenjoy.dojo.battlecity.client.utils.DirectionHelper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

public class MacroMapBuilder implements IMacroMapBuilder {

    final double DANGER = -100;
    final double WARNING = -50;
    final double SMALL_WARNING = -25;
    final double GOOD = 50;
    final double SAFE = 100;

    private BoardInfo info;
    private int sz;
    private double[][] map;

    public double[][] BuildHeatMap(BoardInfo boardInfo) {
        info = boardInfo;
        sz = info.Size;
        clearMap();

        applyBarriers();

        applyBullets();
        applyTanks();

        applyPopularCells();
        applyWideSpots();

        return map;
    }

    private void clearMap() {
        map = new double[sz][];
        for(int i = 0; i < sz; ++i)
            map[i] = new double[sz];
    }
    private int calcDistToBecomeZero(double b0, double q) {
        var n = 0;
        double cur = 0;
        double limit = 0.01 / Math.abs(b0);
        while (cur > limit && n < 50) {
            n++;
            cur *= q;
        }
        return n;
    }
    private void addHeat(int x, int y, double b0, double q, int dist) {
        map[x][y] += b0 * Math.pow(q, dist);
    }
    private void spreadAll(int x, int y, double b0, double q) {
        int maxDist = calcDistToBecomeZero(b0, q);
        for (int i = -maxDist; i <= maxDist; ++i)
            for (int j = -maxDist; j <= maxDist; ++j)
            {
                int i1 = x + i;
                int j1 = y + j;
                var dist = Board.distance(i1, j1, x, y);
                if (dist > maxDist)
                    continue;
                addHeat(i1, j1, b0, q, dist);
            }
    }
    private void spreadRook(int x, int y, double b0, double q) {
        int maxDist = calcDistToBecomeZero(b0, q);
        for (int i = 0; i < sz; ++i)
            {
                var dist = Board.distance(i, y, x, y);
                if (dist > maxDist)
                    continue;
                addHeat(i, y, b0, q, dist);
            }
        for (int j = 0; j < sz; ++j)
        {
            var dist = Board.distance(x, j, x, y);
            if (dist > maxDist)
                continue;
            addHeat(x, j, b0, q, dist);
        }
    }
    private void spreadRay(int x, int y, Direction dir, double b0, double q) {
        int maxDist = calcDistToBecomeZero(b0, q);
        for (int i = 0; i < sz; ++i)
        {
            var dist = Board.distance(i, y, x, y);
            if (dist > maxDist)
                continue;
            var dx = i - x;
            if (DirectionHelper.pointToDirection(new PointImpl(dx, 0)) != dir)
                continue;
            addHeat(i, y, b0, q, dist);
        }
        for (int j = 0; j < sz; ++j)
        {
            var dist = Board.distance(x, j, x, y);
            if (dist > maxDist)
                continue;
            var dy = j - y;
            if (DirectionHelper.pointToDirection(new PointImpl(0, dy)) != dir)
                continue;
            addHeat(x, j, b0, q, dist);
        }
    }

    private void applyBullets() {
        for (var bullet : info.Bullets) {
            spreadRay(bullet.x, bullet.y, bullet.dir, DANGER, 0.3);
            spreadRay(bullet.x, bullet.y, bullet.dir, WARNING, 0.75);
        }
    }

    private void applyTanks() {
        for (var tank : info.Tanks) {
            spreadAll(tank.x, tank.y, DANGER, 0.3);
            spreadAll(tank.x, tank.y, SMALL_WARNING, 0.75);
        }
    }

    private void applyPopularCells() {
        int totalVisits = 0;
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j)
                totalVisits += info.Cells[i][j].timesVisited;
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j) {
                var cell = info.Cells[i][j];
                double visitValue = 1.0 * cell.timesVisited / totalVisits;
                spreadRook(cell.x, cell.y, 100 * visitValue, 1);
            }
    }

    private void applyWideSpots() {
        int delta = 2;
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j){
                int freeCells = 0;
                for (int i1 = -delta; i1 <= delta; ++i1)
                    for (int j1 = -delta; j1 <= delta; ++j1)
                    {
                        if (info.Board.isFreeAt(i + i1, j + j1))
                            freeCells++;
                    }
                spreadAll(i, j, 1.0 * (SAFE / 10) * freeCells / (delta * delta), 0.3);
            }
    }

    private void applyBarriers() {
        for (var barrier : info.Board.getBarriers()) {
            map[barrier.getX()][barrier.getY()] -= 1e6;
        }
    }


}
