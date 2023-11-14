# mqttlink - bukkit compatible plugin

With mqttlink you can trigger mqtt messages with a redstone signal

tested with Version 1.9.3

## Commands
 - /mqttlink [topic] [on message] ([off message])
 	+ Will always link the focused block
 - /mqttdel [id]
 - /mqttedit [id] [key] [value]
 - /mqttlist
 
## Properties for each mqttlink entry (LinkedBlock):
 - `topic` - mqtt topic
 - `up` - on mqtt message content
 - down - off mqtt message content
 - t - material 
 - inbound - default false set true for inbound

## topic
If the topic starts with a `/` it will send to the root, if not provided the message will be send to mqttrootpath

## mqttlink.properties
required file in plugins folder
```
broker = tcp://host:1883
username =
password =
mqttrootpath = minecraft
mqtteventpath = events
```