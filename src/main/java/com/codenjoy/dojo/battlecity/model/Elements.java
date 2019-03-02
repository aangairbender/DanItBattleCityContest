package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    NONE(' '),
    BATTLE_WALL('☼'),
    BANG('Ѡ'),

    CONSTRUCTION('╬', 3),

    CONSTRUCTION_DESTROYED_DOWN('╩', 2),
    CONSTRUCTION_DESTROYED_UP('╦', 2),
    CONSTRUCTION_DESTROYED_LEFT('╠', 2),
    CONSTRUCTION_DESTROYED_RIGHT('╣', 2),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨', 1),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥', 1),
    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞', 1),
    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡', 1),

    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│', 1),
    CONSTRUCTION_DESTROYED_UP_DOWN('─', 1),

    CONSTRUCTION_DESTROYED_UP_LEFT('┌', 1),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐', 1),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└', 1),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘', 1),

    CONSTRUCTION_DESTROYED(' ', 0),

    BULLET('•'),

    TANK_UP('▲'),
    TANK_RIGHT('►'),
    TANK_DOWN('▼'),
    TANK_LEFT('◄'),

    OTHER_TANK_UP('˄'),
    OTHER_TANK_RIGHT('˃'),
    OTHER_TANK_DOWN('˅'),
    OTHER_TANK_LEFT('˂'),

    AI_TANK_UP('?'),
    AI_TANK_RIGHT('»'),
    AI_TANK_DOWN('¿'),
    AI_TANK_LEFT('«');

    public final char ch;
    int power;

    public boolean isFree() {
        return this == NONE || this == BANG  || this == CONSTRUCTION_DESTROYED;
    }

    private static List<Elements> constructionsCache = null;
    public static Collection<Elements> getConstructions() {
        if (constructionsCache == null) {
            constructionsCache = Arrays.stream(values())
                    .filter(e -> e.name().startsWith(CONSTRUCTION.name()))
                    .collect(toList());
        }
        return constructionsCache;
    }
    public boolean isConstruction() {
        return getConstructions().contains(this);
    }

    private static List<Elements> aiEnemiesCache = null;
    public static Collection<Elements> getAIEnemies() {
        if (aiEnemiesCache == null) {
            aiEnemiesCache = Arrays.stream(values())
                    .filter(e -> e.name().startsWith("AI_TANK"))
                    .collect(toList());
        }
        return aiEnemiesCache;
    }
    public boolean isAIEnemy() {
        return getAIEnemies().contains(this);
    }

    private static List<Elements> playerEnemiesCache = null;
    public static Collection<Elements> getPlayerEnemies() {
        if (playerEnemiesCache == null) {
            playerEnemiesCache = Arrays.stream(values())
                    .filter(e -> e.name().startsWith("OTHER_TANK"))
                    .collect(toList());
        }
        return playerEnemiesCache;
    }
    public boolean isPlayerEnemy() {
        return getPlayerEnemies().contains(this);
    }

    private static List<Elements> enemiesCache = null;
    public static Collection<Elements> getEnemies() {
        if (enemiesCache == null) {
            enemiesCache = new ArrayList<>(getAIEnemies());
            enemiesCache.addAll(getPlayerEnemies());
        }
        return enemiesCache;
    }
    public boolean isEnemy() {
        return getEnemies().contains(this);
    }

    public boolean isBullet() {
        return this == Elements.BULLET;
    }

    public Direction getDirection() {
        if (this.name().endsWith("_UP")) return Direction.UP;
        if (this.name().endsWith("_RIGHT")) return Direction.RIGHT;
        if (this.name().endsWith("_DOWN")) return Direction.DOWN;
        if (this.name().endsWith("_LEFT")) return Direction.LEFT;
        return Direction.STOP;
    }

    @Override
    public char ch() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
        this.power = -1;
    }

    Elements(char ch, int power) {
        this.ch = ch;
        this.power = power;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }
}
