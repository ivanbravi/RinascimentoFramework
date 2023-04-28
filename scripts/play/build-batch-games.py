import json as j
import os

archive_path = "/Users/ivanbravi/git/Rinascimento/agents/archive.json"
out_folder = "./agents/J/RNDx3/batch-journal-all/"
printAgentsList = False

agents_data = j.load(open(archive_path))
agents = [2,5,6,7,9,10,12,13,14,15,16,17,18];
opponents = [0];

if not os.path.exists(out_folder): os.makedirs(out_folder)

if printAgentsList:
	for i in range(len(agents_data["agents"])):
		a = agents_data["agents"][i]
		if("name" in a):
			print(str(i)+"\t"+a["name"]+" "+a["type"])
		else:
			print(str(i)+"\t"+a["type"])

for a in agents:
	selected_agents = [a] + opponents
	selected_agents_data = []

	for sa in selected_agents:
		selected_agents_data = selected_agents_data + [agents_data["agents"][sa]]

	game_file_path = out_folder+str(a)+".json"
	with open(game_file_path, 'w') as game_file:
		j.dump({"agents" : selected_agents_data}, game_file)


# 0		RND
# 1		OSLA
# 2		BMRH* RHEA
# 3		MCTS* MCTS
# 4		SRH* SRHEA
# 5		BMRH** RHEA
# 6		linear^5 EHB-simple-RHEA-linear-11
# 7		poly_2^5 EHB-simple-RHEA-poly,2-11
# 8		poly_3^5 EHB-simple-RHEA-poly,3-11
# 9		linear^18 EHB-id-RHEA-linear-11
# 10	poly_2^18 EHB-id-RHEA-poly,2-11
# 11	poly_3^18 EHB-id-RHEA-poly,3-11
# 12	SB-linear EHB-id-RHEA-poly,3-11
# 13	SB-pann1% SVB-SSPP-RHEA-nn,TANH,192,(5702),30,1,[1]-11
# 14	SB-pann1(1N)% SVB-SSPP-RHEA-nn,TANH,192,(5702),30,1,[2]-11
# 15	SB-pann5% SVB-SSPP-RHEA-nn,TANH,192,(5472),30,1,[2]-11
# 16	SB-pann5(1N)% SVB-SSPP-RHEA-nn,TANH,192,(5472),30,1,[8]-11
# 17	SB-pann10% SVB-SSPP-RHEA-nn,TANH,192,(5184),30,1,[6]-11
# 18	SB-pann10(1N)% SVB-SSPP-RHEA-nn,TANH,192,(5184),30,1,[4]-11fjkfbj

