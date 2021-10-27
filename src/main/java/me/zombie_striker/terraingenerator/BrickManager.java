package me.zombie_striker.terraingenerator;

import com.kmschr.brs.Brick;
import com.kmschr.brs.ColorMode;
import com.kmschr.brs.SaveData;
import com.kmschr.brs.Vec3;
import me.zombie_striker.omeggajava.RPCResponse;

import java.util.List;

public class BrickManager {

    private static final int BEACH_HEIGHT = 100+64;

    public static void brick(TerrainManager manager, Brick brick, int x, int y, double height, List<Brick> bricklist) {
        Brick brick2 = brick.clone();
        brick2.setMaterialIndex(0);
        if (height <= BEACH_HEIGHT) {
            brick2.setColor(new ColorMode(42));
        } else {
            brick2.setColor(new ColorMode(67));
        }

        int height0 = (int) height;//manager.getHeight(x, y,false);
        int height1 = manager.getHeight(x, y + 1,true);
        int height2 = manager.getHeight(x, y - 1,true);
        int height3 = manager.getHeight(x + 1, y,true);
        int height4 = manager.getHeight(x - 1, y,true);

        int maxDif = 0;
        maxDif = (int) Math.max(maxDif, height0 - height1);
        maxDif = (int) Math.max(maxDif, height0 - height2);
        maxDif = (int) Math.max(maxDif, height0 - height3);
        maxDif = (int) Math.max(maxDif, height0 - height4);

        if (maxDif >= 12) {
            if (height > 4) {
                Brick brick3 = brick.clone();
                brick3.setColor(new ColorMode(24));
                int size = maxDif;
                brick3.setSize(new Vec3(20, 20, size / 2));
                brick3.setPosition(x * 40, y * 40, (int) (height - (Math.min(3, maxDif / 2)) - (size / 2) - 6));
                bricklist.add(brick3);

                brick2.setPosition(x * 40, y * 40, (int) height - (Math.min(3, maxDif / 2)));
                brick2.setSize(new Vec3(20, 20, Math.min(maxDif, 6)));
                bricklist.add(brick2);
            }

        } else {
            if (height > 4) {
                brick2.setPosition(x * 40, y * 40, (int) height - 3);
                brick2.setSize(new Vec3(20, 20, 6));
                bricklist.add(brick2);
            }
        }
    }

    public static void tile(TerrainManager terrainManager, Brick brick, int x1, int y1, List<Brick> newbricks) {
        Brick brick2 = brick.clone();
        brick2.setPosition((x1 * 960) + (480) - 20, (y1 * 960) + (480) - 20, -4);
        brick2.setSize(new Vec3(480, 480, 2));
        brick2.setColor(new ColorMode(41));
        newbricks.add(brick2);
    }

    public static void object(TerrainManager terrainManager, RPCResponse tree1, int x, int y, int height, List<Brick> newbricks) {
        if (tree1.getReturnValue() == null) {
            return;
        }
        if(height <= BEACH_HEIGHT)
            return;
        SaveData tree1save = ((SaveData) tree1.getReturnValue());

        int xoff = (x * 40);
        int yoff = (y * 40);

        for (Brick brock : tree1save.getBricks()) {
            Brick brick1 = brock.clone();
            brick1.setPosition(brick1.getPosition().x + xoff, brick1.getPosition().y + yoff, +(height) +3 + brick1.getPosition().z);
            newbricks.add(brick1);
        }

    }
}
