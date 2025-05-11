package com.nathanthomp.oddscraper.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.nathanthomp.oddscraper.odd.Entity;
import com.nathanthomp.oddscraper.odd.Event;
import com.nathanthomp.oddscraper.odd.Odd;
import com.nathanthomp.oddscraper.odd.Outcome;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.Put;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

public class DynamoClient {
    private static DynamoClient instance = null;

    public static DynamoClient getInstance() {
        if (instance == null) {
            instance = new DynamoClient();
        }
        return instance;
    }

    private final Region DYNAMODB_REGION = Region.US_EAST_2;
    private final String DYNAMODB_TABLE_NAME = "OddsTable";

    private DynamoDbClient client;

    public DynamoClient() {
        client = DynamoDbClient.builder().region(DYNAMODB_REGION).build();
    }

    /*
     * Behaviors:
     * 
     * 1. Add a new Event
     * Scenarios:
     * - First time scrape of event
     * - New odds have been published for a new event
     * Steps:
     * - Make sure that Event does not exist with condition expression
     * - Make a put item request
     * 
     * 2. Update an existing Event (new start time?)
     * 
     * 
     * 3. Add a new Outcome
     * 4. Update an existing Outcome
     * 
     * 5. Add a new Odd
     * 6. Update an existing Odd
     * 
     * 
     */

    public void put(Entity entity) {
        /*
         * entity: Event, if the PK combination exists,
         * entity: Odd, if PK combination exists and price is the same, dont update
         */
        Map<String, AttributeValue> item = entity.toItem();
        PutItemRequest putRequest = PutItemRequest.builder().tableName(DYNAMODB_TABLE_NAME).item(item)
                .conditionExpression("attribute_not_exists(PK)").build();
        try {
            PutItemResponse putResponse = this.client.putItem(putRequest);
        } catch (ConditionalCheckFailedException e) {
            /*
             * When the condition expression fails, entity exists with same PK and SK,
             * update that item instead
             * 
             * Event: Not entered at all. If time changes, this is unhandled
             * Outcome: Not entered at all
             * Odd: Entered if a odd exists, even if price has not changed
             * Odd: only want to enter if the price has changed
             */
            if (entity instanceof Odd) {
                this.updateOddEntity(entity);
            }
        } catch (Exception e) {
            // Log error
        }
    }

    public void updateOddEntity(Entity entity) {
        Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("PK", AttributeValue.builder().s(entity.getPartitionKey()).build());
        key.put("SK", AttributeValue.builder().s(entity.getSortKey()).build());

        Map<String, AttributeValueUpdate> updates = new HashMap<String, AttributeValueUpdate>();
        updates.put("ATTRIBUTES",
                AttributeValueUpdate.builder().value(AttributeValue.builder().m(entity.getAttributes()).build())
                        .action(AttributeAction.PUT).build());

        UpdateItemRequest updateRequest = UpdateItemRequest.builder().tableName(DYNAMODB_TABLE_NAME).key(key)
                .attributeUpdates(updates).build();

        UpdateItemResponse updateResponse = client.updateItem(updateRequest);
    }

    /*
     * Transaction: Unit of work
     * - Write 25 or less items
     * 
     * We want all writes to succeed. All or nothing
     */
    public void putItems(Collection<Map<String, AttributeValue>> items) {
        Collection<WriteRequest> writeRequests = new LinkedList<WriteRequest>();
        BatchWriteItemRequest request = BatchWriteItemRequest.builder()
                .requestItems(Map.of(DYNAMODB_TABLE_NAME, writeRequests)).build();
        BatchWriteItemResponse response = this.client.batchWriteItem(request);

        do {
            Map<String, List<WriteRequest>> unprocessedItems = response.unprocessedItems();
        } while (response.hasUnprocessedItems());

    }
}
