package clashcraft.clashcraft;

import clashcraft.clashcraft.commands.Test;
import clashcraft.clashcraft.util.ClashPlayer;
import clashcraft.clashcraft.util.PlayerHandler;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ClashCraft extends JavaPlugin {
    private static ClashCraft instance;

    @Override
    public void onEnable() {
        setGameRule(GameRule.DO_MOB_SPAWNING, false);
        setGameRule(GameRule.DO_MOB_LOOT, false);
        instance = this;

        // Commands
        Objects.requireNonNull(this.getCommand("test")).setExecutor(new Test());

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerHandler(), this);

        log("ClashCraft enabled!");
    }

    @Override
    public void onDisable() {
        ClashPlayer.nuke();
        log("ClashCraft disabled!");
    }

    public static ClashCraft getInstance() {
        return instance;
    }

    public static void log(String msg) {
        instance.getLogger().info(msg);
    }

    private void setGameRule(GameRule<Boolean> rule, boolean value) {
        getServer().getWorlds().getFirst().setGameRule(rule, value);
    }
}
