

# Running Spring Cloud Functions on AWS Lambda using the spring-cloud-function-adapter-aws


This project provides an emaple for a Spring Cloud Function application onto AWS Lambda. By writing an app with a single @Bean of type Function it will be deployable in AWS.

The adapter has a couple of generic request handlers that you can use. The most generic is SpringBootRequestHandler which you can extend, and provide the input and output types as type parameters.

If your app has more than one @Bean of type Function etc. then you can choose the one to use by configuring function.name (e.g. as FUNCTION_NAME environment variable in AWS).

Notes on JAR Layout
A Lambda application has to be shaded, but a Spring Boot standalone application does not, so you can run the same app using 2 separate jars (as per the sample here). The sample app creates 2 jar files, one with an aws classifier for deploying in Lambda, and one executable (thin) jar that includes spring-cloud-function-web at runtime.

* * *

### Step 1: add the spring-cloud-function-adapter-aws dependency to your pom file

                <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-function-adapter-aws</artifactId>
			<version>1.0.0.BUILD-SNAPSHOT</version>
		</dependency>
  
### Step 2: add the dynamodb dependency to your pom file to be able to save to dynamoDB

                <dependency>
			<groupId>com.amazonaws</groupId>
			<artifactId>aws-java-sdk-dynamodb</artifactId>
		</dependency>
   
### Step 3: use maven shade to generate the uber jar for AWS
        <build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-shade-plugin</artifactId>
				<configuration>
					<createDependencyReducedPom>false</createDependencyReducedPom>
					<shadedArtifactAttached>true</shadedArtifactAttached>
					<shadedClassifierName>aws</shadedClassifierName>
				</configuration>
			</plugin>
		</plugins>
	</build>   
    
### Step 4: add slf4j logging (optional) dependency

                <dependency>
			<groupId>io.symphonia</groupId>
			<artifactId>lambda-logging</artifactId>
			<version>1.0.0</version>
		</dependency>
		
The way this combination works is by using the typical spring boot main class:
```java
    @SpringBootApplication
    public class SavePersonFunctionApplication {
	public static void main(String[] args) throws Exception {
	SpringApplication.run(SavePersonFunctionApplication.class, args);
        }
    } 
```
