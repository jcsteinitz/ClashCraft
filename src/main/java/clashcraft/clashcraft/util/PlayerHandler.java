package clashcraft.clashcraft.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        new ClashPlayer(event.getPlayer());
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        ClashPlayer.remove(event.getPlayer());
    }

    // To remove training buddies when they die
    @EventHandler
    void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            if (ClashPlayer.locate(event.getEntity()) != null) {
                ClashPlayer.remove(event.getEntity());
            }
        }
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent event) {
        if (event.getDamage() != 9999 && (event.getCause() != EntityDamageEvent.DamageCause.KILL && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK)) {
            event.setCancelled(true);
        }
    }
}
