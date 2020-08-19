package me.bluemond.commandtriggers.fileutil;

import me.bluemond.commandtriggers.CommandTriggers;
import me.bluemond.commandtriggers.triggers.Trigger;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class ConfigHandler {

    private final CommandTriggers plugin;
    private FileConfiguration mainConfig;

    List<Trigger> triggers;


    public ConfigHandler(CommandTriggers instance) throws
            IllegalAccessException, InstantiationException, InvocationTargetException {
        plugin = instance;

        loadMainConfig();
    }

    public void loadMainConfig() throws IllegalAccessException, InvocationTargetException, InstantiationException {

        mainConfig = FileUtil.loadFile("config.yml", "config.yml");
        triggers = new ArrayList<>();

        //create list of Trigger Class Objects populated by config
        Set<String> set = mainConfig.getConfigurationSection("triggers").getKeys(false);
        for (String triggername : set) {
            triggers.add(new Trigger(triggername, mainConfig.getConfigurationSection("triggers." +triggername)));
        }

        plugin.registerDefaultTriggerPermissions(triggers);

        plugin.getLogger().info("Loaded config.yml");
    }

    public List<Trigger> getTriggers(){
        return triggers;
    }

}
