package clashcraft.clashcraft.util;

import org.bukkit.Color;
import org.bukkit.entity.Entity;

import java.util.*;

public class ClashPlayer {
    private static final HashMap<Entity, ClashPlayer> clashPlayerMap = new HashMap<>();

    private final Entity entity;
    private ClashPlayer enemy;
    private Color color = Color.BLUE;

    public ClashPlayer(Entity entity) {
        clashPlayerMap.put(entity, this);
        PlacedManager.registerPlayer(this);
        this.entity = entity;
    }

    public void remove() {
        clashPlayerMap.remove(this.entity);
        PlacedManager.deregisterPlayer(this);
    }

    public static void remove(Entity entity) {
        locate(entity).remove();
    }

    public static ClashPlayer locate(Entity entity) {
        return clashPlayerMap.get(entity);
    }

    public static void nuke() {
        List<ClashPlayer> toRemove = new ArrayList<>(clashPlayerMap.values());
        for (ClashPlayer player : toRemove) {
            player.remove();
        }
    }

    public void setEnemy(ClashPlayer enemy) {
        this.enemy = enemy;
    }

    public ClashPlayer getEnemy() {
        return this.enemy;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public boolean isOnBlue() {
        return this.color == Color.BLUE;
    }
}