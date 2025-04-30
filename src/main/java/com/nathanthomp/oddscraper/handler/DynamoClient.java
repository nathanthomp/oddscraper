package com.nathanthomp.oddscraper.handler;

import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoClient {
    private static DynamoClient instance = null;

    public static DynamoClient getInstance() {
        if (instance == null) {
            instance = new DynamoClient();
        }
        return instance;
    }

    private final Region DYNAMODB_REGION = Region.US_EAST_2;
    private final String DYNAMODB_TABLE_NAME = "Odds";

    private DynamoDbClient client;

    public DynamoClient() {
        client = DynamoDbClient.builder().region(DYNAMODB_REGION).build();
    }

    public void putItem(Map<String, AttributeValue> item) {
        PutItemRequest request = PutItemRequest.builder().tableName(DYNAMODB_TABLE_NAME).item(item).build();
        this.client.putItem(request);
    }
}
