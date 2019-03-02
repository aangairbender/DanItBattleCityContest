package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.services.Point;

public interface IGameStrategy {
    String makeMove(Board board);
}
