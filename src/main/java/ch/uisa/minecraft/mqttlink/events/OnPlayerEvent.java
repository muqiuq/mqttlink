package ch.uisa.minecraft.mqttlink.events;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.Helper;
import org.bukkit.entity.Player;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class OnPlayerEvent {

    public void sendMqttEvent(ReservedEvents event, Player p, String deathCause) throws MqttException {
        Global.mqtt.sendMessage(Helper.getMqttEventPath(event.name()), String.format("%s,%s,%s", ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ), p.getName(), deathCause));
    }

    public void sendMqttEvent(ReservedEvents event, Player p) throws MqttException {
        sendMqttEvent(event,p, "");
    }

}
