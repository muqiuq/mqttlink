package ch.uisa.minecraft.mqttlink;

public class Helper {

    public static String getMqttEventPath(String topic) {
        return Config.mqttRootPath + "/" + Config.mqttEventPath + "/" + topic;
    }

    public static String removeMqttEventPath(String topic) {
        String toRemove = Config.mqttRootPath + "/" + Config.mqttEventPath + "/";
        if(topic.startsWith(toRemove)) {
            return topic.substring(toRemove.length());
        }
        return topic;
    }

}
