resource "aws_secretsmanager_secret" "oddscraper-secrets-manager" {
  name = "OddscraperSecretsManager"

  recovery_window_in_days = 0
}
