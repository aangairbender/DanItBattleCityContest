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

        applyPopularCeels();
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
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j)
            {
                var dist = Board.distance(i, j, x, y);
                if (dist > maxDist)
                    continue;
                addHeat(x, y, b0, q, dist);
            }
    }
    private void spreadRook(int x, int y, double b0, double q) {
        int maxDist = calcDistToBecomeZero(b0, q);
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j)
            {
                var dist = Board.distance(i, j, x, y);
                if (dist > maxDist)
                    continue;
                int dx = i - x;
                int dy = j - y;
                if (dx * dy != 0)
                    continue;
                addHeat(x, y, b0, q, dist);
            }
    }
    private void spreadRay(int x, int y, Direction dir, double b0, double q) {
        int maxDist = calcDistToBecomeZero(b0, q);
        for (int i = 0; i < sz; ++i)
            for (int j = 0; j < sz; ++j)
            {
                var dist = Board.distance(i, j, x, y);
                if (dist > maxDist)
                    continue;
                int dx = i - x;
                int dy = j - y;
                if (dx * dy != 0)
                    continue;
                if (DirectionHelper.pointToDirection(new PointImpl(dx, dy)) != dir)
                    continue;
                addHeat(x, y, b0, q, dist);
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

    private void applyPopularCeels() {
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
