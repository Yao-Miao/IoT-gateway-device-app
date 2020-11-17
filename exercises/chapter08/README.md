# Gateway Device Application (Connected Devices)

## Lab Module 08

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-08-001 - Chapter 08](https://github.com/orgs/programming-the-iot/projects/1#column-10488501).

### Description

What does your implementation do? 

Build a robust yet lightweight request/response data communications server into the GDA using CoAP.

How does your implementation work?

1.	Install and configure Californium CoAP tools to support initial testing of the CoAP server.
2.	Create a Java class named CoapServerGateway that will provide your CoAP server functionality and host your local resource implementations.
3.	Create a Java class named GenericCoapResourceHandler that will provide the CoAP server functionality and host the local resource implementations.
4.	Update CoapServerGateway to create all resource handlers - one for each ResourceNameEnum resource name. Use CoapResource for all parent resources, and GenericCoapResourceHandler for the final 'leaf' nodes.
5.	Create a new integration test in programmingtheiot/part03/integration/connection named CoapServerGatewayTest

### Code Repository and Branch

URL: 

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter08/exercises/chapter08/GDA.png).


### Unit Tests Executed

- N/A 

### Integration Tests Executed

- CoapServerGatewayTest 

EOF.
