resource "aws_lambda_function" "oddscraper_lambda_function" {
  function_name = "OddscraperLambdaFunction"
  role = aws_iam_role.oddscraper_lambda_function_role.arn

  runtime = "java21"
  handler = "com.nathanthomp.oddscraper.OddscraperRequestHandler::handleRequest"
  filename = "../target/oddscraper-1.0.0-SNAPSHOT.jar" # TODO: Dynamically get version
}

resource "aws_iam_role" "oddscraper_lambda_function_role" {
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

resource "aws_iam_policy" "secrets_manager_access_policy" {
  name = "SecretsManagerAccessPolicy"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action   = ["secretsmanager:GetSecretValue"],
        Effect   = "Allow",
        Resource = aws_secretsmanager_secret.oddscraper_secrets_manager.arn,
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_secrets_manager_access_policy" {
  role       = aws_iam_role.oddscraper_lambda_function_role.name
  policy_arn = aws_iam_policy.secrets_manager_access_policy.arn
}
