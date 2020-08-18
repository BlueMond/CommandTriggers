package me.bluemond.commandtriggers.triggers;

import me.bluemond.commandtriggers.events.TriggerEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Trigger {
    private String name;
    private List<String> commands;
    private List<String> permissions;
    private List<TriggerEvent> triggerEvents;



    public Trigger(String name, FileConfiguration mainConfig){
        this.name = name;
        ConfigurationSection triggerPath = mainConfig.getConfigurationSection("triggers." +name);
        commands = triggerPath.getStringList("commands");
        permissions = triggerPath.getStringList("permissions");
        permissions.add("commandtriggers." +name);
        List<String> eventNames = triggerPath.getStringList("events");
        parseEvents(eventNames, triggerPath.getConfigurationSection("events"));

        /* old event check code
        onPlayerRespawn = events.contains("onPlayerRespawn");
        onPlayerBedLeave = events.contains("onPlayerBedLeave");
        onPlayerBedEnter = events.contains("onPlayerBedEnter");
        onPlayerItemConsume = events.contains("onPlayerItemConsume");
        onPlayerJoin = events.contains("onPlayerJoin");
        onPlayerItemBreak = events.contains("onPlayerItemBreak");
        onPlayerEggThrow = events.contains("onPlayerEggThrow");
        onPlayerChangedWorld = events.contains("onPlayerChangedWorld");
        onPlayerDeath = events.contains("onPlayerDeath");
        */
    }

    private void parseEvents(List<String> eventNames, ConfigurationSection eventsSection){
        for (String eventName : eventNames) {
            TriggerEvent triggerEvent = new TriggerEvent(eventName, eventsSection.getConfigurationSection(eventName));
            triggerEvents.add(triggerEvent);
        }
    }


    public String getName() {
        return name;
    }

    public List<String> getCommands() {
        return commands;
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

}
