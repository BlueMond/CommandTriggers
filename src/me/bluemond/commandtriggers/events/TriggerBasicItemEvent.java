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

        // argument checks against event parameters

        // if usedMaterial is not null and event has an argument for it
        if(usedMaterial != null) {
            boolean validMaterial = true;

            if(hasArguments()){
                if (!permittedMaterials.isEmpty()) {
                    validMaterial = false;
                    for (Material material : permittedMaterials) {
                        if (material.equals(usedMaterial)) {
                            validMaterial = true;
                            break;
                        }
                    }
                } else if (!unpermittedMaterials.isEmpty()) {
                    for (Material material : unpermittedMaterials) {
                        if (material.equals(usedMaterial)) {
                            validMaterial = false;
                            break;
                        }
                    }
                }
            }

            return validMaterial;

        // if usedMaterial is null
        }else{
            if(hasArguments()){
                return false;
            }else{
                return true;
            }
        }

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
            material = material.toUpperCase().trim();
            Material mat = Material.matchMaterial(material);
            if(mat != null){
                realMaterials.add(mat);

                // here it adds any materials with the suffix
            }else if(material.contains("*")){
                String segment = material.substring(material.indexOf("*")+1, material.length());
                for(Material mcMaterial : Material.values()){
                    if(mcMaterial.name().contains(segment)){
                        realMaterials.add(mcMaterial);
                    }
                }
            }
        }


        return realMaterials;
    }
}
