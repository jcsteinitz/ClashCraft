package clashcraft.clashcraft.util;

import clashcraft.clashcraft.ClashCraft;
import clashcraft.clashcraft.mobs.ClashMob;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ClashProjectile {
    private final ClashMob shooter;
    private final Entity target;
    private final BukkitRunnable runnable;
    private final ArmorStand tracer;

    public ClashProjectile(ClashMob shooter, Location location, Entity target, BukkitRunnable runnable) {
        this.shooter = shooter;
        this.shooter.addProjectile(this);
        this.target = target;
        this.runnable = runnable;

        this.tracer = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ARMOR_STAND);
        this.tracer.setGravity(false);

        shoot();
    }

    private void shoot() {
        ClashProjectile projectile = this;
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
                    kill(true);
                    this.cancel();
                }
            }
        }.runTaskTimer(ClashCraft.getInstance(), 0L, 1L);
    }

    public void kill(boolean fire) {
        this.tracer.remove();
        this.shooter.removeProjectile(this);
        if (runnable != null && fire) {
            runnable.run();
        }
    }
}
