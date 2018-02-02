package pl.bmstefanski.spawn.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
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
import pl.bmstefanski.tools.storage.configuration.Messages;

public class SetSpawnCommand implements Executor, Messageable {

    private final SpawnPlugin plugin;
    private final SpawnConfig config;
    private final Messages messages;

    public SetSpawnCommand(SpawnPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.messages = plugin.getMessages();
    }

    @Command(name = "setspawn")
    @GameOnly
    @Permission("tools.command.setspawn")
    public void command(Arguments arguments) {
        Player player = (Player) arguments.getSender();
        Location location = player.getLocation();

        String worldName = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        config.setX(x);
        config.setY(y);
        config.setZ(z);
        config.setWorld(worldName);
        config.setExists(true);

        sendMessage(player, StringUtils.replaceEach(messages.getSetspawnSuccess(),
                new String[] {"%x%", "%y%", "%z%", "%world%"},
                new String[] {x + "", y + "", z + "", worldName})
        );
    }

    @Completer("setspawn")
    public void completer(Arguments arguments) {

    }

}
