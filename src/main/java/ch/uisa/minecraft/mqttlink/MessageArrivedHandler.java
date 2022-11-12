package ch.uisa.minecraft.mqttlink;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MessageArrivedHandler {

    public void messageArrived(String topic, MqttMessage message) throws Exception;

}
