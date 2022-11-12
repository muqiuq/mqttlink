package ch.uisa.minecraft.mqttlink.commands;

import ch.uisa.minecraft.mqttlink.Global;
import ch.uisa.minecraft.mqttlink.LinkedBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditCommand implements CommandExecutor {

    private final Logger logger;

    public EditCommand(Logger logger) {
        this.logger = logger;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(strings.length != 3) {
            return false;
        }

        int id = 0;
        try{
            id = Integer.parseInt(strings[0]);
        }catch(NumberFormatException e) {
            player.sendMessage("could not parse id");
            return true;
        }

        LinkedBlock lb = Global.linkedBlocks.get(id);

        if(lb == null) {
            player.sendMessage("id not found");
            return true;
        }

        String key = strings[1].trim();
        String value = strings[2].trim();

        if(lb.trySet(key, value)) {
            player.sendMessage("" + key + " = " + value);
            try {
                Global.linkedBlocks.write();
            } catch (IOException e) {
                player.sendMessage("Could not save to file! Changes are not persistent!");
                logger.log(Level.SEVERE, "", e);
            }
            if(key.equals("inbound")) {
                Global.updateLinkedBlocksSubscriptions();
            }
        }else{
            player.sendMessage("key not found");
        }

        return true;
    }
}