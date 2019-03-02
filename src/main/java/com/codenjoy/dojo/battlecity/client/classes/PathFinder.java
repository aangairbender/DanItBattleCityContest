package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IPathFinder;
import com.codenjoy.dojo.battlecity.client.utils.DirectionHelper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.vfs.Vfs;

import java.util.*;

public class PathFinder implements IPathFinder {

    private int mx, my;
    private int sz;
    private double[][] heatMap;

    public PathFinder(int myX, int myY, double[][] heatMap, int size) {
        mx = myX;
        my = myY;
        this.heatMap = heatMap;
        sz = size;
    }

    public Pair<Direction, Double> findPathTo(int x, int y, Direction needForShot) {
        Map<PointImpl, Double> d = new HashMap<>();
        Map<PointImpl, PointImpl> p = new HashMap<>();
        Set<PointImpl> u = new HashSet<>();

        var s = new PointImpl(mx, my);
        d.put(s, 0.0);
        for (int i = 0; i < sz * sz; ++i){
            if (d.isEmpty())
                break;

            Map.Entry<PointImpl, Double> best = null;
            for (Map.Entry<PointImpl, Double> entry : d.entrySet()) {
                if (best == null || best.getValue() < entry.getValue()) {
                    best = entry;
                }
            }
            var v = best.getKey();

            u.add(v);

            for (var dir : DirectionHelper.getBasicDirections()) {
                var dirPoint = DirectionHelper.directionToPoint(dir);
                var dx = dirPoint.getX();
                var dy = dirPoint.getY();
                var nx = v.getX() + dx;
                var ny = v.getY() + dy;
                var n = new PointImpl(nx, ny);
                if (nx < 0 || ny < 0 || nx >= sz || ny >= sz)
                    continue;
                var cost = heatMap[nx][ny];
                if (d.get(v) + cost > d.getOrDefault(n, -1e18)){
                    d.put(n, d.get(v) + cost);
                    p.put(n, v);
                }
            }

        }

        PointImpl f = new PointImpl(x, y);
        if (!d.containsKey(f))
            return Pair.of(needForShot, 0.0);

        var value = d.get(f);

        while (p.get(f) != s) {
            f = p.get(f);
        }

        return Pair.of(
                DirectionHelper.pointToDirection(new PointImpl(
                    f.getX() - s.getX(),
                    f.getY() - s.getY())
                ),
                value
        );
    }
}
