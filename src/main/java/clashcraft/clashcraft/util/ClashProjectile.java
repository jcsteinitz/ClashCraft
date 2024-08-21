package clashcraft.clashcraft.util;

import clashcraft.clashcraft.ClashCraft;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ClashProjectile {
    private final Entity target;
    private final BukkitRunnable runnable;
    private final ArmorStand tracer;

    public ClashProjectile(Location location, Entity target, BukkitRunnable runnable) {
        this.target = target;
        this.runnable = runnable;

        this.tracer = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ARMOR_STAND);
        this.tracer.setGravity(false);

        shoot();
    }

    private void shoot() {
        // TODO KILL PROJECTILES WHEN MATCH ENDS/SERVER STOP
        new BukkitRunnable() {
            @Override
            public void run() {
                Vector direction = target.getLocation().toVector().subtract(tracer.getLocation().toVector()).normalize();
                Location newLocation = tracer.getLocation().add(direction.multiply(0.2)); // Adjust speed here
                tracer.teleport(newLocation);

                double yaw = Math.atan2(direction.getZ(), direction.getX()) * (180 / Math.PI) - 90;
                double pitch = Math.asin(direction.getY()) * (180 / Math.PI);
                tracer.setRotation((float) yaw, (float) pitch);

                if (tracer.getLocation().distance(target.getLocation()) <= 0.15) {
                    tracer.remove();
                    if (runnable != null) {
                        runnable.run();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(ClashCraft.getInstance(), 0L, 1L);
    }
}
