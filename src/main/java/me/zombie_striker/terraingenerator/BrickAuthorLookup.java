package me.zombie_striker.terraingenerator;

import com.kmschr.brs.User;

import java.util.UUID;

public class BrickAuthorLookup {


    public static User getOwnerFor(int x, int y){
        UUID uuid = new UUID(x,y);
        User user = new User(uuid,x+"."+y);
        return user;
    }
}
