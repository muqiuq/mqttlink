package ch.uisa.minecraft.mqttlink;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.block.Block;

public class LinkedBlock {

    public String materialType;

    public int x;
    public int y;
    public int z;

    public String onUp;

    public String onDown;

    public String topic;

    public boolean rootTopic = false;

    @JsonIgnore
    public int lastPower = -1;

    public boolean isInbound = false;

    public String world;

    @JsonIgnore
    public int id;

    public LinkedBlock(){}

    public int getLastPower() {
        return lastPower;
    }

    public int setPastPower(int newValue) {
        int oldValue = lastPower;
        lastPower = newValue;
        return oldValue;
    }

    public LinkedBlock(String world, int x, int y, int z, String materialType, String topic, String onUp, String onDown, boolean rootTopic) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rootTopic = rootTopic;
        this.onUp = onUp;
        this.onDown = onDown;
        this.topic = topic;
        this.materialType = materialType;
    }

    public LinkedBlock(Block b, String topic, String onUp, String onDown, boolean rootTopic) {
        this.world = b.getWorld().getName();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
        this.rootTopic = rootTopic;
        this.materialType = b.getType().name();
        this.onUp = onUp;
        this.onDown = onDown;
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "LB (" + String.valueOf(id) + "){" +
                " w='" + world + '\'' +
                " t='" + materialType + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", up='" + onUp + '\'' +
                ", down='" + onDown + '\'' +
                ", topic='" + topic + '\'' +
                ", lp=" + lastPower +
                '}';
    }

    public boolean  trySet(String key, String value) {
        if(key.equals("topic")) {
            topic = value;
            return true;
        }
        if(key.equals("up")) {
            onUp = value;
            return true;
        }
        if(key.equals("down")) {
            onDown = value;
            return true;
        }
        if(key.equals("t")) {
            materialType = value;
            return true;
        }
        if(key.equals("inbound")) {
            isInbound = value.toUpperCase().equals("TRUE");
            return true;
        }
        return false;
    }
}
