# Gateway Device Application (Connected Devices)

## Lab Module 01

Be sure to implement all the PIOT-GDA-* issues (requirements) listed at [PIOT-INF-01-001 - Chapter 01](https://github.com/orgs/programming-the-iot/projects/1#column-9974937).

### Description

NOTE: Include two full paragraphs describing your implementation approach by answering the questions listed below.

What does your implementation do? 

At first, set up my CDA dev environment by using the GitHub Classroom repository. Then run the unit/integration tests in eclipse, make sure all sample code works well. Finally, create and checkout a new branch in my git repository.

How does your implementation work?

At first, check the git, python, and pip version in my system, make sure they all fulfill the requirements of the project. Secondly, clone the code from my git repository to the local. Thirdly, install virtualenv to prepare a separate python running environment. Finally, In order to setup CDA project and run unit/integration tests in eclipse, I install the PyDev and config the venv python interpreter as the default interpreter. The challenge is that I can't find the python interpreter exe file in my .venv folder. So I copy the python interpreter to the .venv/bin path and it works well.

### Code Repository and Branch

NOTE: Be sure to include the branch (e.g. https://github.com/programming-the-iot/python-components/tree/alpha001).

URL: 

### UML Design Diagram(s)

NOTE: Include one or more UML designs representing your solution. It's expected each
diagram you provide will look similar to, but not the same as, its counterpart in the
book [Programming the IoT](https://learning.oreilly.com/library/view/programming-the-internet/9781492081401/).
![GDA_UML](https://github.com/NU-CSYE6530-Fall2020/gateway-device-app-MyronForNEU/blob/chapter01/exercises/chapter01/GDA_UML.jpg)


### Unit Tests Executed

NOTE: TA's will execute your unit tests. You only need to list each test case below
(e.g. ConfigUtilTest, DataUtilTest, etc). Be sure to include all previous tests, too,
since you need to ensure you haven't introduced regressions.

- ConfigUtilTest
- 
- 

### Integration Tests Executed

NOTE: TA's will execute most of your integration tests using their own environment, with
some exceptions (such as your cloud connectivity tests). In such cases, they'll review
your code to ensure it's correct. As for the tests you execute, you only need to list each
test case below (e.g. SensorSimAdapterManagerTest, DeviceDataManagerTest, etc.)

- GatewayDeviceAppTest
- 
- 

EOF.
