package ch.uisa.minecraft.mqttlink.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

public class OnPlayerJoinEvent extends OnPlayerEvent implements Listener
{
    private Logger logger;

    public OnPlayerJoinEvent(Logger logger) {
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) throws MqttException {
        sendMqttEvent(ReservedEvents.PLAYER_JOIN, playerJoinEvent.getPlayer());
    }


}
