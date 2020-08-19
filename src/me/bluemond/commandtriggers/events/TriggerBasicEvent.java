package me.bluemond.commandtriggers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public class TriggerBasicEvent extends TriggerEvent{

    public TriggerBasicEvent(String name, ConfigurationSection eventConfig) {
        super(name, eventConfig);
    }

    @Override
    public boolean checkArguments(Event event, Material usedMaterial) {
        return true;
    }

    @Override
    protected boolean parseArguments(ConfigurationSection eventConfig) {
        return false;
    }
}
