package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IShooter;
import com.codenjoy.dojo.battlecity.client.model.BoardInfo;
import com.codenjoy.dojo.battlecity.client.utils.DirectionHelper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Collection;

public class Shooter implements IShooter {

    private BoardInfo info;

    public Shooter(BoardInfo info) {
        this.info = info;
    }

    public Direction chooseShootDirection() {
        var me = info.Board.getMe();
        var mx = me.getX();
        var my = me.getY();
        for (var tank : info.Tanks) {
            int tx = tank.x;
            int ty = tank.y;
            int dx = tx - mx;
            int dy = ty - my;
            if (dx * dy != 0)
                continue;
            if (dx != 0) dx /= Math.abs(dx);
            if (dy != 0) dy /= Math.abs(dy);

            var tankDirPoint = DirectionHelper.directionToPoint(tank.lookDirection);
            var tdx = tankDirPoint.getX();
            var tdy = tankDirPoint.getY();
            if (!tank.isMoving)
                tdx = tdy = 0;

            for (int t = 1; t < 100; ++t) {
                boolean ok = false;
                tx += tdx;
                ty += tdy;
                if (tx == mx && ty == my)
                    ok = true;
                mx += dx;
                my += dy;
                if (tx == mx && ty == my)
                    ok = true;
                mx += dx;
                my += dy;
                if (tx == mx && ty == my)
                    ok = true;
                if (ok)
                    return DirectionHelper.pointToDirection(new PointImpl(dx, dy));
            }
        }
        return Direction.STOP;
    }
}
