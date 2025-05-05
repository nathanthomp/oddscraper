package com.nathanthomp.oddscraper.odd;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public abstract class Entity {
    public abstract String getPartitionKey();

    public abstract String getSortKey();

    protected abstract String getType();

    public abstract Map<String, AttributeValue> getAttributes();

    protected abstract String getTimeToLive();

    public Map<String, AttributeValue> toItem() {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("PK", AttributeValue.builder().s(this.getPartitionKey()).build());
        item.put("SK", AttributeValue.builder().s(this.getSortKey()).build());
        item.put("TYPE", AttributeValue.builder().s(this.getType()).build());
        item.put("ATTRIBUTES", AttributeValue.builder().m(this.getAttributes()).build());
        item.put("TTL", AttributeValue.builder().s(this.getTimeToLive()).build());
        return item;
    }
}
