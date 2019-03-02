package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.Board;
import com.codenjoy.dojo.battlecity.client.BoardInfo;

public interface IBoardAnalyzer {

    BoardInfo getBorderInfo(int tick);

    void processNext(Board board);
}
