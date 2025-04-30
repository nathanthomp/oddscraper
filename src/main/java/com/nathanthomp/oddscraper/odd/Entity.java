package com.nathanthomp.oddscraper.odd;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public abstract class Entity {
    protected abstract String getPartitionKey();

    protected abstract String getSortKey();

    protected abstract String getType();

    protected abstract Map<String, AttributeValue> getAttributes();

    public Map<String, AttributeValue> toItem() {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("PK", AttributeValue.builder().s(this.getPartitionKey()).build());
        item.put("SK", AttributeValue.builder().s(this.getSortKey()).build());
        item.put("TYPE", AttributeValue.builder().s(this.getType()).build());
        item.put("ATTRIBUTES", AttributeValue.builder().m(this.getAttributes()).build());
        return item;
    }
}
