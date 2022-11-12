package ch.uisa.minecraft.mqttlink;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.block.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class LinkedBlocks {

    private int idCounter = 0;

    public ArrayList<LinkedBlock> linkedBlocks = new ArrayList<>();
    public String filename = "plugins/mqttlink.json";

    public HashMap<Long, ArrayList<LinkedBlock>> lookupTable = new HashMap<Long, ArrayList<LinkedBlock>>();

    public LinkedBlocks() {

    }

    public ArrayList<LinkedBlock> lookup(Block b) {
        long key = b.getX() * b.getY() * b.getZ();
        if(!lookupTable.containsKey(key)) return null;
        return lookupTable.get(key);
    }

    public boolean tryDelete(int id) {
        Optional<LinkedBlock> lb = linkedBlocks.stream().filter(p -> p.id == id).findFirst();
        if(lb.isPresent()) {
            linkedBlocks.remove(lb.get());
            long key = calculateKey(lb.get());
            lookupTable.get(key).remove(lb.get());
            return true;
        }
        return false;
    }

    private long calculateKey(LinkedBlock linkedBlock) {
        return (long)linkedBlock.x * (long)linkedBlock.y * (long)linkedBlock.z;
    }

    public boolean tryAdd(LinkedBlock linkedBlock) {
        linkedBlock.id = idCounter;
        idCounter++;
        if(linkedBlocks.stream().anyMatch(p -> p.x == linkedBlock.x && p.y == linkedBlock.y && p.z == linkedBlock.z)) {
            return false;
        }
        linkedBlocks.add(linkedBlock);
        long key = calculateKey(linkedBlock);
        if(!lookupTable.containsKey(key)) {
            lookupTable.put(key, new ArrayList<LinkedBlock>());
        }
        lookupTable.get(key).add(linkedBlock);
        return true;
    }

    public void open() throws IOException {
        if((new File(filename)).exists()) {
            ObjectMapper objectMapper = new ObjectMapper();

            ArrayList<LinkedBlock> loadedLinkedBlocks = objectMapper.readValue(new FileReader(filename), new TypeReference<ArrayList<LinkedBlock>>() {});

            for(LinkedBlock linkedBlock: loadedLinkedBlocks) {
                if(linkedBlock.world == null) linkedBlock.world = "world";
                tryAdd(linkedBlock);
            }
        }
    }

    public void write() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(new FileWriter(filename), linkedBlocks);
    }

    public int size() {
        return linkedBlocks.size();
    }

    public LinkedBlock get(int id) {
        Optional<LinkedBlock> lb = linkedBlocks.stream().filter(p -> p.id == id).findFirst();
        if(lb.isPresent()) {
            return lb.get();
        }
        return null;
    }

    public LinkedBlock getByTopic(String topic) {
        Optional<LinkedBlock> lb1 = linkedBlocks.stream().filter(p -> p.topic.equals(topic) && p.rootTopic).findFirst();
        Optional<LinkedBlock> lb2 = linkedBlocks.stream().filter(p ->
                p.topic.equals(Helper.removeMqttEventPath(topic)) &&
                !p.rootTopic)
                .findFirst();
        if(lb1.isPresent()) {
            return lb1.get();
        }
        if(lb2.isPresent()) {
            return lb2.get();
        }
        return null;
    }
}
