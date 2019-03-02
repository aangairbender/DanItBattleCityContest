package com.codenjoy.dojo.battlecity.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import org.reflections.vfs.Vfs;

import java.util.*;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private Direction[] order = new Direction[4];
    private int[][] b = new int[43][43];
    private Collection<Elements> obstacles = new Vector<>();
    private int tick = 0;
    private Point curMoveTarget = null;

    private int curDirIndex = 0;

    public YourSolver(Dice dice) {
        this.dice = dice;

        order[0] = Direction.DOWN;
        order[1] = Direction.LEFT;
        order[2] = Direction.UP;
        order[3] = Direction.RIGHT;

        obstacles.addAll(Elements.getConstructions());
        obstacles.add(Elements.BATTLE_WALL);
        obstacles.add(Elements.BULLET);
    }

    @Override
    public String get(Board board)
    {
        try {
            this.board = board;
            if (board.isGameOver()) return "";
            tick++;
            if (tick % 20 == 0) {
                for (int i = 0; i < 4; ++i) {
                    int j = dice.next(4);
                    var tmp = order[i];
                    order[i] = order[j];
                    order[j] = tmp;
                }
                curDirIndex = 0;
            }

            getEnemy();

            Direction moveDirection = null;
            boolean act = false;

            for (var dir : order) {
                if (!needAct(dir))
                    continue;
                moveDirection = dir;
                act = true;
                break;
            }
            if (!act) {
                moveDirection = curMoveTarget == null ? makeMove() : getPathTo(curMoveTarget.getX(), curMoveTarget.getY());
            }

            var result = moveDirection.toString();
            if (act) {
                result += ", " + Direction.ACT.toString();
            }

            return result;
        } catch (Exception e)
        {
            return Direction.STOP.toString();
        }
    }

    private Direction makeMove() {
        var me = this.board.getMe();
        for (int k = 0; k < 4; ++k){
            var dir = order[curDirIndex];
            var dirPoint = resolveDirection(dir);
            int i = dirPoint.getX();
            int j = dirPoint.getY();
            int nextX = me.getX() + i;
            int nextY = me.getY() + j;
            var nextElement = this.board.getAt(nextX, nextY);
            if (!this.board.isOutOfField(nextX, nextY) && !obstacles.contains(nextElement))
                return resolveDirection(i, j);

            curDirIndex++;
            curDirIndex %= 4;
        }
        return Direction.STOP;
    }

    private Direction resolveDirection(int dx, int dy) {
        if (dx == -1) return Direction.LEFT;
        if (dx == 1) return Direction.RIGHT;
        if (dy == -1) return Direction.DOWN;
        if (dy == 1) return Direction.UP;
        return Direction.STOP;
    }

    private PointImpl resolveDirection(Direction dir)
    {
        switch (dir)
        {
            case LEFT: return new PointImpl(-1, 0);
            case RIGHT: return new PointImpl(1, 0);
            case UP: return new PointImpl(0, 1);
            case DOWN: return new PointImpl(0, -1);
            default: return new PointImpl(0, 0);
        }
    }

    private boolean needAct(Direction nextMove) {
        if (nextMove == Direction.STOP)
            return true;
        var dirPoint = resolveDirection(nextMove);
        var bullet = this.board.getMe();
        var tanks = new Vector<Elements>();
        tanks.add(Elements.OTHER_TANK_DOWN);
        tanks.add(Elements.OTHER_TANK_LEFT);
        tanks.add(Elements.OTHER_TANK_RIGHT);
        tanks.add(Elements.OTHER_TANK_UP);
        tanks.add(Elements.AI_TANK_DOWN);
        tanks.add(Elements.AI_TANK_LEFT);
        tanks.add(Elements.AI_TANK_RIGHT);
        tanks.add(Elements.AI_TANK_UP);
        while (true)
        {
            bullet = new PointImpl(
                    bullet.getX() + dirPoint.getX(),
                    bullet.getY() + dirPoint.getY());
            if (board.isOutOfField(bullet.getX(), bullet.getY())
                    || obstacles.contains(board.getAt(bullet)))
                break;
            var element = this.board.getAt(bullet);
            if (tanks.contains(element))
                return true;
        }
        return false;
    }

    private void getEnemy() {
        var me = board.getMe();
        var enemies = board.getEnemies();
        if (enemies.size() == 0)
        {
            curMoveTarget = null;
            return;
        }
        if (curMoveTarget != null) {
            for (var option : enemies) {
                if (Math.abs(option.getX() - curMoveTarget.getX()) + Math.abs(option.getY() - curMoveTarget.getY()) <= 1) {
                    curMoveTarget = option;
                    return;
                }
            }
        }
        var etalon = enemies.get(0);
        for (var e: enemies) {
            if (Math.abs(e.getX() - me.getX()) + Math.abs(e.getY() - me.getY()) <
                    Math.abs(etalon.getX() - me.getX()) + Math.abs(etalon.getY() - me.getY())) {
                etalon = e;
            }
        }
        curMoveTarget = etalon;
    }

    private Direction getPathTo(int fx, int fy)
    {
        var me = board.getMe();
        int sx = me.getX();
        int sy = me.getY();
        for (int i = 0; i < 43; ++i)
            b[i] = new int[43];

        var q = new LinkedList<PointImpl>();
        q.add(new PointImpl(sx, sy));
        b[sx][sy] = 1;
        while (q.size() > 0) {
            var cur = q.getFirst();
            int cx = cur.getX();
            int cy = cur.getY();
            q.removeFirst();


            if (cx == fx && cy == fy)
                break;

            for (var dir : order) {
                var d = resolveDirection(dir);
                int dx = d.getX();
                int dy = d.getY();
                int nx = cx + dx;
                int ny = cy + dy;
                var n = new PointImpl(nx, ny);
                if (board.isOutOfField(nx, ny))
                    continue;
                if (obstacles.contains(board.getAt(nx, ny)))
                    continue;
                if (b[nx][ny] > 0)
                    continue;;
                b[nx][ny] = b[cx][cy] + 1;
                q.addLast(n);
            }
        }

        if (b[fx][fy] == 0)
            return Direction.STOP;

        while (true) {
            for (var dir : order) {
                var d = resolveDirection(dir);
                int dx = d.getX();
                int dy = d.getY();
                int nx = fx + dx;
                int ny = fy + dy;
                if (board.isOutOfField(nx, ny))
                    continue;
                if (nx == sx && ny == sy)
                    return resolveDirection(-dx, -dy);
                if (b[nx][ny] + 1 == b[fx][fy])
                {
                    fx = nx;
                    fy = ny;
                    break;
                }
            }
        }

    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://algoritmix.dan-it.kiev.ua/codenjoy-contest/board/player/xvmnf5cgmyqtcoownxza?code=5922043541836404766",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
