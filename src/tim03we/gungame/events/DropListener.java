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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import tim03we.gungame.GunGame;

public class DropListener implements Listener {

    private GunGame plugin;

    public DropListener(GunGame plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event)
    {
        if(!this.plugin.getConfig().getBoolean("events.drop")) {
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }
}
