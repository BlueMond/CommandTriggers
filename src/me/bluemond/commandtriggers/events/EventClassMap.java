package me.bluemond.commandtriggers.events;

import java.util.HashMap;
import java.util.Map;

public class EventClassMap {
    private static Map<String, Class> eventClassReferences;


    static{
        eventClassReferences = new HashMap<>();
        eventClassReferences.put("onPlayerItemUse", TriggerBasicItemEvent.class);
        eventClassReferences.put("onBlockBreak", TriggerBasicItemEvent.class);
        eventClassReferences.put("onBlockPlace", TriggerBasicItemEvent.class);
        eventClassReferences.put("onPlayerItemBreak", TriggerBasicItemEvent.class);
        eventClassReferences.put("onPlayerItemHeld", TriggerBasicItemEvent.class);
        eventClassReferences.put("onPlayerDeath", TriggerBasicEvent.class);
        eventClassReferences.put("onPlayerChangedWorld", TriggerBasicEvent.class);
        eventClassReferences.put("onPlayerJoin", TriggerBasicEvent.class);
        eventClassReferences.put("onPlayerItemConsume", TriggerBasicItemEvent.class);
        eventClassReferences.put("onPlayerRespawn", TriggerBasicEvent.class);
    }

    public static Class getClass(String eventName){
        return eventClassReferences.get(eventName);
    }
}
