import requests
import json

from bs4 import BeautifulSoup
from sportsbook import Sportsbook

def download_html(url):
    res = requests.get(url)
    res.raise_for_status()
    soup = BeautifulSoup(res.text, 'html.parser')
    return soup

def get_moneyline_odds():
    pass

def get_total_odds():
    pass

def get_spread_odds(odds, tag):
    pass

def get_odds(soup):
    odds = []

    odds_moneyline = soup.find(id='odds-table-moneyline--0')

    # Get sportsbooks by getting tags with class 'book-icn' and descendant
    # sportsbooks = []
    # first_child = odds_moneyline.contents[1]
    # for book in first_child
    #     print(book)



    return odds

def get_games():
    pass

# Main execution:
#   Get configurations as input
#   Manipulate data to download and parse games
#   Store data as output

# file = open('oddscraper.config.json')
# data = json.load(file)

url = 'http://www.vegasinsider.com/nfl/odds/las-vegas/'
soup = download_html(url)

games = get_games()

odds = get_odds(soup)






# sportsbooks = []
# for configuration in data['configurations']:
#     for sportsbook in configuration['sportsbooks']:
#         sportsbooks.append(Sportsbook(sportsbook['name'], sportsbook['url']))
  
# print(sportsbooks)

# draft_kings_sportbook = Sportsbook('DraftKings', 'https://sportsbook.draftkings.com/leagues/football/nfl')

# response = requests.get(draft_kings_sportbook.url)
# print(response.content)
# soup = BeautifulSoup(response.content, 'html.parser')
# #print(soup.prettify())

# container = soup.find('div', class_='sportsbook-offer-category-card')
# for card in container.findAll('div', class_='parlay-card-10-a'): # Multiple games
#     table = card.find('table', class_='sportsbook-table')
#     body = table.find('tbody', class_='sportsbook-table__body')
#     # for row in body.findAll('tr'): # Corresponds to one team
