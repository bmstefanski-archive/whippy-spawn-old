/*
MIT License

Copyright (c) 2018 Whippy Tools

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package pl.bmstefanski.spawn.command;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bmstefanski.commands.Arguments;
import pl.bmstefanski.commands.Executor;
import pl.bmstefanski.commands.Messageable;
import pl.bmstefanski.commands.annotation.Command;
import pl.bmstefanski.commands.annotation.Completer;
import pl.bmstefanski.commands.annotation.GameOnly;
import pl.bmstefanski.commands.annotation.Permission;
import pl.bmstefanski.spawn.SpawnPlugin;
import pl.bmstefanski.spawn.configuration.SpawnConfig;
import pl.bmstefanski.tools.manager.TeleportManager;
import pl.bmstefanski.tools.storage.configuration.Messages;

public class SpawnCommand implements Executor, Messageable {

    private final SpawnPlugin plugin;
    private final Messages messages;
    private final SpawnConfig config;

    public SpawnCommand(SpawnPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.config = plugin.getConfiguration();
    }

    @Command(name = "spawn", usage = "[player]", max = 1)
    @Permission("tools.command.spawn")
    @GameOnly(false)
    public void command(Arguments arguments) {
        TeleportManager taskManager = new TeleportManager(plugin.getParentPlugin());
        CommandSender sender = arguments.getSender();

        if (config.getExists()) {

            int x = config.getX();
            int y = config.getY();
            int z = config.getZ();
            String worldName = config.getWorld();

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);


            if (arguments.getArgs().length == 0) {
                if (!(sender instanceof Player)) {
                    sendMessage(sender, messages.getOnlyPlayer());
                    return;
                }

                Player player = (Player) arguments.getSender();

                taskManager.teleport(player, location, plugin.getParentPlugin().getConfiguration().getSpawnDelay());
                return;
            }

            if (Bukkit.getPlayer(arguments.getArgs(0)) == null) {
                sendMessage(sender, StringUtils.replace(messages.getPlayerNotFound(), "%player%", arguments.getArgs(0)));
                return;
            }

            Player target = Bukkit.getPlayer(arguments.getArgs(0));
            target.teleport(location);

            return;
        }

        sendMessage(sender, messages.getSpawnFailed());
    }

    @Completer("spawn")
    public void completer(Arguments arguments) {

    }
}
