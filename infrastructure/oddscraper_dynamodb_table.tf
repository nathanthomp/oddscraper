resource "aws_dynamodb_table" "oddscraper-dynamodb-table" {
    name = "Odds"
    billing_mode = "PAY_PER_REQUEST"
    read_capacity = 5
    write_capacity = 5

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

    attribute {
      name = "TYPE"
      type = "S"
    }

    attribute {
      name = "ATTRIBUTES"
      type = "M"
    }
}
