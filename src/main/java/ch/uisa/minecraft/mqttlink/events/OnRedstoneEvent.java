package ch.uisa.minecraft.mqttlink.events;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.Helper;
import ch.uisa.minecraft.mqttlink.LinkedBlock;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnRedstoneEvent implements Listener {

    private Logger logger;

    public OnRedstoneEvent(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event)
    {
        ArrayList<LinkedBlock> preFilteredLinkedBlocks = Global.linkedBlocks.lookup(event.getBlock());
        if(preFilteredLinkedBlocks == null) return;

        List<LinkedBlock> matchedBlocks = preFilteredLinkedBlocks.stream().filter(
                p -> p.x == event.getBlock().getX()
                        && p.y == event.getBlock().getY()
                        && p.z == event.getBlock().getZ()
                        && p.isInbound == false
                        && p.materialType.equals(event.getBlock().getType().name())
        ).toList();
        try {
            for (LinkedBlock linkedBlock : matchedBlocks) {
                Block b = event.getBlock();
                if(linkedBlock.getLastPower() == b.getBlockPower()) continue;
                linkedBlock.setPastPower(b.getBlockPower());
                String topic = linkedBlock.topic;
                if(!linkedBlock.rootTopic) {
                    topic = Helper.getMqttEventPath(linkedBlock.topic);
                }
                if(b.isBlockPowered() && b.getBlockPower() > 7) {
                    if(!linkedBlock.onUp.trim().equals(""))
                        Global.mqtt.sendMessage(topic, linkedBlock.onUp.trim());
                }else{
                    if(!linkedBlock.onDown.trim().equals("")) {
                        Global.mqtt.sendMessage(topic, linkedBlock.onDown.trim());
                    }
                }
            }
        } catch (MqttException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onRedstoneEvent(BlockRedstoneEvent blockRedstoneEvent) {

    }

}
