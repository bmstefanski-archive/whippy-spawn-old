package pl.bmstefanski.spawn;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.diorite.config.ConfigManager;
import pl.bmstefanski.commands.BukkitCommands;
import pl.bmstefanski.spawn.api.SpawnPluginAPI;
import pl.bmstefanski.spawn.command.SetSpawnCommand;
import pl.bmstefanski.spawn.command.SpawnCommand;
import pl.bmstefanski.spawn.configuration.SpawnConfig;
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

    private void checkParentPlugin() {
        PluginManager pluginManager = getServer().getPluginManager();

        Tools tools = (Tools) pluginManager.getPlugin("WhippyTools");

        if (!tools.isEnabled()) {
            this.setEnabled(false);
            System.out.println("Cannot load plugin, check if your ./plugins directory contains WhippyTools.jar!");
        }
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
