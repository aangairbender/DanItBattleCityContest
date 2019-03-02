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
    private IPathFinder pathFinder;
    private IShooter shooter;
    private Vector<Elements> a = new Vector<>();

    public GameStrategy(Dice dice) {
        boardAnalyzer = new BoardAnalyzer();
        macroMapBuilder = new MacroMapBuilder();
        macroDecisionMaker = new MacroDecisionMaker();
        pathFinder = new PathFinder();
        shooter = new Shooter();
    }

    public String makeMove(Board board) {
        boardAnalyzer.processNext(board);

        var bestPos = macroDecisionMaker.findBestPosition(boardAnalyzer, macroMapBuilder);

        var shootDirection = shooter.chooseShootDirection(new Vector<>());

        var moveDirection = pathFinder.findPathTo(
                bestPos.getX(),
                bestPos.getY(),
                macroMapBuilder.BuildHeatMap(
                        boardAnalyzer.getBorderInfo(
                                boardAnalyzer.getCurrentTick()
                        )
                ),
                shootDirection);

        String result = moveDirection.toString();
        if (shootDirection == moveDirection)
            result += ", " + Direction.ACT.toString();

        return result;
    }
}
