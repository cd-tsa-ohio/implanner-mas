# implanner-mas
## How to run IMPlanner-MAS in a distributed environment?
Differet agents in IMPlanner-MAS can be hosted in different containers runnning in different machines. 
However the same strategy can be applied while running every agent in the same machine, just by changing the configuration files.
We used -conf modifier, which let Jade read the configurations from the supplied configuration file.

`-conf  Specifies a property file. All options specified in that file are used to launch
JADE.`

The following configuration files are vailable in src/main/resources/META-INF/
### Configuration files
Configurations files sets agent name and class name along with arguments for agent soecifier format, given below. Other keys, used to set the container configuration, are given below.
`AgentSpecifier = AgentName ":" ClassName ("(" ArgumentList ")")`
```
-host specifies the host name where the main container to register with is running; its value is defaulted to localhost. This option is typically specified on peripheral
containers.     
-port  specifies the port number where the main container to register with is running; its value is defaulted to 1099. This option is typically specified on peripheral
containers. 
-gui   specifies that the RMA (Remote Monitoring Agent) GUI of JADE should be
launched (by default this option is unselected)   
-local-host specifies the host name where this container is going to run; its value is defaulted to localhost. A typical example of this kind of usage is to include the full domain of the host (e.g. –host kim.cselt.it  when the localhost would have returned just ‘kim’)  such that the main-container can be contacted even from
outside the local domain.     
-local-port   this option allows to specify the port number where this container can be
contacted. By default the port number 1099 is used.   
```
1. jade-main-container.properties :: Start the main container with gui on the local machine
2. jade-agent-mas.properties :: can be used to run every agent once main agent is started
3. jade-agent-system.properties :: can be used to run only the System Agent
4. jade-agent-dealers.properties :: runs dealer agents
5. jade-agent-vendors.properties :: runs service agents

### Execute
Easiest way to run agents is by Eclipse Run config. 
* Project :: implanner-mas
* Main class :: jade.Boot
* Program argument :: -conf src/main/resources/META-INF/<property file>

POM.xml has permanent profile configuration as following
```
         <profile>
            <id>jade-main</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>exec-maven-plugin</artifactId>
                        <version>1.3.2</version>
                        <configuration>
                            <mainClass>jade.Boot</mainClass>
                            <arguments>
                                <argument>-conf</argument>
                                <argument>src/main/resources/META-INF/jade-main-container.properties</argument>
                            </arguments>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
```
A Maven build can be set as follows
* Goals :: exec:java
* profiles :: jade-main

