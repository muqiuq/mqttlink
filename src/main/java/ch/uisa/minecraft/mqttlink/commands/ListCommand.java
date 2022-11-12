package ch.uisa.minecraft.mqttlink.commands;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.LinkedBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ListCommand  implements CommandExecutor {

    private final Logger logger;

    public ListCommand(Logger logger) {
        this.logger = logger;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        player.sendMessage("Found " + String.valueOf(Global.linkedBlocks.size()) + " entries:");

        for(LinkedBlock lb: Global.linkedBlocks.linkedBlocks) {
            player.sendMessage(lb.toString());
        }

        return true;
    }
}
