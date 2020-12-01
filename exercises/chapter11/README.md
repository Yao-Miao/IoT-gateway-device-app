# Gateway Device Application (Connected Devices)

## Lab Module 11

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-11-001 - Chapter 11](https://github.com/orgs/programming-the-iot/projects/1#column-10488514).

### Description

What does your implementation do? 

Build cloud-integration functionality into your GDA using your GDA's MQTT client to connect to a variety of MQTT-enabled cloud services that provide IoT capabilities. Build the additional functionality needed to enable end-to-end communications between your CDA, GDA, and the cloud.

How does your implementation work?

1.	Setup and configure cloud service environment
2.	Update MqttClientConnector with some additional features that allow it to do the following: Load its configuration parameters from a different section of the PiotConfig.props configuration file. Allow a package-scoped class or sub-class to directly invoke publish, subscribe, and unsubscribe functions.
3.	Create a Java interface named ICloudClient
4.	Create a Java class named CloudClientConnector that implements ICloudClient
5.	implement the requisite services and functionality within your selected cloud service environment to do the following: Capture, collect, and store sensor data from the CDA and system performance data from both the CDA and GDA. Analyze the sensor data and trigger an LED actuation event based on thresholds you determine are best for your implementation. 


### Code Repository and Branch

URL: 

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter11/exercises/chapter11/GDA.png).


### Unit Tests Executed

- N/A 

### Integration Tests Executed

- MqttClientConnectorTest
- CloudClientConnectorTest 

EOF.
