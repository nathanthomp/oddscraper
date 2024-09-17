
def calculate_payout(odds, stake):
    payout = stake * odds / 100 if odds > 0 else stake / (odds / -100)
    return round(payout + stake, 2)

def calculate_roi(payout, total_stake):
    roi = (payout - total_stake) / total_stake * 100
    return round(roi, 2)

def get_ideal_payouts(odds1, odds2):
    stakes = []

    # stakes.append(10.02)
    # stakes.append(19.98)
    stakes.append(550.00)
    stakes.append(723.34)
    
    return stakes

odds1 = 150
odds2 = -111

stake1 = 550.00
stake2 = 723.34

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
print(f"min ROI = {calculate_roi(min(payout1, payout2), total_input)}% \t max ROI = {calculate_roi(max(payout1, payout2), total_input)}%") # Should be 7.39%
