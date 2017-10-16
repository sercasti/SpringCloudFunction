package com.globallogic.lambda.service;

import org.springframework.stereotype.Service;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
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

	public PersonResponse savePerson(PersonRequest personRequest) {
		this.initDynamoDbClient();
		persistData(personRequest);
		PersonResponse personResponse = new PersonResponse();
		personResponse.setMessage("Saved Successfully!!!");
		return personResponse;
	}

	private PutItemOutcome persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {
		return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
				.putItem(new PutItemSpec().withItem(new Item().withString("firstName", personRequest.getFirstName())
						.withString("lastName", personRequest.getLastName())));
	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}
}
