package me.zombie_striker.terraingenerator;

public class SprialXY {

    public static int[] getXYForSpiral(int seq) {
        int directionx = 0;
        int directiony = -1;

        int x = 0;
        int y = 0;

        if(seq==0)
            return new int[]{x,y};

        int center = (int) (Math.sqrt(seq+1) + 1) / 2;
        boolean[][] conquered = new boolean[(center * 2)+1][(center * 2)+1];
        conquered[center][center]=true;

        for (int i = 0; i < seq; i++) {
            if (directiony == 1) {
                y++;
                conquered[center+x][center+y]=true;
                if(!conquered[center+x+1][center+y]){
                    directionx=1;
                    directiony=0;
                }
            } else if (directiony == -1) {
                y--;
                conquered[center+x][center+y]=true;
                if(!conquered[center+x-1][center+y]){
                    directionx=-1;
                    directiony=0;
                }
            } else if (directionx == 1) {
                x++;
                conquered[center+x][center+y]=true;
                if(!conquered[center+x][center+y-1]){
                    directionx=0;
                    directiony=-1;
                }
            } else if (directionx == -1) {
                x--;
                conquered[center+x][center+y]=true;
                if(!conquered[center+x][center+y+1]){
                    directionx=0;
                    directiony=1;
                }
            }
        }
        return new int[]{x,y};
    }
}
