# Gateway Device Application (Connected Devices)

## Lab Module 12 - Semester Project - GDA Components

### Description

What does your implementation do? 

1.	Connect to the CDA using at least 1 protocol
2.	Connect to the cloud using at least 1 protocol
3.	Process system performance data, sensor data, and actuator command responses from the CDA
4.	Analyze one sensor data value from the CDA, and generate an appropriate actuation event based on an algorithm of your choosing. This must be different than the actuation event the CDA will generate for itself based on a configured threshold crossing.
5.	Collect internal system performance data for CPU, memory and disk util (these can be averaged)
6.	Store at least one data sample (the latest) from the CDA (sensor data and system performance data) plus at least one data sample from internal system performance data within a local storage data repository such as Redis (others are OK - coordinate with the TA's for approval if not Redis)
7.	Send all CDA sensor and system performance data to the cloud service
8.	Send all internal system performance data to the cloud service
9.	Connect to the cloud service topic that will notify the GDA upon cloud-based event and use that event to trigger an actuation event that will then be sent to the CDA
10.	Run for at least 1 hour without interruption, and collect / send at least 30 system performance samples each from the CDA and internally, 30 sensor data samples from the CDA, and trigger at least 2 actuator events based on GDA logic, and at least 2 actuator events based on cloud notifications

How does your implementation work?

1.	Upate CloudClientConnector class to use the callback function to send data to cloud service.
2.	Update MqttClientConnector to receive the data from cda and use the callback function to handle the data.
3.	Update CoapClientConnector class to send actuator events to CDA.
4.	Update RedisPersistenceAdapter class to store the data to local database.
5.	Update DeviceDataManager class.


### Code Repository and Branch

URL: https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/chapter12

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/GDA.png).


### Unit Tests Executed


### Integration Tests Executed

- MqttClientConnectorTest
- CloudClientConnectorTest
- CoapServerGatewayTest
- CoapClientConnectorTest

### Cloud Data

![CPU](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/CDA_CPU.jpg).
![ME](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/CDA_ME.jpg).
![SOIL](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/Soil.jpg).
![TEMP](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/Temp.jpg).
![PRESSURE](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter12/exercises/chapter12/Pressure.jpg).



EOF.
