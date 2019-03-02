package com.codenjoy.dojo.battlecity.client.interfaces;

import com.codenjoy.dojo.services.Direction;

import java.util.Collection;

public interface IShooter {
    Direction chooseShootDirection(Collection<Direction> bannedDirection);
}
