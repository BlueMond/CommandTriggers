package me.bluemond.commandtriggers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

import java.util.List;

public abstract class TriggerEvent {
    String name;
    private boolean arguments;


    public TriggerEvent(String name, ConfigurationSection eventConfig){
        this.name = name;
        arguments = parseArguments(eventConfig);
    }

    public abstract boolean checkArguments(Event event, Material usedMaterial);

    protected abstract boolean parseArguments(ConfigurationSection eventConfig);



    /*
    returns whether or not this event has arguments
     */
    public boolean hasArguments(){
        return arguments;
    }

    public String getName() {
        return name;
    }

}
