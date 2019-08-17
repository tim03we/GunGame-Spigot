package tim03we.gungame.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tim03we.gungame.GunGame;

public class GGTask extends BukkitRunnable {

    private GunGame plugin;

    public GGTask(GunGame plugin)
    {

        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            int needLevel = this.plugin.needLevel.get(player.getName());
            if (needLevel == 1) {
                int currLevel = this.plugin.levels.get(player.getName());
                this.plugin.levelChange(player, currLevel);
            }
        }
    }
}
