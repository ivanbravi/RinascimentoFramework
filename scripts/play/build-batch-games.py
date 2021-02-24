import json as j
import os

archive_path = "/Users/ivanbravi/git/Rinascimento/agents/archive.json"

agents_data = j.load(open(archive_path))
agents = [2,3,4,5,6,7,8,9,10,11];
opponents = [2];
folder = "./agents/batch-03-all/"

if not os.path.exists(folder): os.makedirs(folder)

for a in agents:
	selected_agents = [a] + opponents
	selected_agents_data = []

	for sa in selected_agents:
		selected_agents_data = selected_agents_data + [agents_data["agents"][sa]]

	game_file_path = folder+str(a)+".json"
	with open(game_file_path, 'w') as game_file:
		j.dump({"agents" : selected_agents_data}, game_file)


# 0     RND
# 1     OSLA
# 2     BMRH* [RHEA]
# 3     MCTS* [MCTS]
# 4     SRH* [SRHEA]
# 5     BMRH** [RHEA]
# 6     linear^5 [EHB-simple-RHEA-linear-11]
# 7     poly_2^5 [EHB-simple-RHEA-poly,2-11]
# 8     poly_3^5 [EHB-simple-RHEA-poly,3-11]
# 9     linear^18 [EHB-id-RHEA-linear-11]
# 10    poly_2^18 [EHB-id-RHEA-poly,2-11]
# 11    poly_3^18 [EHB-id-RHEA-poly,3-11]