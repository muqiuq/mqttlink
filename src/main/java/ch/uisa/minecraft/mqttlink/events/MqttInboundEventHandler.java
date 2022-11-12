package ch.uisa.minecraft.mqttlink.events;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.LinkedBlock;
import ch.uisa.minecraft.mqttlink.LinkedBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.material.Lever;

import java.util.logging.Logger;

public class MqttInboundEventHandler {

    private final Logger logger;
    private LinkedBlocks linkedBlocks;

    public MqttInboundEventHandler(Logger logger, LinkedBlocks linkedBlocks) {
        this.logger = logger;
        this.linkedBlocks = linkedBlocks;
    }

    public void triggerEvent(String topic, String value) {
        LinkedBlock linkedBlock = this.linkedBlocks.getByTopic(topic);
        if(linkedBlock != null) {
            Block b = Bukkit.getWorld(linkedBlock.world).getBlockAt(linkedBlock.x, linkedBlock.y, linkedBlock.z);

            if(!value.equals(linkedBlock.onUp) && !value.equals(linkedBlock.onDown)) return;

            if(b != null && b.getBlockData() instanceof Powerable) {
                Bukkit.getScheduler().runTask(Global.main, (() -> {
                    BlockData data = b.getBlockData();

                    Powerable powerable = (Powerable) data;
                    powerable.setPowered(value.equals(linkedBlock.onUp));
                    b.setBlockData(powerable);
                }));

                if(b.getType().name().endsWith("_BUTTON")) {
                    Bukkit.getScheduler().runTaskLater(Global.main, (() -> {
                        BlockData data = b.getBlockData();

                        Powerable powerable = (Powerable) data;
                        powerable.setPowered(false);
                        b.setBlockData(powerable);
                    }), 20);
                }

            }
        }
    }


}
