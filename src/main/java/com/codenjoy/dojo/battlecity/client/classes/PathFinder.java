package com.codenjoy.dojo.battlecity.client.classes;

import com.codenjoy.dojo.battlecity.client.interfaces.IPathFinder;
import com.codenjoy.dojo.battlecity.client.model.Board;
import com.codenjoy.dojo.battlecity.client.model.BoardInfo;
import com.codenjoy.dojo.battlecity.client.utils.DirectionHelper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.vfs.Vfs;

import java.util.*;

public class PathFinder implements IPathFinder {

    private int sz;
    private BoardInfo info;
    private Board board;
    private int[][] b;
    int mx, my;

    private Direction[][] pCache;


    public PathFinder(BoardInfo info) {
        this.info = info;
        board = info.Board;
        mx = board.getMe().getX();
        my = board.getMe().getY();
        sz = board.size();
        b = new int[sz][sz];
        for (int i = 0; i < sz;++i)
            b[i] = new int[sz];
        pCache = new Direction[sz][sz];
        for (int i = 0; i < sz;++i) {
            pCache[i] = new Direction[sz];
            for (int j = 0; j < sz; ++j)
                pCache[i][j] = Direction.ACT;
        }
        bfs();
    }

    public void bfs() {
        var me = board.getMe();
        int sx = me.getX();
        int sy = me.getY();

        var q = new LinkedList<PointImpl>();
        q.add(new PointImpl(sx, sy));
        b[sx][sy] = 1;
        while (q.size() > 0) {
            var cur = q.getFirst();
            int cx = cur.getX();
            int cy = cur.getY();
            q.removeFirst();

            for (var dir : DirectionHelper.getBasicDirections()) {
                var d = DirectionHelper.directionToPoint(dir);
                int dx = d.getX();
                int dy = d.getY();
                int nx = cx + dx;
                int ny = cy + dy;
                var n = new PointImpl(nx, ny);
                if (board.isOutOfField(nx, ny))
                    continue;
                if (!board.getAt(nx, ny).isFree() || board.getAt(nx, ny).isBullet())
                    continue;

                if (info.Bullets.stream().anyMatch(e ->
                        (e.x + DirectionHelper.getX(e.dir) == nx && e.y + DirectionHelper.getY(e.dir) == ny)
                        || (e.x + DirectionHelper.getX(e.dir) * 2 == nx && e.y + DirectionHelper.getY(e.dir) * 2 == ny)))
                    continue;

                if (b[nx][ny] > 0)
                    continue;
                b[nx][ny] = b[cx][cy] + 1;
                q.addLast(n);
            }
        }
    }

    public Pair<Direction, Double> findPathTo(int fx, int fy, Direction needForShot)
    {

        if (b[fx][fy] == 0)
            return Pair.of(Direction.STOP, 0.0);

        var value = b[fx][fy];

        var dir = fastPathTo(fx, fy);

        return Pair.of(dir, value + 0.0);
    }

    private Direction fastPathTo(int fx, int fy) {
        if (pCache[fx][fy] != Direction.ACT)
            return pCache[fx][fy];
        for (var dir : DirectionHelper.getBasicDirections()) {
            var d = DirectionHelper.directionToPoint(dir);
            int dx = d.getX();
            int dy = d.getY();
            int nx = fx + dx;
            int ny = fy + dy;
            if (board.isOutOfField(nx, ny))
                continue;
            if (nx == mx && ny == my)
                return pCache[fx][fy] = DirectionHelper.pointToDirection(new PointImpl(-dx, -dy));
            if (b[nx][ny] + 1 == b[fx][fy])
            {
                return pCache[fx][fy] = fastPathTo(nx, ny);
            }
        }
        return pCache[fx][fy] = Direction.STOP;
    }

}
