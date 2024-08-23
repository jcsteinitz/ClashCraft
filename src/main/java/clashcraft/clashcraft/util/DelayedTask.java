package clashcraft.clashcraft.util;

import clashcraft.clashcraft.ClashCraft;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayedTask {
    public DelayedTask(Runnable runnable, int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(ClashCraft.getInstance(), delay);
    }
}
