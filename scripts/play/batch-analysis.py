import json as j
import statistics as s

data_path = "/Users/ivanbravi/Desktop/Simulazioni/play/batch-03-all/PlayRinascimento - [batch-03-all]/results.json"

data = j.load(open(data_path))

games = ["2.json","3.json","4.json","5.json","6.json","7.json","8.json","9.json","10.json","11.json"]
opponents = 1

def player_with_id(players,i):
	for p in players:
		if str.endswith(p,str(i)):
			return p
	return -1

for game in games:
	run_data = j.loads(data[game])
	players = list(run_data["playersStats"].keys())
	wr = []
	for i in range(0,1+opponents):
		agent = player_with_id(players, i)
		wr_history = run_data["playersStats"][agent]["stats"]["win ratio"]["history"]
		wr.append(s.mean(wr_history))
	
	print(f"{game}\t{player_with_id(players,0)}\t{wr[0]}\topponents: ({wr[1:4]})")