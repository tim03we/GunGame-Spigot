package tim03we.gungame.events;

/*
 * Copyright (c) 2019 tim03we  < https://github.com/tim03we >
 * Discord: tim03we | TP#9129
 *
 * This software is distributed under "GNU General Public License v3.0".
 * This license allows you to use it and/or modify it but you are not at
 * all allowed to sell this plugin at any cost. If found doing so the
 * necessary action required would be taken.
 *
 * GunGame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3.0 for more details.
 *
 * You should have received a copy of the GNU General Public License v3.0
 * along with this program. If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import tim03we.gungame.GunGame;

public class EntityListener implements Listener {

    private GunGame plugin;

    public EntityListener(GunGame plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        int x = Math.round((int) player.getLocation().getX());
        int y = Math.round((int) player.getLocation().getY());
        int z = Math.round((int) player.getLocation().getZ());
        World level = event.getPlayer().getWorld();
        int ground = level.getBlockTypeIdAt(x, y, z);
        if(ground == Material.WATER.getId() || ground == Material.WATER_LILY.getId() || ground == Material.STATIONARY_WATER.getId()) {
            EntityDamageEvent cause = player.getLastDamageCause();
            if(cause instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
                if(damager instanceof Player) {
                    this.plugin.levelUp((Player)damager);
                    player.setHealth(0);
                    Bukkit.getServer().broadcastMessage(this.plugin.getConfig().getString("messages.kill").replace("{player}", player.getName()).replace("{killer}", damager.getName()));
                }
            } else {
                player.setHealth(0);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event)
    {
        event.getDrops().clear();
        Player player = event.getEntity();
        event.setDeathMessage(this.plugin.getConfig().getString("messages.death").replace("{player}", player.getName()));
        this.plugin.levelDown(player);
        EntityDamageEvent cause = player.getLastDamageCause();
        if(cause instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
            this.plugin.levelUp((Player)damager);
            event.setDeathMessage(this.plugin.getConfig().getString("messages.kill").replace("{player}", player.getName()).replace("{killer}", damager.getName()));
        }
    }
}
