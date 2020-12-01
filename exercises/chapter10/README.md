# Gateway Device Application (Connected Devices)

## Lab Module 10

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-10-001 - Chapter 10](https://github.com/orgs/programming-the-iot/projects/1#column-10488510).

### Description

What does your implementation do? 

Add intelligent edge messaging using MQTT and / or CoAP to handle various messaging scenarios between the GDA and CDA. Use MQTT and / or CoAP to process sensor messages the CDA and trigger actuation events from both the CDA and GDA.

How does your implementation work?

1.	Update the Java class named MqttClientConnector so it can support TLS encrypted connections to the broker.
2.	Update MqttClientConnector to subscribe to the CDA's topics related to SensorData messages, SystemPerformanceData messages, and ActuatorData response messages.
3.	Add functionality to DeviceDataManager to handle incoming CDA messages: SensorData, SystemPerformanceData, and ActuatorData (response messages).
4.	The sensor and actuator data the GDA processes (and generates) will need to have a name associated with it, in addition to the type value that's already been set previously. In addition to providing consistency with the CDA's data structures, this is to allow mapping into a cloud-based virtualized representation of the device with a later exercise.
5.	Use the tests written for the CDA and GDA and your two implementations of MqttClientConnector to pass messages between the CDA and GDA.


### Code Repository and Branch

URL: 

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter10/exercises/chapter10/GDA.png).


### Unit Tests Executed

- ActuatorDataTest
- SensorDataTest
- SystemPerformanceDataTest
- SystemStateDataTest


### Integration Tests Executed

- MqttClientConnectorTest
- DataIntegrationTest

EOF.
