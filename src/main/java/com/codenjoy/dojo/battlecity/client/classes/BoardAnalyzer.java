package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IBoardAnalyzer;
import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.client.model.BoardInfo;
import com.codenjoy.dojo.battlecity.client.model.Bullet;
import com.codenjoy.dojo.services.Direction;

import java.util.*;

public class BoardAnalyzer implements IBoardAnalyzer {

    private int currentTick;
    private LinkedList<Board> history = new LinkedList<>();

    private Collection<Bullet> bullets = new Vector<>();
    private Map<Bullet, Collection<Direction>> bulletDirectionCandidates = new HashMap<>();

    public int getCurrentTick() {
        return currentTick;
    }

    public BoardInfo getBorderInfo(int tick) {
        return null;
    }

    public void processNext(Board board) {
        currentTick++;
        history.addFirst(board);
        updateBullets();
        updateTanks();
    }

    public BoardAnalyzer() {
        currentTick = 0;
    }

    private void updateBullets() {

    }

    private void updateTanks() {

    }
}
