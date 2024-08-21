package clashcraft.clashcraft.commands;

import clashcraft.clashcraft.mobs.ClashSkeleton;
import clashcraft.clashcraft.util.ClashPlayer;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        commandSender.sendMessage("Executing test command.");

        Entity armorstand = Objects.requireNonNull(((Player) commandSender).getLocation().getWorld()).spawnEntity(((Player) commandSender).getLocation().add(0,0,10), EntityType.ARMOR_STAND);
        ClashPlayer npc = new ClashPlayer(armorstand);
        npc.setColor(Color.RED);
        Player sender = (Player) commandSender;

        ClashSkeleton skeleton = new ClashSkeleton();
        skeleton.spawn(sender.getLocation(), ClashPlayer.locate(sender), npc);

        ClashSkeleton skeleton2 = new ClashSkeleton();
        skeleton2.spawn(npc.getEntity().getLocation(), npc, ClashPlayer.locate(sender));


        return true;
    }
}
