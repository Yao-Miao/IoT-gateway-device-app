# Gateway Device Application (Connected Devices)

## Lab Module 09

Be sure to implement all the PIOT-CDA-* issues (requirements) listed at [PIOT-INF-09-001 - Chapter 09](https://github.com/orgs/programming-the-iot/projects/1#column-10488503).

### Description

What does your implementation do? 

Build a robust yet lightweight request/response data communications client into the GDA using CoAP. Prove you can communicate between your GDA and CDA using this protocol and the server you built in Chapter 8. Send CoAP requests (GET, POST, PUT, DELETE) to the GDA CoAP server, and - optionally from the CDA - observe a resource hosted by your GDA using CoAP's observe functionality.

How does your implementation work?

1.	Create a new Java class named CoapClientConnector that implements the IRequestResponse interface.
2.	Add a custom response handler to process responses from asynchronous requests
3.	Add DISCOVER functionality to CoapClientConnector
4.	Add GET functionality to CoapClientConnector
5.	Add PUT functionality to CoapClientConnector
6.	Add POST functionality to CoapClientConnector
7.	Add DELETE functionality to CoapClientConnector
8.	Create a new Python class named CoapClientConnectorTest to test CoapClientConnector.

### Code Repository and Branch

URL: https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/chapter09

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter09/exercises/chapter09/GDA.png).


### Unit Tests Executed

- N/A

### Integration Tests Executed

- CoapClientConnectorTest 

EOF.
