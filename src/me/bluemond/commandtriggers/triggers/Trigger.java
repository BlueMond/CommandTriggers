package me.bluemond.commandtriggers.triggers;

import me.bluemond.commandtriggers.CommandTriggers;
import me.bluemond.commandtriggers.events.EventClassMap;
import me.bluemond.commandtriggers.events.TriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class Trigger {
    private String name;
    private List<String> playerCommands;
    private List<String> serverCommands;
    private List<String> permissions;
    private List<TriggerEvent> triggerEvents;



    public Trigger(String name, ConfigurationSection triggerConfig) throws
            IllegalAccessException, InstantiationException, InvocationTargetException {
        this.name = name;
        triggerEvents = new ArrayList<>();

        playerCommands = triggerConfig.getStringList("playerCommands");
        serverCommands = triggerConfig.getStringList("serverCommands");
        permissions = triggerConfig.getStringList("permissions");
        permissions.add("commandtriggers." +name);
        ConfigurationSection eventsConfig = triggerConfig.getConfigurationSection("events");

        parseEvents(eventsConfig);
    }

    public String getName() {
        return name;
    }

    private void parseEvents(ConfigurationSection eventsConfig) throws
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Set<String> eventKeys = eventsConfig.getKeys(false);
        for (String eventKey : eventKeys) {
            Class eventClass = EventClassMap.getClass(eventKey);
            if(eventClass != null){
                Constructor cstr = eventClass.getConstructors()[0];
                TriggerEvent triggerEvent =
                        (TriggerEvent) cstr.newInstance(eventKey, eventsConfig.getConfigurationSection(eventKey));
                triggerEvents.add(triggerEvent);
            }
        }
    }


    public List<String> getPermissions() {
        return permissions;
    }

    public TriggerEvent getEvent(String eventName) {
        for (TriggerEvent triggerEvent : triggerEvents) {
            if(triggerEvent.getName().equalsIgnoreCase(eventName)){
                return triggerEvent;
            }
        }
        return null;
    }

    public void initiate(Player player, CommandTriggers plugin) {
        boolean hasPermission = false;

        // permissions checks

        for (String permission : permissions) {
            if(player.hasPermission(permission)){
                hasPermission = true;
                break;
            }
        }


        if(hasPermission){
            runCommands(player, plugin);
        }

    }

    private void runCommands(Player player, CommandTriggers plugin){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        if(serverCommands != null){
            for (String command : serverCommands) {
                final String replacedCommand = command.replace("{player}", player.getName());
                Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.getServer().dispatchCommand(console, replacedCommand));
            }
        }

        if(playerCommands != null){
            for (String command : playerCommands) {
                final String replacedCommand = command.replace("{player}", player.getName());
                Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Boolean>(){
                    @Override
                    public Boolean call(){
                        player.chat(replacedCommand);
                        return true;
                    }
                });
            }
        }

    }

}
