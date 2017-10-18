

# Running Spring Cloud Functions on AWS Lambda using the spring-cloud-function-adapter-aws


This project provides an example for a Spring Cloud Function application onto AWS Lambda. By writing an app with a single @Bean of type Function it will be deployable in AWS.

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

This will startup the Spring Boot context (only once, for the first request), and register a @Component("savePersonFunction").

From the AWS perspective, the main handler for this class will be this:
```java
public class SavePersonFunctionHandler extends SpringBootRequestHandler<PersonRequest, PersonResponse> {
}
``` 

And there is where the magic happens, the SpringBootRequestHandler will handle the request, given a function name invoked from AWS, it will try to find the Function, Consumer or Supplier with that name inside the spring context, and pass the request to the *apply* method of that implementation, in this case:

```java
@Component("savePersonFunction")
public class SavePersonFunction implements Function<PersonRequest, PersonResponse> {

	private final SavePersonService savePersonService;

	public SavePersonFunction(final SavePersonService savePersonService) {
		this.savePersonService = savePersonService;
	}

	@Override
	public PersonResponse apply(final PersonRequest personRequest) {
		return savePersonService.savePerson(personRequest);
	}
}
```

