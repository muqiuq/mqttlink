package ch.uisa.minecraft.mqttlink;

import ch.uisa.minecraft.mqttlink.events.MqttInboundEventHandler;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Global {

    public static LinkedBlocks linkedBlocks = new LinkedBlocks();

    public static Mqtt mqtt;


    public static Logger logger;
    public static MqttInboundEventHandler mqttInboundEventHandler;
    public static Main main;

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
}
