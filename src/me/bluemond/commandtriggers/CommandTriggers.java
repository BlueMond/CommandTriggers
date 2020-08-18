package me.bluemond.commandtriggers;

import me.bluemond.commandtriggers.listeners.TriggerListener;
import me.bluemond.commandtriggers.fileutil.ConfigHandler;
import me.bluemond.commandtriggers.triggers.Trigger;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CommandTriggers extends JavaPlugin {

    ConfigHandler configHandler;

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    @Override
    public void onEnable() {

        configHandler = new ConfigHandler(this);
        registerListeners();
        getLogger().info("CommandTriggers v" + getDescription().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {

        getLogger().info("CommandTriggers v" + getDescription().getVersion() + " has been disabled!");
    }

    public void registerDefaultTriggerPermissions(List<Trigger> triggers){
        for (Trigger trigger : triggers) {
            for (String permission : trigger.getPermissions()) {
                getServer().getPluginManager().addPermission(new Permission(permission, PermissionDefault.FALSE));
            }
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new TriggerListener(this), this);
    }
}
