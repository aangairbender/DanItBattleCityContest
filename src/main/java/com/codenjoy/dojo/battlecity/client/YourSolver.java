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

import com.codenjoy.dojo.battlecity.client.classes.GameStrategy;
import com.codenjoy.dojo.battlecity.client.interfaces.IGameStrategy;
import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.util.*;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private IGameStrategy gameStrategy;
    private Dice dice;

    public YourSolver(Dice dice) {
        gameStrategy = new GameStrategy(dice);
    }

    @Override
    public String get(Board board)
    {
        try {
            return gameStrategy.makeMove(board);
        } catch (Exception e) {
            e.printStackTrace();
            return Direction.STOP.toString();
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
