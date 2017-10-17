

# Running Spring Cloud Functions on AWS Lambda using the spring-cloud-function-adapter-aws

* * *

### Step 1: add the spring-cloud-function-adapter-aws dependency to your pom file

This project provides an emaple for a Spring Cloud Function application onto AWS Lambda. By writing an app with a single @Bean of type Function it will be deployable in AWS.

The adapter has a couple of generic request handlers that you can use. The most generic is SpringBootRequestHandler which you can extend, and provide the input and output types as type parameters.

If your app has more than one @Bean of type Function etc. then you can choose the one to use by configuring function.name (e.g. as FUNCTION_NAME environment variable in AWS).

Notes on JAR Layout
A Lambda application has to be shaded, but a Spring Boot standalone application does not, so you can run the same app using 2 separate jars (as per the sample here). The sample app creates 2 jar files, one with an aws classifier for deploying in Lambda, and one executable (thin) jar that includes spring-cloud-function-web at runtime.
