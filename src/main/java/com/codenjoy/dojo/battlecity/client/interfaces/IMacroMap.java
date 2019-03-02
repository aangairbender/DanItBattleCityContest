package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.BoardInfo;

public interface IMacroMap {
    double[][] BuildHeatMap(BoardInfo boardInfo);
}
