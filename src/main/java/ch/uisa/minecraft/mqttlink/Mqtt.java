package ch.uisa.minecraft.mqttlink;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mqtt implements MqttCallback {

    int qos             = 2;
    String broker       = "tcp://host:1883";
    String clientId     = "bukkit.mqttlink";
    String username     = "";
    String password     = "";

    private MqttClient client;

    Logger logger;
    private Thread controlThread;
    private volatile boolean isRunning;

    private ArrayList<String> subscriptions = new ArrayList<>();
    private ArrayList<MessageArrivedHandler> handlers = new ArrayList<>();

    public Mqtt(java.util.logging.Logger logger, String broker, String username, String password) {
        this.logger = logger;
        this.broker = broker;
        this.username = username;
        this.password = password;
    }

    public void sendMessage(String topic, String content) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
        logger.info(topic + " = " + message);
    }

//    public void sendEventMessage(String topic, String content) throws MqttException {
//        this.sendMessage(Config.mqttRootPath + "/" + Config.mqttEventPath + "/" + topic, content);
    //}

    public void subscribe(String topic) {
        if(!subscriptions.contains(topic)) {
            try {
                client.subscribe(topic);
                logger.log(Level.FINE, "Subscribed to " + topic);
            } catch (MqttException e) {
                logger.log(Level.SEVERE, "Error subscribing to new topic", e);
                return;
            }
            subscriptions.add(topic);
        }
    }

    public void start() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        connOpts.setCleanSession(true);
        client.setCallback(this);
        logger.info("Connecting to broker: "+broker);
        client.connect(connOpts);
        logger.info("Connected");
        isRunning = true;
        controlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(isRunning) {
                        if(!client.isConnected()) {
                            client.reconnect();
                        }
                        Thread.sleep(1000l);
                    }
                }catch(InterruptedException | MqttException e) {
                    logger.log(Level.SEVERE, "", e);
                }

            }
        });
        controlThread.start();

    }

    public void addHandler(MessageArrivedHandler handler) {
        this.handlers.add(handler);
    }

    public void stop() throws MqttException {
        isRunning = false;
        client.disconnect();
        logger.info("Disconnected");
    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        for(MessageArrivedHandler handler: handlers) {
            try{
                handler.messageArrived(topic, message);
            }catch(Exception e) {
                logger.log(Level.SEVERE, "Handler Error", e);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
