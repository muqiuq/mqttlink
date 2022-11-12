package ch.uisa.minecraft.mqttlink.commands;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.LinkedBlock;
import ch.uisa.minecraft.mqttlink.events.ReservedEvents;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockCommand implements CommandExecutor {

    private final Logger logger;

    public BlockCommand(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            Block targetBlock = player.getTargetBlockExact(5);

            if(strings.length < 2) {
                logger.log(Level.WARNING, "mqttlink missing arguments");
                return false;
            }

            if(targetBlock == null) return false;

            logger.info(String.valueOf(strings.length));

            boolean rootTopic = false;

            String topic = strings[0];
            if(topic.startsWith("/")) {
                rootTopic = true;
                topic = topic.substring(1);
            }

            // Could be overriden by using a rootTopic
            if(Arrays.asList(ReservedEvents.values()).contains(topic.toUpperCase())) {
                player.sendMessage("invalid topic. topic is a reserved keyword.");
                return false;
            }

            String upValue = strings[1];
            String downValue = "";

            if(topic.equals("")) {
                logger.log(Level.WARNING, "topic cannot be empty");
                return false;
            }

            if(strings.length >= 3) {
                downValue = strings[2];
            }

            LinkedBlock lb = new LinkedBlock(targetBlock, topic, upValue, downValue, rootTopic);

            if(!Global.linkedBlocks.tryAdd(lb)) {
                player.sendMessage("Could not add entry. Already exists.");
                return true;
            }

            player.sendMessage("Added new entry: " + lb.toString());

            try {
                Global.linkedBlocks.write();
            } catch (IOException e) {
                player.sendMessage("Could not save to file! Changes are not persistent!");
                logger.log(Level.SEVERE, "", e);
            }

            return true;
        }
        return false;
    }
}
