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

package pl.bmstefanski.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.config.ConfigManager;
import pl.bmstefanski.commands.BukkitCommands;
import pl.bmstefanski.spawn.api.SpawnPluginAPI;
import pl.bmstefanski.spawn.command.SetSpawnCommand;
import pl.bmstefanski.spawn.command.SpawnCommand;
import pl.bmstefanski.spawn.configuration.SpawnConfig;
import pl.bmstefanski.spawn.listener.PlayerJoinListener;
import pl.bmstefanski.spawn.listener.PlayerRespawnListener;
import pl.bmstefanski.tools.Tools;
import pl.bmstefanski.tools.storage.configuration.Messages;

import java.io.File;

public class SpawnPlugin extends JavaPlugin implements SpawnPluginAPI {

    private final File spawnConfigFile = new File(getDataFolder(), "spawn.yml");

    private SpawnConfig spawnConfig;

    @Override
    public void onEnable() {
        this.checkParentPlugin();

        this.spawnConfig = ConfigManager.createInstance(SpawnConfig.class);

        this.spawnConfig.bindFile(spawnConfigFile);
        this.spawnConfig.load();
        this.spawnConfig.save();

        this.registerListeners(
                new PlayerJoinListener(this),
                new PlayerRespawnListener(this)
        );

        this.registerCommands(
                new SetSpawnCommand(this),
                new SpawnCommand(this)
        );
    }

    @Override
    public void onDisable() {
        this.spawnConfig.save();
    }

    private void registerCommands(Object... commands) {
        BukkitCommands bukkitCommands = new BukkitCommands(this);

        for (Object object : commands) {
            bukkitCommands.registerCommands(object);
        }
    }

    private void registerListeners(Listener... listeners) {

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }

    }

    private void checkParentPlugin() {
        PluginManager pluginManager = getServer().getPluginManager();

        Tools tools = (Tools) pluginManager.getPlugin("WhippyTools");

        if (!tools.isEnabled()) {
            this.setEnabled(false);
            System.out.println("Cannot load plugin, check if your ./plugins directory contains WhippyTools.jar!");
        }
    }

    @Override
    public Location getSpawnLocation() {
        int x = spawnConfig.getX();
        int y = spawnConfig.getY();
        int z = spawnConfig.getZ();
        String worldName = spawnConfig.getWorld();

        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    @Override
    public SpawnConfig getConfiguration() {
        return spawnConfig;
    }

    @Override
    public Messages getMessages() {
        return Tools.getInstance().getMessages();
    }

    @Override
    public Tools getParentPlugin() {
        return Tools.getInstance();
    }

}
