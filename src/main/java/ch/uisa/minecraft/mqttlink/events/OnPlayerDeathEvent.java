package ch.uisa.minecraft.mqttlink.events;

import ch.uisa.minecraft.mqttlink.Global;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class OnPlayerDeathEvent extends OnPlayerEvent implements Listener
{
    private Logger logger;

    public OnPlayerDeathEvent(Logger logger) {
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeathEvent(EntityDeathEvent entityDeathEvent) throws MqttException {
        if(entityDeathEvent.getEntity() instanceof LivingEntity && entityDeathEvent.getEntity() instanceof Player) {
            Player p = (Player) entityDeathEvent.getEntity();
            sendMqttEvent(ReservedEvents.PLAYER_DEATH, p, p.getLastDamageCause().getCause().toString());
        }
    }

}
