package clashcraft.clashcraft.util;

import clashcraft.clashcraft.ClashCraft;
import clashcraft.clashcraft.mobs.ClashMob;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ClashArrow extends ClashProjectile{
    private final ClashMob shooter;
    private final Entity target;
    private final Runnable runnable;
    private final Arrow arrow;

    public ClashArrow(ClashMob shooter, Location location, Entity target, Runnable runnable) {
        this.shooter = shooter;
        this.shooter.addProjectile(this);
        this.target = target;
        this.runnable = runnable;
        this.arrow = (Arrow) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ARROW);

        shoot();
    }

    private void shoot() {
        new BukkitRunnable() {
            @Override

            public void run() {
                if (arrow.getLocation().distance(target.getLocation().add(0,1,0)) <= 0.35) {
                    kill(true);
                    this.cancel();
                }

                // Calculate the direction vector towards the target
                Location arrowLocation = arrow.getLocation();
                Location targetLocation = target.getLocation().add(0,1,0);

                Vector direction = targetLocation.toVector().subtract(arrowLocation.toVector()).normalize();
                arrow.setVelocity(direction.multiply(0.25)); // Adjust speed by multiplying the direction
            }
        }.runTaskTimer(ClashCraft.getInstance(), 0L, 1L);
    }

    public void kill(boolean fire) {
        this.arrow.remove();
        this.shooter.removeProjectile(this);
        if (runnable != null && fire) {
            runnable.run();
        }
    }
}
