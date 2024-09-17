import json

# Load configurations
file = open('oddscraper.config.json')
configuration = json.load(file)

sportsbooks = []
for sportsbook in configuration['sportsbooks']:
    sportsbooks.append(sportsbook)

print(sportsbooks)
# Get Games -> Bets ()

# Ouptut data (top 5 ROI arbitrage bets)