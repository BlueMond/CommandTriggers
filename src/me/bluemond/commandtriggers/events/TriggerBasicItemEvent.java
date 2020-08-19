package me.bluemond.commandtriggers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class TriggerBasicItemEvent extends TriggerEvent{

    private List<Material> permittedMaterials;
    private List<Material> unpermittedMaterials;

    public TriggerBasicItemEvent(String name, ConfigurationSection eventConfig){
        super(name, eventConfig);
    }

    @Override
    public boolean checkArguments(Event event, Material usedMaterial) {
        boolean valid = true;

        // argument checks against event parameters
        if(usedMaterial != null) {
            if (!permittedMaterials.isEmpty()) {
                valid = false;
                for (Material material : permittedMaterials) {
                    if (material.equals(usedMaterial)) {
                        valid = true;
                        break;
                    }
                }
            } else if (!unpermittedMaterials.isEmpty()) {
                for (Material material : unpermittedMaterials) {
                    if (material.equals(usedMaterial)) {
                        valid = false;
                        break;
                    }
                }
            }
        }

        return valid;
    }

    /*
    Parses the arguments from an event and returns whether or not arguments were found
     */
    @Override
    protected boolean parseArguments(ConfigurationSection eventConfig){
        permittedMaterials = parseMaterials(eventConfig.getStringList("materials"));
        unpermittedMaterials = parseMaterials(eventConfig.getStringList("notMaterials"));

        if(permittedMaterials.isEmpty() && unpermittedMaterials.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    /*
    Parses the bukkit recognized Materials from the material list
     */
    private List<Material> parseMaterials(List<String> materialsList){
        List<Material> realMaterials = new ArrayList<>();

        for(String material : materialsList){
            Material mat = Material.getMaterial(material.toUpperCase().trim());
            if(mat != null){
                realMaterials.add(mat);
            }
        }

        return realMaterials;
    }
}
