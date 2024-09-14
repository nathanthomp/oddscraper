import requests
from bs4 import BeautifulSoup

sportbooks = {"DraftKings"}

url = 'https://sportsbook.draftkings.com/leagues/football/nfl'

response = requests.get(url)
print(response.content)
soup = BeautifulSoup(response.content, 'html.parser')
#print(soup.prettify())

container = soup.find('div', class_='sportsbook-offer-category-card')
for card in container.findAll('div', class_='parlay-card-10-a'): # Multiple games
    table = card.find('table', class_='sportsbook-table')
    body = table.find('tbody', class_='sportsbook-table__body')
    # for row in body.findAll('tr'): # Corresponds to one team





def get_DraftKings_Odds():
    return ""