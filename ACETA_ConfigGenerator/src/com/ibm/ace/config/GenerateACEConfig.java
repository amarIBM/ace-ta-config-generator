package com.ibm.ace.config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class GenerateACEConfig {

    public static void main(String[] args) {
        try {
            // Specify the path to your JSON  file - recommendations.json - which is generated from ACE TADataCollector utility
        	// For Windows platform :  set TADataCollectorOutput=C:\\temp\\output\\my.bar\\recommendations.json"             	 
        	// For Unix platform :  export TADataCollectorOutput=/tmp/output/my.bar/recommendations.json"
        	
        	String jsonFileName = System.getenv("TADataCollectorOutput");
        	if (jsonFileName == null) {
                System.out.println ("Environment variable " + "TADataCollectorOutput" + " is not set.");
                return;
            }
        	File jsonFile = new File (jsonFileName);
            
            // Create ObjectMapper instance to parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Read the JSON file into a JsonNode (a tree structure for JSON)
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Navigate to the "assessmentUnits" node
            JsonNode assessmentUnitsNode = rootNode.path("assessmentUnits");

            // Loop through the "assessmentUnits" array if it exists
            if (assessmentUnitsNode.isArray()) {
                for (JsonNode unit : assessmentUnitsNode) {
                    // Iterate through "targets" inside each assessment unit
                    JsonNode targetsNode = unit.path("targets");
                    if (targetsNode.isArray()) {
                        for (JsonNode target : targetsNode) {
                            // Navigate to "issues" inside each target
                            JsonNode issuesNode = target.path("issues");

                            // Loop through the issues and extract details
                            issuesNode.fieldNames().forEachRemaining(issueKey -> {
                                JsonNode issuesArray = issuesNode.path(issueKey);
                                if (issuesArray.isArray()) {
                                    for (JsonNode issue : issuesArray) {
                                        // Extract the "id" of the issue
                                        JsonNode idNode = issue.path("id");
                                        String id ="";
                                        if (!idNode.isMissingNode()) {
                                            id = idNode.asText();
                                                                                 
                                            if ("IIB05".equals(id)) {
                                                // Generate XML content for IIB05
                                                generateXMLFile("FilePolicy");
                                            } else if ("IIB19".equals(id) || "IIB07".equals(id) || "IIB08".equals(id) || "IIB09".equals(id) || "IIB10".equals(id) || "IIB11".equals(id)) {
                                                // Generate XML content for IIB19
                                                generateXMLFile("MQPolicy");
                                            } else if ("IIB31".equals(id)) {
                                                // Generate XML content for IIB19
                                                generateXMLFile("ActivityLog");
                                            } else if ("IIB30".equals(id)) {
                                                // Generate XML content for IIB19
                                                generateXMLFile("udp");
                                            } else if ("IIB16".equals(id)) {
                                                // Generate YAML content for IIB16
                                                generateYAMLFile("TCPIP_Port");
                                            } else if ("IIB34".equals(id)) {
                                            // Generate XML content for IIB34
                                            	generateXMLFile("MQTTPublish");
                                            } else if ("IIB35".equals(id)) {
                                            // Generate XML content for IIB35
                                            	generateXMLFile("MQTTSubscribe");
                                            }
                                        }
                                        // Loop through occurrences and print details
                                        JsonNode occurrencesNode = issue.path("occurrences");
                                        if (occurrencesNode.isArray()) {
                                            for (JsonNode occurrence : occurrencesNode) {
                                                // Extract and print app, messageFlow, and flowNode
                                                JsonNode appNode = occurrence.path("app");
                                                JsonNode messageFlowNode = occurrence.path("messageFlow");
                                                JsonNode flowNode = occurrence.path("flowNode");
                                                System.out.print("ID: " + id);
                                                if (!appNode.isMissingNode()) {
                                                    System.out.print("   App: " + appNode.asText());
                                                }
                                                if (!messageFlowNode.isMissingNode()) {
                                                    System.out.print("    Message Flow: " + messageFlowNode.asText());
                                                }
                                                if (!flowNode.isMissingNode()) {
                                                    System.out.println("   Flow Node: " + flowNode.asText());
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            } else {
                System.out.println("No assessmentUnits found.");
            }

        } catch (IOException e) {
            // Handle potential exceptions
            e.printStackTrace();
        }
    }

    private static void generateXMLFile(String id) {
        // Specify the path where you want to generate the XML file
        String xmlFilePath;
        String xmlContent = "";

        if ("FilePolicy".equals(id)) {
            xmlFilePath = "FileServer.policyxml";
            xmlContent = generateFileServerXML();
        } else if ("MQPolicy".equals(id)) {
            xmlFilePath = "MQEndpoint.policyxml";
            xmlContent = generateMQEndpointXML();
        } else if ("ActivityLog".equals(id)) {
        	xmlFilePath = "ActivityLog.policyxml";
            xmlContent = generateActivityLogXML();
        } else if ("udp".equals(id)) {
        	xmlFilePath = "udp.policyxml";
            xmlContent = generateUDPXML();
        } else if ("MQTTPublish".equals(id)) {
        	xmlFilePath = "MQTTPublish.policyxml";
            xmlContent = generateMQTTPublishXML();
        } else if ("MQTTSubscribe".equals(id)) {
        	xmlFilePath = "MQTTSubscribe.policyxml";
            xmlContent = generateMQTTSubscribeXML();
        }         else {
            return;
        }

        // Generate XML file based on the id
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFilePath))) {
            writer.write(xmlContent);
            System.out.println("XML file generated for " + id + " at " + xmlFilePath);
        } catch (IOException e) {
            System.out.println("An error occurred while generating the XML file.");
            e.printStackTrace();
        }
    }
    private static void generateYAMLFile(String id) {
    	
    	String yamlFilePath;
        String yamlContent = "";
        
    	if ("TCPIP_Port".equals(id)) {
            yamlFilePath = "tcp_port.yaml";
            yamlContent = generateTCPServiceYAML();
        
        } else {
            return;
        }

        // Generate YAML file based on the id
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(yamlFilePath))) {
            writer.write(yamlContent);
            System.out.println("YAML file generated for " + id + " at " + yamlFilePath);
        } catch (IOException e) {
            System.out.println("An error occurred while generating the YAML file.");
            e.printStackTrace();
        }
    }
    private static String generateTCPServiceYAML() {
    	
                 return "spec:\n" +
    	                "  service:\n" +
    	                "    ports:\n" +
    	                "      - protocol: TCP\n" +
    	                "        name: config-abc\n" +
    	                "        nodePort: 32000\n" +
    	                "        port: 9910\n" +
    	                "        targetPort: 9920\n" +
    	                "      - protocol: SCTP\n" +
    	                "        name: config-xyz\n" +
    	                "        nodePort: 31500\n" +
    	                "        port: 9376\n" +
    	                "        targetPort: 9999\n" +
    	                "    type: NodePort\n";

    	          
    }

    private static String generateFileServerXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<policies>\n" +
               "  <policy policyType=\"FtpServer\" policyName=\"remoteftp\" policyTemplate=\"FtpServer\">\n" +
               "    <fileFtpServer>xxxxx</fileFtpServer>\n" +
               "    <fileFtpAccountInfo></fileFtpAccountInfo>\n" +
               "    <fileFtpDirectory></fileFtpDirectory>\n" +
               "    <fileFtpUser></fileFtpUser>\n" +
               "    <fileFtpTransferMode></fileFtpTransferMode>\n" +
               "    <fileFtpScanDelay></fileFtpScanDelay>\n" +
               "    <fileFtpConnectionType>PASSIVE</fileFtpConnectionType>\n" +
               "    <strictHostKeyChecking>false</strictHostKeyChecking>\n" +
               "    <allowedCiphers></allowedCiphers>\n" +
               "    <fileFtpCompression>0</fileFtpCompression>\n" +
               "    <remoteTransferType></remoteTransferType>\n" +
               "    <knownHostsFile></knownHostsFile>\n" +
               "    <sslProtocol>TLSv1.2</sslProtocol>\n" +
               "    <mac></mac>\n" +
               "    <preserveRemoteFileDate>false</preserveRemoteFileDate>\n" +
               "    <timeoutInterval>0</timeoutInterval>\n" +
               "  </policy>\n" +
               "</policies>\n";
    }

    private static String generateMQEndpointXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<policies>\n" +
               "  <policy policyType=\"MQEndpoint\" policyName=\"mqendpoint\" policyTemplate=\"MQEndpoint\">\n" +
               "    <connection>CLIENT</connection>\n" +
               "    <destinationQueueManagerName></destinationQueueManagerName>\n" +
               "    <queueManagerHostname></queueManagerHostname>\n" +
               "    <listenerPortNumber>1414</listenerPortNumber>\n" +
               "    <channelName></channelName>\n" +
               "    <CCDTUrl></CCDTUrl>\n" +
               "    <securityIdentity></securityIdentity>\n" +
               "    <useSSL>false</useSSL>\n" +
               "    <SSLPeerName></SSLPeerName>\n" +
               "    <SSLCipherSpec></SSLCipherSpec>\n" +
               "    <SSLCertificateLabel></SSLCertificateLabel>\n" +
               "    <MQApplName></MQApplName>\n" +
               "    <reconnectOption>default</reconnectOption>\n" +
               "  </policy>\n" +
               "</policies>\n";
    }
    private static String generateActivityLogXML() {
        // XML content for IIB31 (Activity Log)
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<policies>\n" +
               "  <policy policyType=\"ActivityLog\" policyName=\"activitylog\" policyTemplate=\"ActivityLog\">\n" +
               "    <enabled>true</enabled>\n" +
               "    <filter></filter>\n" +
               "    <minSeverityLevel>INFORMATIONAL</minSeverityLevel>\n" +
               "    <elkLog>false</elkLog>\n" +
               "    <elkConnections></elkConnections>\n" +
               "    <fileName></fileName>\n" +
               "    <numberOfLogs>4</numberOfLogs>\n" +
               "    <maxAgeMins>0</maxAgeMins>\n" +
               "    <maxFileSizeMb>25</maxFileSizeMb>\n" +
               "    <formatEntries>false</formatEntries>\n" +
               "    <consoleLog>false</consoleLog>\n" +
               "    <consoleLogFormat>text</consoleLogFormat>\n" +
               "    <additionalJsonElements></additionalJsonElements>\n" +
               "  </policy>\n" +
               "</policies>\n";
    }
    
    private static String generateUDPXML() {
        // XML content for IIB31 (Activity Log)
    	 return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    	    "<policies>\n" +
    	      "<policy policyType=\"UserDefined\" policyName=\"udp\" policyTemplate=\"UserDefined\">\n"+
    	        "<Name>Joe</Name>\n"+
    	        "<Age>20</Age>\n"+
    	      "</policy>\n"+
    	    "</policies>\n";
    }
    private static String generateMQTTPublishXML() {
    	StringBuilder xmlContent = new StringBuilder();

        // Building the XML content
        xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                  .append("<policies>\n")
                  .append("  <policy policyType=\"MQTTPublish\" policyName=\"mqttpublish\" policyTemplate=\"MQTTPublish\">\n")
                  .append("    <clientId></clientId>\n")
                  .append("    <topicName></topicName>\n")
                  .append("    <hostName></hostName>\n")
                  .append("    <port>1883</port>\n")
                  .append("    <qos>0</qos>\n")
                  .append("    <securityIdentity></securityIdentity>\n")
                  .append("    <useSSL>false</useSSL>\n")
                  .append("  </policy>\n")
                  .append("</policies>");

        return xmlContent.toString();
    
    }
    
    private static String generateMQTTSubscribeXML() {
    	StringBuilder xmlContent = new StringBuilder();

        // Building the XML content for MQTTSubscribe
        xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                  .append("<policies>\n")
                  .append("  <policy policyType=\"MQTTSubscribe\" policyName=\"mqttsubscribe\" policyTemplate=\"MQTTSubscribe\">\n")
                  .append("    <clientId></clientId>\n")
                  .append("    <topicName></topicName>\n")
                  .append("    <hostName></hostName>\n")
                  .append("    <port>1883</port>\n")
                  .append("    <qos>0</qos>\n")
                  .append("    <securityIdentity></securityIdentity>\n")
                  .append("    <useSSL>false</useSSL>\n")
                  .append("  </policy>\n")
                  .append("</policies>");

        return xmlContent.toString();
    }
   
   
}
