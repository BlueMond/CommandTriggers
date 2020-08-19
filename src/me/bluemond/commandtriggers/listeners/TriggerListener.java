package me.bluemond.commandtriggers.listeners;

import me.bluemond.commandtriggers.CommandTriggers;
import me.bluemond.commandtriggers.events.TriggerEvent;
import me.bluemond.commandtriggers.triggers.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.Callable;

public class TriggerListener implements Listener {

    CommandTriggers plugin;

    public TriggerListener(CommandTriggers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            initiateAsyncTriggers(player, "onPlayerItemUse", item.getType(), (Event) event);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onBlockBreak", event.getBlock().getType(), (Event) event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onBlockPlace", event.getBlockPlaced().getType(), (Event) event);
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemBreak", event.getBrokenItem().getType(), (Event) event);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemHeld", player.getInventory().getItemInMainHand().getType(), (Event) event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        initiateAsyncTriggers(player, "onPlayerDeath", null, (Event) event);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerChangedWorld", null, (Event) event);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerJoin", null, (Event) event);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();

        initiateAsyncTriggers(player, "onPlayerItemConsume", event.getItem().getType(), (Event) event);
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        // wait 10 ticks (.5 seconds)   PLAYERRESPAWN requires this because the player doesn't exist yet
        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                initiateAsyncTriggers(player, "onPlayerRespawn", null, (Event) event), 10);


    }

    /*
    This may be used later to attempt to group all asyncs into one function,
    but it causes confusion with how to still determine which triggers are in use
     */
    private void initiateAsyncTriggers(Player player, String eventName, Material usedMaterial, Event event){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {

                List<Trigger> triggers = plugin.getConfigHandler().getTriggers();
                System.out.println(eventName+ " FIRED! Checking Triggers For Handling Event.");

                for (Trigger trigger : triggers) {
                    TriggerEvent triggerEvent = trigger.getEvent(eventName);
                    System.out.println("Checking " + trigger.getName() + " trigger for usage of " + eventName);

                    if (triggerEvent != null) {
                        System.out.println("Found " + eventName + " in " + trigger.getName() + " trigger.");
                        if (triggerEvent.checkArguments(event, usedMaterial)) {
                            trigger.initiate(player, plugin);
                        }
                    }
                }

            }
        });
    }


}
