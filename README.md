# Maven dependency analise

this plugin is to avoid duplicate classes by maven dependencies with different groupID

* To integrate

```xml
	<plugin>
		<groupId>com.github.ramonnteixeira</groupId>
		<artifactId>dependency-analize-plugin</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<executions>
			<execution>
				<goals>
					<goal>duplicated-class</goal>
				</goals>
			</execution>
		</executions>			
	</plugin>
```

* Configuration
It is possible to configure the exceptions list will be ignored for the plugin.

```xml
	<plugin>
		<groupId>com.github.ramonnteixeira</groupId>
		<artifactId>dependency-analize-plugin</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		<executions>
			<execution>
				<goals>
					<goal>duplicated-class</goal>
				</goals>
			</execution>
			<configuration>
				<exceptions>
					<exception>
						<dependency1>artifact-version.jar</dependency1>
						<dependency2>otherartifact-version.jar</dependency2>
					</exception>
				</exceptions>
			</configuration>	
		</executions>			
	</plugin>
```
