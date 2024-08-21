package clashcraft.clashcraft.util;

import clashcraft.clashcraft.mobs.ClashMob;

import java.util.ArrayList;
import java.util.HashMap;

public class PlacedManager {
    private static final HashMap<ClashPlayer, ArrayList<ClashMob>> placedMap = new HashMap<>();

    public static void registerPlayer(ClashPlayer player) {
        placedMap.put(player, new ArrayList<>());
    }

    public static void deregisterPlayer(ClashPlayer player) {
        killMobs(player);
        placedMap.remove(player);
    }

    public static ArrayList<ClashMob> getPlaced(ClashPlayer player) {
        return placedMap.get(player);
    }

    public static void addToPlayer(ClashPlayer player, ClashMob mob) {
        placedMap.get(player).add(mob);
    }

    public static void removeFromPlayer(ClashPlayer player, ClashMob mob) {
        if (placedMap.containsKey(player)) {
            placedMap.get(player).remove(mob);
        }
    }

    public static void killMobs(ClashPlayer player) {
        if (placedMap.get(player) != null) {
            for (ClashMob mob : placedMap.get(player)) {
                mob.kill();
            }
        }
    }
}
