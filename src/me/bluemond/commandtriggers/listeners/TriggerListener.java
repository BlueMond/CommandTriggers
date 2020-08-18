package me.bluemond.commandtriggers.listeners;

import me.bluemond.commandtriggers.CommandTriggers;
import me.bluemond.commandtriggers.events.TriggerEvent;
import me.bluemond.commandtriggers.triggers.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TriggerListener implements Listener {

    CommandTriggers plugin;

    public TriggerListener(CommandTriggers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if(action.equals(Action.)){

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onBlockBreak", event.getBlock().getType());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onBlockPlace", event.getBlockPlaced().getType());
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemBreak", event.getBrokenItem().getType());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemBreak", player.getInventory().getItemInMainHand().getType());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        initiateAsyncTriggers(player, "onPlayerDeath", null);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerChangeWorld", null);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerJoin", null);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemConsume", event.getItem().getType());
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        // wait 10 ticks (.5 seconds)   PLAYERRESPAWN requires this because the player doesn't exist yet
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                initiateAsyncTriggers(player, "onPlayerRespawn", null), 10);


    }

    /*
    This may be used later to attempt to group all asyncs into one function,
    but it causes confusion with how to still determine which triggers are in use
     */
    private void initiateAsyncTriggers(Player player, String eventName, Material usedMaterial){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {

                List<Trigger> triggers = plugin.getConfigHandler().getTriggers();

                for (Trigger trigger : triggers) {
                    TriggerEvent triggerEvent = trigger.getEvent(eventName);

                    if (triggerEvent != null){
                        // procure argument lists
                        List<Material> materialWhitelist = triggerEvent.getPermittedMaterials();
                        List<Material> materialBlacklist = triggerEvent.getUnpermittedMaterials();

                        boolean valid = true;

                        // argument checks against event parameters
                        if(usedMaterial != null) {
                            if (!materialWhitelist.isEmpty()) {
                                valid = false;
                                for (Material material : materialWhitelist) {
                                    if (material.equals(usedMaterial)) {
                                        valid = true;
                                        break;
                                    }
                                }
                            } else if (!materialBlacklist.isEmpty()) {
                                for (Material material : materialWhitelist) {
                                    if (material.equals(usedMaterial)) {
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                        }

                        // run the trigger
                        if(valid) {
                            initiateTrigger(player, trigger);
                        }

                    }
                }

            }
        });
    }


    /*
    initiateTrigger performs a trigger's commands after checking that the target player has permission
     */
    private void initiateTrigger(Player player, Trigger trigger) {
        boolean hasPermission = false;

        // permissions checks

        for (String permission : trigger.getPermissions()) {
            if(player.hasPermission(permission)){
                hasPermission = true;
                break;
            }
        }

        if(hasPermission){
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            for (String command : trigger.getCommands()) {
                command = command.replace("{player}", player.getName());
                Bukkit.getServer().dispatchCommand(console, command);
            }
        }

    }
}
