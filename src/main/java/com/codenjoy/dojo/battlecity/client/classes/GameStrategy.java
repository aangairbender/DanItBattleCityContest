package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.*;
import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Collection;
import java.util.Vector;

public class GameStrategy implements IGameStrategy {

    private IBoardAnalyzer boardAnalyzer;
    private IMacroMapBuilder macroMapBuilder;
    private IMacroDecisionMaker macroDecisionMaker;
    private Vector<Elements> a = new Vector<>();

    public GameStrategy(Dice dice) {
        boardAnalyzer = new BoardAnalyzer();
        macroMapBuilder = new MacroMapBuilder();
        macroDecisionMaker = new MacroDecisionMaker();
    }

    public String makeMove(Board board) {
        boardAnalyzer.processNext(board);
        var boardInfo = boardAnalyzer.getBoardInfo();
        var heatMap = macroMapBuilder.BuildHeatMap(boardInfo);

        var pathFinder = new PathFinder(boardInfo);

        var bestPos = macroDecisionMaker.findBestPosition(pathFinder, heatMap, board.size());

        var shooter = new Shooter(boardInfo);
        var shootDirection = shooter.chooseShootDirection();

        var moveDirection = pathFinder.findPathTo(
                bestPos.getX(),
                bestPos.getY(),
                shootDirection).getLeft();

        String result = moveDirection.toString();
        if (shootDirection != Direction.STOP && shootDirection == moveDirection)
            result += ", " + Direction.ACT.toString();

        return result;
    }
}
