package clashcraft.clashcraft.mobs;

import clashcraft.clashcraft.util.ClashArrow;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Objects;

public class ClashSkeleton extends ClashMob {
    @Override
    public void setStats() {
        setRange(6.5);
        setFirstCooldown(10);
        setNormalCooldown(35);
    }

    @Override
    public void attack() {
        new ClashArrow(this, getDummy().getLocation().add(0,1,0), getTarget().getDummy(), null);
    }

    @Override
    public Entity spawnDummy() {
        Skeleton skeleton = (Skeleton) spawnMob(EntityType.SKELETON);
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
        if (meta != null) {
            meta.setColor(getPlayer().getColor());
            meta.setUnbreakable(true);
            leatherHelmet.setItemMeta(meta);
        }
        Objects.requireNonNull(skeleton.getEquipment()).setHelmet(leatherHelmet);
        Objects.requireNonNull(skeleton.getEquipment()).setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        Objects.requireNonNull(skeleton.getEquipment()).setItemInMainHand(new ItemStack(Material.CROSSBOW));
        return skeleton;
    }
}
