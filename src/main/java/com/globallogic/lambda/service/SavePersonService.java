package com.globallogic.lambda.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.globallogic.lambda.domain.PersonRequest;
import com.globallogic.lambda.domain.PersonResponse;

@Service
public class SavePersonService {

	private DynamoDB dynamoDb;
	private String DYNAMODB_TABLE_NAME = "Person";
	private Regions REGION = Regions.US_EAST_1;
    private static Logger LOG = LoggerFactory.getLogger(SavePersonService.class);

	public PersonResponse savePerson(PersonRequest personRequest) {
		this.initDynamoDbClient();
		persistData(personRequest);
		PersonResponse personResponse = new PersonResponse();
		personResponse.setMessage("Saved Successfully!!!");
		return personResponse;
	}

	private PutItemOutcome persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {
		String uuid = UUID.randomUUID().toString();
		PrimaryKey pk = new PrimaryKey("id", uuid);
		LOG.info("Saving new person to the table with id: {} firstName: {} lastName: {}", uuid,
				personRequest.getFirstName(), personRequest.getLastName());
		return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
				.putItem(new PutItemSpec()
						.withItem(new Item().withPrimaryKey(pk).withString("firstName", personRequest.getFirstName())
								.withString("lastName", personRequest.getLastName())));
	}

	private void initDynamoDbClient() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
		this.dynamoDb = new DynamoDB(client);
	}
}
