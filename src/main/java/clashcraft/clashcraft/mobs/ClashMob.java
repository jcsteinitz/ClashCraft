package clashcraft.clashcraft.mobs;

import clashcraft.clashcraft.ClashCraft;
import clashcraft.clashcraft.util.ClashPlayer;
import clashcraft.clashcraft.util.PlacedManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public abstract class ClashMob {
    private Location originalLocation;
    private ClashPlayer player;
    private ClashPlayer enemy;
    private Zombie finder;
    private Entity dummy;
    private ClashMob target;

    private int dummyHeight = 5;
    private double speed = 0.25;
    private double range;

    private int firstCooldown;
    private int normalCooldown;
    private int currentCooldown;

    public ClashMob() {
        setStats();
    }

    public void spawn(Location location, ClashPlayer player, ClashPlayer enemy) {
        PlacedManager.addToPlayer(player, this);
        location.setPitch(0);
        if (!(player.isOnBlue())) {
            location.setYaw(-180);
        }
        this.originalLocation = location;
        this.player = player;
        this.enemy = enemy;
        this.finder = spawnFinder();
        this.dummy = spawnDummy();

        if (this.firstCooldown == 0) {
            this.firstCooldown = this.normalCooldown;
        }
        this.currentCooldown = this.firstCooldown;

        if (this.dummy instanceof LivingEntity) {
            ((LivingEntity) this.dummy).setAI(false);
        }

        clashMobLogic();
    }

    private void clashMobLogic() {
        ClashMob mob = this;
        new BukkitRunnable() {
            @Override
            public void run() {

                // Exit Condition
                if (finder.isDead()) {
                    kill();
                    cancel();
                }

                dummy.teleport(finder.getLocation().add(0,dummyHeight,0));

                // Acquiring Target
                if (target == null) {
                    finder.setTarget(null);
                    setFinderSpeed(speed);
                    ClashMob closest = getClosestEnemy();
                    if (closest != null) {
                        makeMobLookAt(mob.getFinder(), closest.getFinder());
                        finder.setTarget(closest.getFinder());
                        if (finder.getLocation().distance(closest.getFinder().getLocation()) <= range) {
                            target = closest;
                        } else {
                            // If there is no mob in range, set the cooldown back to the first cooldown.
                            currentCooldown = firstCooldown;
                        }
                    }
                }

                // Handling Target
                if (target != null) {
                    if (target.getFinder().isDead()) {
                        resetTarget();
                    } else {
                        if (mob.getFinder().getLocation().distance(target.getFinder().getLocation()) > range + 0.15) {
                            resetTarget();
                        } else {
                            setFinderSpeed(0);

                            // Cooldown & Attack Management
                            if (currentCooldown == 0) {
                                attack();
                                currentCooldown = normalCooldown;
                            }
                            currentCooldown--;
                        }
                    }
                }
            }


        }.runTaskTimer(ClashCraft.getInstance(), 0L, 1L);
    }

    public void resetTarget() {
        this.target = null;
        this.finder.setTarget(null);
        this.setFinderSpeed(this.speed);
    }

    public ClashMob getClosestEnemy() {
        ArrayList<ClashMob> placed = PlacedManager.getPlaced(this.enemy);
        if (placed == null || placed.isEmpty()) {
            return null;
        }
        ClashMob closest = null;
        for (ClashMob entry : placed) {
            if (closest == null) {
                closest = entry;
            } else {
                Location entryLocation = entry.getFinder().getLocation();
                Location closestLocation = closest.getFinder().getLocation();
                Location finderLocation = this.finder.getLocation();
                if (finderLocation.distance(entryLocation) < finderLocation.distance(closestLocation)) {
                    closest = entry;
                }
            }
        }
        return closest;
    }

    public void setFinderSpeed(double speed) {
        Objects.requireNonNull(this.finder.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(speed);
    }

    public void kill() {
        this.finder.damage(9999);
        if (this.dummy instanceof LivingEntity) {
            ((LivingEntity) this.dummy).damage(9999);
        } else {
            this.dummy.remove();
        }
    }

    private Zombie spawnFinder() {
        Zombie zombie = (Zombie) spawnMob(EntityType.ZOMBIE);
        zombie.setSilent(true);
        Objects.requireNonNull(zombie.getEquipment()).setHelmet(new ItemStack(Material.STONE));
        return zombie;
    }

    public Entity spawnMob(EntityType entityType) {
        Entity mob = Objects.requireNonNull(originalLocation.getWorld()).spawnEntity(originalLocation, entityType);
        if (mob instanceof LivingEntity) {
            if (mob instanceof Ageable) {
                ((Ageable) mob).setAdult();
            }
            Objects.requireNonNull(((LivingEntity) mob).getEquipment()).setHelmet(new ItemStack(Material.AIR));
            ((LivingEntity) mob).getEquipment().setChestplate(new ItemStack(Material.AIR));
            ((LivingEntity) mob).getEquipment().setLeggings(new ItemStack(Material.AIR));
            ((LivingEntity) mob).getEquipment().setBoots(new ItemStack(Material.AIR));
            ((LivingEntity) mob).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
            ((LivingEntity) mob).getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
            Entity vehicle = mob.getVehicle();
            if (vehicle != null) {
                vehicle.removePassenger(mob);
                vehicle.remove();
            }
        }
        return mob;
    }

    public void makeMobLookAt(Entity mob1, Entity mob2) {
        Location loc1 = mob1.getLocation();
        Location loc2 = mob2.getLocation();
        Vector direction = loc2.toVector().subtract(loc1.toVector()).normalize();
        double yaw = Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
        double pitch = Math.toDegrees(Math.asin(direction.getY()));
        mob1.setRotation((float) yaw, (float) pitch);
    }


    public Zombie getFinder() {
        return this.finder;
    }

    public Entity getDummy() {
        return this.dummy;
    }

    public ClashMob getTarget() {
        return this.target;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setFirstCooldown(int ticks) {
        this.firstCooldown = ticks;
    }

    public void setNormalCooldown(int ticks) {
        this.normalCooldown = ticks;
    }

    public ClashPlayer getPlayer() {
        return this.player;
    }

    public abstract void setStats();
    public abstract void attack();
    public abstract Entity spawnDummy();
}
