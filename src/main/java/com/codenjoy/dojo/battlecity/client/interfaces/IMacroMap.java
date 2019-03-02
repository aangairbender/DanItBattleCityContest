package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.model.BoardInfo;

public interface IMacroMap {
    double[][] BuildHeatMap(BoardInfo boardInfo);
}
