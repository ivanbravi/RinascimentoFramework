import numpy as np

rh_space = [
    {"a": ["F","T"],
      "name": "Use Shift Buffer"},
    {"a": [1,2,3,5,10,20],
      "name": "Sequence Length"},
    {"a": [20,50,100,200],
      "name": "Number of Evaluations"},
    {"a": ["F","T"],
      "name": "Flip at least one"},
    {"a": [0,1,2],
      "name": "Mutation Style"},
    {"a": [0,1,2],
      "name": "Opponent Type"},
    {"a": [0.005,0.01,0.02,0.05],
      "name": "Opponent Budget Share"},
    {"a": [0.5,0.7,0.8,0.9],
      "name": "Exponential Decay"},
    {"a": [0.0,0.1,0.3,0.5,0.75],
      "name": "Gaussian Mean"},
    {"a": [0.5,1.0,2.0],
      "name": "Gaussian Standard Dev"}]
w_space = (np.linspace(-1,1,11))

event_id = ("nobleTaken","increaseTokens","decreaseTokens","increaseGold","decreaseGold","drawCard",
			"fillCard","fillNoble","increasePlayerTokens","decreasePlayerTokens","increasePlayerGold",
			"decreasePlayerGold","reserveHiddenCard","reserveBoardCard","receiveNoble",
			"receiveCardBonus","receiveCardPoints","receiveNoblePoints")
event_simple = ("take token","reserve hidden","reserve board","receive noble","receive points")

config = (0,2,3,1,1,1,2,0,1,2)
event = event_id # event_id
is_lin = False

for i in range(0,len(config)):
	if(i<len(rh_space)):
		value = rh_space[i]["a"][config[i]]
		name  = rh_space[i]["name"]
		print(f"{{\"{name}\"\t:\t\"{value}\"}},")
	elif is_lin:
		value = round(w_space[config[i]],1)
		print(f"{{\"{event[i-len(rh_space)]}\"\t:\t\"{value}\"}},")
	else:
		value = round(w_space[config[i]],1)
		print(f"{{\"w{i-len(rh_space)}\"\t:\t\"{value}\"}},")