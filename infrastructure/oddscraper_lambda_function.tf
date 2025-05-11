resource "aws_lambda_function" "oddscraper-lambda-function" {
  function_name = "OddscraperLambdaFunction"
  role = aws_iam_role.oddscraper-lambda-function-role.arn

  runtime = "java21"
  handler = "com.nathanthomp.oddscraper.OddscraperRequestHandler::handleRequest"
  filename = "../target/oddscraper-1.0.0-SNAPSHOT.jar" # TODO: Dynamically get version

  timeout = 60

  # Pass environment variables for DynamoDB database
}

resource "aws_iam_role" "oddscraper-lambda-function-role" {
  name = "OddscraperLambdaFunctionRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_policy" "oddscraper-lambda-function-dynamodb-policy" {
  name = "OddscraperLambdaFunctionDynamoDbPolicy"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action   = ["dynamodb:PutItem"], # TODO
        Effect   = "Allow",
        Resource = aws_dynamodb_table.oddscraper-dynamodb-table.arn # TODO
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "oddscraper-lambda-function-dynamodb-policy-attachment" {
  role       = aws_iam_role.oddscraper-lambda-function-role.name
  policy_arn = aws_iam_policy.oddscraper-lambda-function-dynamodb-policy.arn
}

resource "aws_iam_policy" "oddscraper-lambda-function-secrets-manager-policy" {
  name = "OddscraperLambdaFunctionSecretsManagerPolicy"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action   = ["secretsmanager:GetSecretValue"],
        Effect   = "Allow",
        Resource = aws_secretsmanager_secret.oddscraper-secrets-manager.arn
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "oddscraper-lambda-function-secrets-manager-policy-attachment" {
  role       = aws_iam_role.oddscraper-lambda-function-role.name
  policy_arn = aws_iam_policy.oddscraper-lambda-function-secrets-manager-policy.arn
}
