resource "aws_dynamodb_table" "oddscraper-dynamodb-table" {
    name = "OddsTable"
    billing_mode = "PROVISIONED"
    read_capacity = 1
    write_capacity = 1

    stream_enabled = true
    stream_view_type = "NEW_AND_OLD_IMAGES"

    hash_key = "PK"
    range_key = "SK"

    attribute {
      name = "PK"
      type = "S"
    }

    attribute {
      name = "SK"
      type = "S"
    }

    # Only need to define key attributes

    # Not defining since it is NoSQL
    # attribute {
    #   name = "TYPE"
    #   type = "S"
    # }

    # Not defining since it is NoSQL
    # attribute {
    #   name = "ATTRIBUTES"
    #   type = "S"
    # }

    ttl {
      attribute_name = "TTL"
      enabled = true
    }
}
