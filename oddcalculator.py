from money import Money


def calculate_payout(odds, stake):
    payout = stake * odds / 100 if odds > 0 else stake / (odds / -100)
    return round(payout + stake, 2)

def calculate_roi(payout, total_stake):
    roi = (payout - total_stake) / total_stake * 100
    return round(roi, 2)

def find_stakes(underdog_odds, favorite_odds):
    underdog_stake = 1000
    favorite_stake = 2000
    stakes = []

    underdog_payout = calculate_payout(underdog_odds, underdog_stake / 100)
    favorite_payout = calculate_payout(favorite_odds, favorite_stake / 100)

    if (underdog_payout < favorite_payout):
        while (underdog_payout <= favorite_payout):
            underdog_stake += 1
            favorite_stake -= 1

            underdog_payout = calculate_payout(underdog_odds, underdog_stake / 100)
            favorite_payout = calculate_payout(favorite_odds, favorite_stake / 100)
    elif (underdog_payout > favorite_payout):
        while (underdog_payout >= favorite_payout):
            underdog_stake -= 1
            favorite_stake += 1

            underdog_payout = calculate_payout(underdog_odds, underdog_stake / 100)
            favorite_payout = calculate_payout(favorite_odds, favorite_stake / 100)
    else:
        print('Not going to happen')

    roi = calculate_roi(underdog_payout, (underdog_stake + favorite_stake) / 100)

    stakes.append(underdog_stake / 100)
    stakes.append(favorite_stake / 100)

    return stakes

odds1 = 360 # Underdog odds
odds2 = -425 # Favorite odds
stake1 = 6.35
stake2 = 23.65
roi = -2.63

# stakes = find_stakes(125, -140) # Underdog, favorite (test case couldnt find equal)
stakes = find_stakes(360, -430) # Underdog, favorite




# for each bet in game
# odds1 = 240 # Max underdog odds among sportsbooks for game
# odds2 = -275 # Min favorite odds among sportsbooks for game

# stakes = get_ideal_payouts(odds1, odds2)
# stake1 = stakes[0]
# stake2 = stakes[1]

# Find the stakes that amount to the same payout

payout1 = calculate_payout(odds1, stake1)
payout2 = calculate_payout(odds2, stake2)

total_input = round(stake1 + stake2, 2)

print(f"payout1 = {payout1}")
print(f"payout2 = {payout2}")
print(f"total_input = {total_input}")
print(f"min profit = {round(min(payout1, payout2) - total_input, 2)} \t max profit = {round(max(payout1, payout2) - total_input, 2)}")
print(f"min ROI = {calculate_roi(min(payout1, payout2), total_input)}% \t max ROI = {calculate_roi(max(payout1, payout2), total_input)}%") 
# print(f"Calculated ROI = {find_roi(odds1, odds2)}%")
