package tim03we.gungame;

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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tim03we.gungame.events.*;
import tim03we.gungame.tasks.GGTask;

import java.io.File;
import java.util.HashMap;

public class GunGame extends JavaPlugin {

    public HashMap<String, Integer> levels = new HashMap<String, Integer>();
    public HashMap<String, Integer> needLevel = new HashMap<String, Integer>();

    @Override
    public void onLoad()
    {
        for (Player player: this.getServer().getOnlinePlayers()) {
            this.levels.put(player.getName(), 0);
            this.needLevel.put(player.getName(), 0);
            player.sendMessage(this.getConfig().getString("messages.reload"));
        }
    }

    @Override
    public void onEnable()
    {
        this.register();
        new GGTask(this).runTaskTimer(this, 20, 20);
        this.saveDefaultConfig();
        this.saveResource("level.yml", true);

    }

    public void invClear(Player player)
    {
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().clear();
    }

    public void levelChange(Player player, int level)
    {
        FileConfiguration lcfg = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/level.yml"));
        this.invClear(player);
        this.needLevel.put(player.getName(), 0);
        int currLevel = this.levels.get(player.getName());
        player.setHealth(20);
        //player.setNameTag(String.valueOf(this.getConfig().get("format.nametag")).replace("{player}", player.getName()).replace("{level}", String.valueOf(currLevel)));
        if(this.getConfig().getInt("Maximum-Level") < currLevel) {
            player.sendMessage(this.getConfig().getString("messages.max"));
            ItemStack helmet = new ItemStack(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".helmet.id"));
            ItemStack chestplate = new ItemStack(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".chestplate.id"));
            ItemStack leggings = new ItemStack(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".leggings.id"));
            ItemStack boots = new ItemStack(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".boots.id"));
            ItemStack weapon = new ItemStack(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".weapon.id"));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        } else {
            ItemStack helmet = new ItemStack(lcfg.getInt("L" + currLevel + ".helmet.id"));
            ItemStack chestplate = new ItemStack(lcfg.getInt("L" + currLevel + ".chestplate.id"));
            ItemStack leggings = new ItemStack(lcfg.getInt("L" + currLevel + ".leggings.id"));
            ItemStack boots = new ItemStack(lcfg.getInt("L" + currLevel + ".boots.id"));
            ItemStack weapon = new ItemStack(lcfg.getInt("L" + currLevel + ".weapon.id"));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        }
    }

    public void register()
    {
        Bukkit.getLogger().info("Events registered.");
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LogListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DropListener(this), this);
    }

    public void levelUp(Player player)
    {
        this.levels.put(player.getName(), this.levels.get(player.getName()) + 1);
        this.needLevel.put(player.getName(), 1);
    }

    public void levelDown(Player player)
    {
        int cL = this.levels.get(player.getName());
        int nL = cL * this.getConfig().getInt("Chance");
        this.levels.put(player.getName(), nL);
    }
}
