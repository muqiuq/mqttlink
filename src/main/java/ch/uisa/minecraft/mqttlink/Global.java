package ch.uisa.minecraft.mqttlink;

import ch.uisa.minecraft.mqttlink.events.MqttInboundEventHandler;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class Global {

    public static LinkedBlocks linkedBlocks = new LinkedBlocks();

    public static Mqtt mqtt;

    public static Logger logger;
    public static MqttInboundEventHandler mqttInboundEventHandler;
    public static Main main;

    public static NotifyStateThread notifyStateThread;
    public static OnlinePlayers onlinePlayers;

    public static void updateLinkedBlocksSubscriptions() {
        for(LinkedBlock linkedBlock: linkedBlocks.linkedBlocks) {
            if(linkedBlock.isInbound) {
                String topic = linkedBlock.topic;
                if(!linkedBlock.rootTopic) {
                    topic = Helper.getMqttEventPath(linkedBlock.topic);
                }
                mqtt.subscribe(topic);
            }
        }
    }

    public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
}
