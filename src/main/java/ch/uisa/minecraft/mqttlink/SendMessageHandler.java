package ch.uisa.minecraft.mqttlink;

import java.io.IOException;

public interface SendMessageHandler {

    public void sendMsg(String topic, String message) throws IOException;

}
