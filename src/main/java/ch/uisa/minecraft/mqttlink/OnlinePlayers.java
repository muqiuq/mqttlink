package ch.uisa.minecraft.mqttlink;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class OnlinePlayers {

    public ArrayList<String> players = new ArrayList<>();

    private boolean changed = false;

    public void playerVentOnline(Player p) {
        String playerName = p.getName();
        if(!players.contains(playerName)) {
            players.add(playerName);
        }
        changed = true;
    }

    public void playerVentOffline(Player p) {
        String playerName = p.getName();
        if(players.contains(playerName)) {
            players.remove(playerName);
        }
        changed = true;
    }

    public boolean hasChanged(boolean reset) {
        boolean originalState = changed;
        if(reset) {
            changed = false;
        }
        return originalState;
    }

    @Override
    public String toString() {
        return String.join(",", players);
    }
}
