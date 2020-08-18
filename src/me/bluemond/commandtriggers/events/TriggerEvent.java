package me.bluemond.commandtriggers.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TriggerEvent {
    String name;
    private boolean arguments;
    private List<Material> permittedMaterials;
    private List<Material> unpermittedMaterials;

    public TriggerEvent(String name, ConfigurationSection argumentPath){
        this.name = name;
        arguments = parseArguments(argumentPath);
    }

    /*
    Parses the arguments from an event and returns whether or not arguments were found
     */
    private boolean parseArguments(ConfigurationSection argumentPath){
        List<Material> permittedMaterials = parseMaterials(argumentPath.getStringList("materials"));
        List<Material> unpermittedMaterials = parseMaterials(argumentPath.getStringList("notMaterials"));

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

    /*
    returns whether or not this event has arguments
     */
    public boolean hasArguments(){
        return arguments;
    }

    public String getName() {
        return name;
    }

    public List<Material> getPermittedMaterials() {
        return permittedMaterials;
    }

    public List<Material> getUnpermittedMaterials() {
        return unpermittedMaterials;
    }
}
