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
