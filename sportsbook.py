# Sport book class to define the location and method to get game odds
class Sportsbook:
    def __init__(self, name, url) -> None:
        self.name = name
        self.url = url
        self.games = []

    def __str__(self) -> str:
        return self.name + ' at ' + self.url

    # Method to download HTML
    def download_html(self) -> None:
        pass