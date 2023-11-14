package ch.uisa.minecraft.mqttlink;

import ch.uisa.minecraft.mqttlink.events.ReservedEvents;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NotifyStateThread
{

    private Instant lastGraciousNotification;
    private boolean stopRequested = false;
    private SendMessageHandler sendMessageHandler;
    private ScheduledExecutorService executorService;
    private OnlinePlayers onlinePlayers;

    long graciousNotificationWaitTime = 30;

    Runnable checkandnotify = new Runnable() {
        public void run() {
            try{
                Duration duration = Duration.between(lastGraciousNotification, Instant.now());
                if(onlinePlayers.hasChanged(true) || duration.getSeconds() > graciousNotificationWaitTime) {
                    lastGraciousNotification = Instant.now();
                    sendMessageHandler.sendMsg(
                            Helper.getMqttEventPath(ReservedEvents.PLAYERS_ONLINE.name()),
                            onlinePlayers.toString()
                    );
                }
            }catch(IOException e) {
                Global.logger.log(Level.SEVERE, "Online player notification failed", e);
            }
        }
    };

    public NotifyStateThread(
            SendMessageHandler sendMessageHandler, ScheduledExecutorService executorService, OnlinePlayers onlinePlayers) {

        this.sendMessageHandler = sendMessageHandler;
        this.executorService = executorService;
        this.onlinePlayers = onlinePlayers;
        lastGraciousNotification = Instant.now();

        executorService.scheduleAtFixedRate(checkandnotify, 0, 5, TimeUnit.SECONDS);
    }


}
