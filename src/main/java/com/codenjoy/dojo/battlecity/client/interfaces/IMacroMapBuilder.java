package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.battlecity.client.model.BoardInfo;

public interface IMacroMapBuilder {
    double[][] BuildHeatMap(BoardInfo boardInfo);
}
