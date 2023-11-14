package ch.uisa.minecraft.mqttlink;

import ch.uisa.minecraft.mqttlink.commands.BlockCommand;
import ch.uisa.minecraft.mqttlink.commands.DeleteCommand;
import ch.uisa.minecraft.mqttlink.commands.EditCommand;
import ch.uisa.minecraft.mqttlink.commands.ListCommand;
import ch.uisa.minecraft.mqttlink.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private Logger logger;

    @Override
    public void onEnable() {
        Global.logger = Bukkit.getLogger();
        Global.main = this;

        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        logger = Bukkit.getLogger();

        try {
            Config.Load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot properly start without Config File");
            return;
        }

        Global.mqtt = new Mqtt(logger, Config.broker, Config.username, Config.password);

        try {
            Global.mqtt.start();
        } catch (MqttException e) {
            logger.log(Level.SEVERE, "Failed to start MQTT client", e);
        }
        try {
            Global.linkedBlocks.open();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load LinkedBlocks", e);
        }


        this.getCommand("mqttlink").setExecutor(new BlockCommand(logger));
        this.getCommand("mqttlist").setExecutor(new ListCommand(logger));
        this.getCommand("mqttdel").setExecutor(new DeleteCommand(logger));
        this.getCommand("mqttedit").setExecutor(new EditCommand(logger));

        getServer().getPluginManager().registerEvents(new OnRedstoneEvent(logger), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(logger), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(logger), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(logger), this);

        Global.updateLinkedBlocksSubscriptions();

        Global.mqttInboundEventHandler = new MqttInboundEventHandler(logger, Global.linkedBlocks);

        Global.mqtt.addHandler((topic, message) -> {
            Global.mqttInboundEventHandler.triggerEvent(topic, message.toString());
        });

        Global.onlinePlayers = new OnlinePlayers();

        Global.notifyStateThread = new NotifyStateThread(Global.mqtt, Global.executor, Global.onlinePlayers);

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
        try {
            Global.mqtt.stop();
        } catch (MqttException e) {
            logger.log(Level.SEVERE, "", e);
        }
        try {
            Global.linkedBlocks.write();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "", e);
        }
        try{
            Global.executor.shutdown();
        }catch(Exception e) {
            logger.log(Level.FINER, "", e);
        }
    }

}
