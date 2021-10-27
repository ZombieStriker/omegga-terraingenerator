package me.zombie_striker.terraingenerator;

import com.kmschr.brs.*;
import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.RPCResponse;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TerrainManager {

    private List<ChunkCoords> loadedChunks = new LinkedList<>();

    private long seed;
    private long tree1percent=1;
    private long bush1percent=1;


    private RPCResponse terrainblock = JOmegga.readSaveData("terrainmetallic1");
    private RPCResponse tree1 = JOmegga.readSaveData("tree1");
    private RPCResponse bush1 = JOmegga.readSaveData("bush1");

    private OpenSimplexNoise noise;

    private List<String> materials = Arrays.asList("BMC_Plastic", "BMC_Glow");

    public TerrainManager() {

    }

    public void registerChunk(int x, int y, User owner) {
        loadedChunks.add(new ChunkCoords(x, y, owner));
    }

    public void removeChunk(int x, int y) {
        for (ChunkCoords c : new LinkedList<>(loadedChunks)) {
            if (c.getX() == x && c.getY() == y) {
                JOmegga.clearBricks(c.getOwner().getId().toString(), true);
                loadedChunks.remove(c);
                break;
            }
        }

    }

    public void removeChunk(ChunkCoords c) {
        JOmegga.clearBricks(c.getOwner().getId().toString(), true);
        loadedChunks.remove(c);
    }


    public int getSinSeed(double x, double y) {
        double xSin = Math.sin((y) / 47);
        double ySin = Math.cos((x) / 47);

        return ((int) ((xSin * ySin))) + 1;
    }

    public double getNoise(double x, double y, double scale, double scale2) {
        if (noise == null) {
            noise = new OpenSimplexNoise();
            seed = (long) JOmegga.getConfig().get("seed");
            bush1percent= (long) JOmegga.getConfig().get("bush1-percent");
            tree1percent= (long) JOmegga.getConfig().get("tree1-percent");
            JOmegga.log("Loading seed : "+seed);
        }
        double n = (noise.eval((x) / scale, y / scale,seed) + 1) * 3;
        double n2 = (noise.eval(y / scale, (x) / scale, n) + 1) * scale2;

        return n2;

    }

    public int getHeight(int x, int y, boolean averageNearby) {
        double waterlevel = Math.max(0, getNoise(x, y, 400, 1) - 1);
        double hb = getNoise(x, y, 400, 200);
        double h1 = getNoise(x, y, 100, 200);
        double h2 = getNoise(x, y, 10, 264);
        double h2m = getNoise(x, y, 100, 1);
        double hm1 = getNoise(x, y, 366, 1);
        double height = (h1 + (h2 * h2m) + hb);

        double sigwater = (sigmoid(1.0 / waterlevel) + 1) / 2;
        double height2 = sigwater * (waterlevel * waterlevel);
        height = (1.0 / sigmoid(height2)) * height * height2;

        height *= 1.0 / (hm1 * hm1);

        height *= 5;

        if(averageNearby){
            int ah1 = getHeight(x,y+1,false);
            int ah2 = getHeight(x,y-1,false);
            int ah3 = getHeight(x+1,y,false);
            int ah4 = getHeight(x-1,y,false);

            height = (height+ah1+ah2+ah3+ah4)/5;
        }

        height = (int) (height / 4);
        height *= 4;
        height = Math.max(height, 0);



        return (int) height;
    }

    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public void generate(int x1, int y1) {
        try {
            if (terrainblock.getReturnValue() == null) {
                return;
            }

            User owner = BrickAuthorLookup.getOwnerFor(x1, y1);

            SaveData saveData = ((SaveData) terrainblock.getReturnValue()).clone();
            Brick brick = saveData.getBricks().get(0);
            saveData.setMaterials(materials);
            List<Brick> newbricks = saveData.getBricks();
            newbricks.clear();

            saveData.setAuthor(owner);
            saveData.setBrickOwners(Arrays.asList(owner));

            BrickManager.tile(this, brick, x1, y1, newbricks);

            for (int x = x1 * 24; x < 24 + (x1 * 24); x++) {
                for (int y = y1 * 24; y < 24 + (y1 * 24); y++) {

                    int height = getHeight(x, y, true);
                    BrickManager.brick(this, brick, x, y, height, newbricks);

                    if (Math.random() *100 < tree1percent) {
                        BrickManager.object(this, tree1, x, y, height, newbricks);
                    } else if (Math.random()*100 < bush1percent) {
                        BrickManager.object(this, bush1, x, y, height, newbricks);
                    }
                }
            }
            saveData.setBricks(newbricks);
            JOmegga.loadSaveData(saveData.toRPCData(), 0, 0, 100, true);
            registerChunk(x1, y1, owner);
        } catch (Exception e34) {
            for (StackTraceElement s : e34.getStackTrace()) {
                JOmegga.log(s.getFileName() + " " + s.getMethodName() + " " + s.getLineNumber());
            }
        }
    }

    public List<ChunkCoords> getChunks() {
        return loadedChunks;
    }

    public boolean isChunkLoaded(int x, int y) {
        for (ChunkCoords c : loadedChunks) {
            if (c.getX() == x && c.getY() == y)
                return true;
        }
        return false;
    }
}
