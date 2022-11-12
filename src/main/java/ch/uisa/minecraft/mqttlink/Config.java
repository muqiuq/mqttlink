package ch.uisa.minecraft.mqttlink;

import org.bukkit.Bukkit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {


    public static int qos             = 2;
    public static String broker       = "";
    public static String clientId     = "bukkit.mqttlink";
    public static String username     = "";
    public static String password     = "";

    public static String mqttRootPath = "";

    public static String mqttEventPath = "";


    public static void Load() throws IOException {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("plugins/mqttlink.properties")) {

            if (input == null) {
                Global.logger.log(Level.SEVERE, "Sorry, unable to find mqttlink.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            broker = prop.getProperty("broker");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            mqttRootPath = prop.getProperty("mqttrootpath", "minecraft");
            mqttEventPath = prop.getProperty("mqtteventpath", "events");

            if(mqttRootPath.endsWith("/")) {
                mqttRootPath = mqttRootPath.substring(0, mqttRootPath.length() - 1);
            }
            if(mqttRootPath.startsWith("/")) {
                mqttRootPath = mqttRootPath.substring(1);
            }
            if(mqttEventPath.endsWith("/")) {
                mqttEventPath = mqttEventPath.substring(0, mqttEventPath.length() - 1);
            }
            if(mqttEventPath.startsWith("/")) {
                mqttEventPath = mqttEventPath.substring(1);
            }


        } catch (IOException ex) {
            Global.logger.log(Level.SEVERE, "Could not load config file", ex);
            throw ex;
        }
    }





}
