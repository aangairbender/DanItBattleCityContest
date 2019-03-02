package com.codenjoy.dojo.battlecity.client.model;

import com.codenjoy.dojo.services.Direction;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public class BoardInfo {
    public int Size;
    public int Tick;
    public Collection<Bullet> Bullets;
    public Collection<Tank> Tanks;
    public CellInfo[][] Cells;
    public Board Board;
}
