package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.client.model.BoardInfo;

public interface IBoardAnalyzer {

    BoardInfo getBoardInfo();
    int getCurrentTick();

    void processNext(Board board);
}
