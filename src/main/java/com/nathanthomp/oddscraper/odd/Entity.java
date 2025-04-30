package com.nathanthomp.oddscraper.odd;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public abstract class Entity {
    protected abstract String getPartitionKey();

    protected abstract String getSortKey();

    protected abstract String getType();

    public abstract Map<String, AttributeValue> toItem();

    protected Map<String, AttributeValue> itemKeysAndType() {
        Map<String, AttributeValue> keysAndTypeMap = new HashMap<String, AttributeValue>();
        keysAndTypeMap.put("PK", AttributeValue.builder().s(this.getPartitionKey()).build());
        keysAndTypeMap.put("SK", AttributeValue.builder().s(this.getSortKey()).build());
        keysAndTypeMap.put("TYPE", AttributeValue.builder().s(this.getType()).build());
        return keysAndTypeMap;
    }
}
