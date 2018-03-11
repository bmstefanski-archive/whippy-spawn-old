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

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bmstefanski.commands.CommandArguments;
import pl.bmstefanski.commands.CommandExecutor;
import pl.bmstefanski.commands.Messageable;
import pl.bmstefanski.commands.annotation.Command;
import pl.bmstefanski.commands.annotation.GameOnly;
import pl.bmstefanski.commands.annotation.Permission;
import pl.bmstefanski.spawn.SpawnPlugin;
import pl.bmstefanski.spawn.configuration.SpawnConfig;
import pl.bmstefanski.tools.storage.configuration.Messages;

public class SetSpawnCommand implements CommandExecutor, Messageable {

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
    @Override
    public void execute(CommandSender commandSender, CommandArguments commandArguments) {
        Player player = (Player) commandSender;
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

}
