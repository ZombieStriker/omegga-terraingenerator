package me.zombie_striker.terraingenerator;

import me.zombie_striker.omeggajava.JOmegga;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

    protected static TerrainManager manager;


    public static void main(String... args) {
        JOmegga.registerListener(new TerrainListener());
        JOmegga.init("TerrainGenerator");
        try {
            loadAsset("terrainmetallic1");
            loadAsset("tree1");
            loadAsset("bush1");
        } catch (Exception e4) {for(StackTraceElement s : e4.getStackTrace()){
            JOmegga.log(s.getClassName()+" "+s.getMethodName()+" "+s.getLineNumber());
        }
        }
        manager = new TerrainManager();
        JOmegga.broadcast("Starting terrain generator");
        while (JOmegga.isRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void loadAsset(String asset){
        File outputDir = new File(JOmegga.getOmeggaDir(), "/data/Saved/Builds/");
        File output = new File(outputDir, asset+".brs");
        if (!output.exists()) {
            File input = new File(JOmegga.getJOmeggaJar().getParentFile(), asset+".brs");
            if (input.exists()) {
                try {
                    Files.copy(input.toPath(), output.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOmegga.log("Saved "+asset);
            }
        }
    }
}
