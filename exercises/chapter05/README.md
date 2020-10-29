# Gateway Device Application (Connected Devices)

## Lab Module 05


### Description

What does your implementation do? 

Build data management and transformation capabilities into GDA by building a converter to serialize and de-serialize your data wrappers to / from JSON. Build a local storage capability into your GDA using Redis. Make GDA (Java) support JSON serialization.

How does your implementation work?

1.	Create Java modules that will contain the sensor and actuator data: SensorData, ActuatorData, SystemPerformanceData, and SystemStateData. These will all be derived from BaseIotData.
2.	Create a new / edit the existing Java class named DataUtil with class name DataUtil
3.	Update the BaseSystemUtilTask class to support telemetry generation using the SensorData type.
4.	Create a new Java class named DeviceDataManager in the programmingtheiot.gda.app package.
5.	Create an instance of DeviceDataManager within GatewayDeviceApp and invoke the manager's start / stop methods within the app's start / stop methods.


### Code Repository and Branch

URL: https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/tree/chapter05

### UML Design Diagram(s)

book [Programming the IoT](https://learning.oreilly.com/library/view/programming-the-internet/9781492081401/).


### Unit Tests Executed

- ActuatorDataTest
- SensorDataTest
- SystemPerformanceDataTest
- SystemStateDataTest
- DataUtilTest
- SystemCpuUtilTaskTest
- SystemMemUtilTaskTest


### Integration Tests Executed

- DataIntegrationTest
- DeviceDataManagerNoCommsTest
- GatewayDeviceAppTest


EOF.
