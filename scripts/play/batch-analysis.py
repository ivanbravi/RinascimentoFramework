import pandas as pd
import json as j
import statistics as s

def player_with_id(players,i):
		for p in players:
			if str.endswith(p,str(i)):
				return p
		return -1

def id_for_player(player):
	return player.split("_").last()

def analyse(path, agents, opponents, exp_name="exp", verbose=False):
	print(path)
	data = j.load(open(path))

	games = [str(i)+".json" for i in agents]
	exp_names = [exp_name for _ in games]
	names = []
	wrs = []
	o_wrs = []
	draws = []

	for game in games:
		run_data = j.loads(data[game])
		players = list(run_data["playersStats"].keys())
		name = player_with_id(players,0)
		names.append(name)
		wr = []
	
		for i in range(0,opponents+1):
			agent = player_with_id(players, i)
			wr_history = run_data["playersStats"][agent]["stats"]["win ratio"]["history"]
			wr.append(s.mean(wr_history))
			
		o_wrs.append([wr[1:(opponents+1)]])
		wrs.append(wr[0])
		draws.append(1-sum(wr))
		if verbose : print(f"{game}\t{name}\t{wr[0]}\topponents: ({wr[1:(opponents+1)]})")

	df = pd.DataFrame(zip(exp_names, agents,games, names, wrs, o_wrs, draws),
                  columns = ['experiment', 'id', 'file', 'name', 'win_rate', 'o_win_rate', 'draws']) 

	return df

# -–––––––––––––––––––––––––––––––––––––––
# specify full path to json file containing all results
# the file should be compiled using the play.jar
base = "/Users/ivanbravi/Desktop/Simulazioni/play/_journal/x10000 missing agent/"

agents = [10, 13, 15, 17, 12, 14, 16, 18, 7, 5, 2, 6, 9]


folders = (
	base+"PlayRinascimento - [batch-journal-all-RNDx1]/results.json", 
	base+"PlayRinascimento - [batch-journal-all-BMRHx1]/results.json",
	base+"PlayRinascimento - [batch-journal-all-RNDx3]/results.json",
	base+"PlayRinascimento - [batch-journal-all-BMRHx3]/results.json"
)

opponents = (
	1,
	1,
	3,
	3
)

name = [
	"RNDx1",
	"BMRHx1",
	"RNDx3",
	"BMRHx3"
]

exp = list(zip(folders, opponents, name))

frames = [analyse(f,agents,o,n) for f,o,n in exp]
result = pd.concat(frames)
result.to_csv(base+"results.csv")

# -–––––––––––––––––––––––––––––––––––––––

# base = "/Users/ivanbravi/Desktop/Simulazioni/play/_journal/" 
# opponents = 1

# # pt1 10 13 15 17
# # pt2 12 14 16 18
# # pt3 7 5 2 6 9

# frames = [
# 	analyse(base+"pt1/logs/PlayRinascimento - [batch-journal-all]/results.json", [10, 13, 15, 17], opponents),
# 	analyse(base+"pt2/logs/PlayRinascimento - [batch-journal-all]/results.json", [12, 14, 16, 18], opponents),
# 	analyse(base+"pt3/logs/PlayRinascimento - [batch-journal-all]/results.json", [7, 5, 2, 6, 9], opponents)
# ]
# result = pd.concat(frames)

# print(result)

