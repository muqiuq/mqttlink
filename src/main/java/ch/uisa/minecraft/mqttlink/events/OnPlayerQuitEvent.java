package ch.uisa.minecraft.mqttlink.events;

import ch.uisa.minecraft.mqttlink.Global;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

public class OnPlayerQuitEvent extends OnPlayerEvent implements Listener {

    // PlayerQuitEvent
    private Logger logger;

    public OnPlayerQuitEvent(Logger logger) {
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuitEvent(PlayerQuitEvent playerQuitEvent) throws MqttException {
        sendMqttEvent(ReservedEvents.PLAYER_QUIT, playerQuitEvent.getPlayer());
        Global.onlinePlayers.playerVentOffline(playerQuitEvent.getPlayer());
    }

}
