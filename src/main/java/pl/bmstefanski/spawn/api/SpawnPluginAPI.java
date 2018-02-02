package pl.bmstefanski.spawn.api;

import pl.bmstefanski.spawn.configuration.SpawnConfig;
import pl.bmstefanski.tools.Tools;
import pl.bmstefanski.tools.storage.configuration.Messages;

public interface SpawnPluginAPI {

    SpawnConfig getConfiguration();

    Messages getMessages();

    Tools getParentPlugin();

}
