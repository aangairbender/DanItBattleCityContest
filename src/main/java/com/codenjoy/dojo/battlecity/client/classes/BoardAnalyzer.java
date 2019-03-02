package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IBoardAnalyzer;
import com.codenjoy.dojo.battlecity.client.model.*;
import com.codenjoy.dojo.battlecity.client.utils.DirectionHelper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class BoardAnalyzer implements IBoardAnalyzer {

    private int currentTick;
    private LinkedList<Board> history = new LinkedList<>();
    private Board board;

    private Collection<Bullet> bullets = new Vector<>();
    private Collection<Tank> tanks = new Vector<>();
    private CellInfo[][] cells = null;

    public int getCurrentTick() {
        return currentTick;
    }

    public Board getCurrentBoard() {
        return history.getFirst();
    }

    public BoardInfo getBoardInfo(int tick) {
        var info = new BoardInfo();
        info.Board = board;
        info.Bullets = bullets;
        info.Tanks = tanks;
        info.Tick = tick;
        info.Size = board.size();
        info.Cells = cells;
        return info;
    }

    public void processNext(Board board) {
        currentTick++;
        this.board = board;
        history.addFirst(board);
        if (history.size() > 10)
            history.removeLast();

        updateTanks();
        updateBullets();
        updateCells();
    }

    public BoardAnalyzer() {
        currentTick = 0;
    }

    private void updateBullets() {
        moveBullets();

        var newBullets = getCurrentBoard().getBullets();
        for (var oldBullet : bullets) {
            newBullets.removeIf(e -> e.getX() == oldBullet.x && e.getY() == oldBullet.y);
        }

        for (var newBullet : newBullets) {
            var tank = tanks.stream()
                    .filter(e -> Board.distance(e.x, e.y, newBullet.getX(), newBullet.getY()) == 1)
                    .findAny();
            bullets.add(new Bullet(newBullet.getX(), newBullet.getY(),
                    tank.isPresent() ? tank.get().lookDirection : Direction.UP));
        }
    }

    private void moveBullets() {
        var deadBullets = new Vector<Bullet>();
        for (var b : bullets) {
            var pointDir = DirectionHelper.directionToPoint(b.dir);
            b.x += pointDir.getX();
            b.y += pointDir.getY();
            if (!board.isFreeAt(b.x, b.y))
            {
                deadBullets.add(b);
                continue;
            }
            b.x += pointDir.getX();
            b.y += pointDir.getY();
        }
        bullets.removeAll(deadBullets);
    }

    private void updateTanks() {
        var graph = new ObjectGraph<Tank, Point>();

        var newTanks = board.getEnemies();
        for (var oldTank : tanks) {
            var candidates = newTanks.stream()
                    .filter(e -> Board.distance(e.getX(), e.getY(), oldTank.x, oldTank.y) <= 1)
                    .collect(toList());
            for (var candidate : candidates) {
                graph.addEdge(oldTank, candidate);
            }
        }

        var pairs = graph.findPairs();
        for (var pair : pairs) {
            var tank = pair.getLeft();
            var newPos = pair.getRight();

            tank.x = newPos.getX();
            tank.y = newPos.getY();
            tank.lookDirection = board.getDirectionOf(tank.x, tank.y);
        }
    }

    private void updateCells() {
        if (cells == null) {
            int sz = board.size();
            cells = new CellInfo[sz][];
            for (int i = 0; i < sz; ++i) {
                cells[i] = new CellInfo[sz];
                for (int j = 0; j < sz; ++j)
                    cells[i][j] = new CellInfo(i, j);
            }
        }

        for (var tank : tanks) {
            cells[tank.x][tank.y].timesVisited++;
        }
    }

    private class ObjectGraph<L, R> {

        private Map<L, Collection<R>> g = new HashMap<>();
        private Set<L> used = new HashSet<>();
        private Map<R, L> mt = new HashMap<>();

        private Set<L> left = new HashSet<>();
        private Set<R> right = new HashSet<>();

        public void addEdge(L a, R b) {
            if (!g.containsKey(a))
                g.put(a, new Vector<>());
            g.get(a).add(b);
            left.add(a);
            right.add(b);
        }

        private boolean tryKuhn(L v) {
            if (used.contains(v))
                return false;
            used.add(v);
            for (var to : g.get(v)) {
                if (!mt.containsKey(to) || tryKuhn(mt.get(to))) {
                    mt.put(to, v);
                    return true;
                }
            }
            return false;
        }

        public Collection<Pair<L, R>> findPairs() {
            mt.clear();
            for (var v : left) {
                used.clear();
                tryKuhn(v);
            }

            Collection<Pair<L, R>> pairs = new Vector<>();
            for (var to : right) {
                if (mt.containsKey(to))
                    pairs.add(new MutablePair<>(mt.get(to), to));
            }
            return pairs;
        }

    }
}
