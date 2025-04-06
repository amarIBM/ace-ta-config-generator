# ace-ta-config-generator
Generates Configurations from ACE Transformation Advisor report


# How to Run the utility

Specify the path to your JSON  file - recommendations.json - which is generated from ACE TADataCollector utility through environment variable TADataCollectorOutput
For Windows platform :  set TADataCollectorOutput=C:\\temp\\output\\my.bar\\recommendations.json"
For Unix platform :  export TADataCollectorOutput=/tmp/output/my.bar/recommendations.json"

java -classpath "C:\ACET13\workspace\ACETA_ConfigGenerator\bin;C:\Downloads\jackson-databind-2.1.4.jar;C:\Downloads\jackson-core-2.2.3.jar;C:\Downloads\jackson-annotations-2.13.1.jar" com.ibm.ace.config.GenerateACEConfig

Console Output:

YAML file generated for TCPIP_Port at tcp_port.yaml
ID: IIB16   App: TCPIP_connection_03    Message Flow: flow1   Flow Node: TCPIP Server Input
XML file generated for MQPolicy at MQEndpoint.policyxml
ID: IIB19   App: TCPIP_connection_03    Message Flow: flow1   Flow Node: CLIENT_FAILURE
ID: IIB19   App: TCPIP_connection_03    Message Flow: flow1   Flow Node: SERVER_CATCH
ID: IIB19   App: TCPIP_connection_03    Message Flow: flow1   Flow Node: SERVER_FAILURE

The policyxml and yaml are generated in the current folder from where the java utility is executed.