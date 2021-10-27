package me.zombie_striker.terraingenerator;

import com.kmschr.brs.User;

import java.util.Objects;

public class ChunkCoords {

    private int x;
    private int y;
    private User owner;

    ChunkCoords(int x, int y, User owner){
        this.x= x;
        this.y=y ;
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
