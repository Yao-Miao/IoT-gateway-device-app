# Gateway Device Application (Connected Devices)

## Lab Module 02

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-02-001 - Chapter 02](https://github.com/orgs/programming-the-iot/projects/1#column-9974938).

### Description

What does your implementation do? 

Implement the Gateway Device App (GDA) in Java. Build an IoT performance monitoring applications to collect some simple telemetry about the gateway devices. In this exercise, the application will read and report on basic systems performance parameters, such as CPU utilization and memory utilization.

How does your implementation work?
1. Create a new Java class named GatewayDeviceApp.
2. Create the SystemPerformanceManager module.
3. Connect SystemPerformanceManager to GatewayDeviceApp so it can be started and stopped with the application. This work should be implemented within the GatewayDeviceApp class.
4. Create (edit) a new Java class named BaseSystemUtilTask.
5. Create the SystemCpuUtilTask module and implement the functionality to retrieve CPU utilization.
6. Create the SystemMemUtilTask module and implement the functionality to retrieve JVM memory utilization.
7. Connect SystemCpuUtilTask and SystemMemUtilTask into SystemPerformanceManager, and call each instance's handleTelemetry() method from within a thread that starts when the manager is started, and stops when the manager is stopped. This work should be implemented within the SystemPerformanceManager class.

### Code Repository and Branch

Chapter02 Branch URL: https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/chapter02

Main Branch URL:https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/alpha001

### UML Design Diagram(s)

![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter02/exercises/chapter02/GDA.jpg)

### Unit Tests Executed

- ConfigUtilTest
- SystemCpuUtilTaskTest
- SystemMemUtilTaskTest

### Integration Tests Executed

- GatewayDeviceAppTest
- SystemPerformanceManagerTest
- GatewayDeviceAppTest
- GatewayDeviceAppTest

EOF.
