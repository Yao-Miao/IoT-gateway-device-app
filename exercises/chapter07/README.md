# Gateway Device Application (Connected Devices)

## Lab Module 07

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-07-001 - Chapter 07](https://github.com/orgs/programming-the-iot/projects/1#column-10488499).

### Description

What does your implementation do? 

Build a robust publish/subscribe (pub/sub) data communications capability into your GDA using MQTT. Prove you can communicate between your GDA and CDA using this protocol and an MQTT message broker server.

How does your implementation work?

1.	Create a Java class named MqttClientConnector that can interact with an MQTT broker
2.	Add callbacks to the MqttClientConnector module to handle the MQTT client events for connect, disconnect, and message received events.
3.	Add publish and subscribe capabilities to the MqttClientConnector module pub/sub functionality.
4.	Connect MqttClientConnector into DeviceDataManager.


### Code Repository and Branch

URL: https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/chapter07

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter07/exercises/chapter07/GDA.png).


### Unit Tests Executed

- N/A 

### Integration Tests Executed

- MqttClientConnectorTest 

EOF.
