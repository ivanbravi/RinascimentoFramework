from mapelites import MAPElitesSpaceLoader, MAP_FITNESS, MAP_CONFIG, MAP_BEHAVIOURS_V, MAP_WEIGHTS, quiet
import os
import json
import numpy
from datetime import datetime
import matplotlib.pyplot as plt
numpy.set_printoptions(threshold=numpy.inf)

quiet()

# TODO: FIIL IN FILE SYSTEM DETAILS
game = 'SP2P'
opponent = 'RND'
# data_folder = '/Volumes/Lacie/___PhD/Rinascimento/MAP-Elites/ToG/_data/'
data_folder = '/Users/ivanbravi/Desktop/AgentDB/'
agent = 'eb-linear-id'
folder = data_folder+"_data/"+game+'/vs '+opponent+'/'+agent+'/'
out_folder = data_folder+agent+"/"
name = 'eb_id_db'

# TODO: FIIL IN AGENTS DETAILS
agent_type = "RHEA"
agent_name = game+"-"+opponent+"-"+agent
space = MAPElitesSpaceLoader.load(folder, name)
experiment_description = agent+" vs "+opponent +" in "+game

os.makedirs(out_folder, exist_ok=True)

# card count
# nobles
# total coins
# reserved cards
# card cost

behaviour_1 = 'card count'
behaviour_2 = 'card cost'
behaviour_1 = 0
behaviour_2 = 1

db_filter = space.project([behaviour_1, behaviour_2])
db_full = space.df.filter(items = db_filter['index'], axis=0)
db_full = db_full.sort_values(by=MAP_FITNESS, ascending=False)

plt.hist(db_full[MAP_FITNESS], bins = 20)
plt.title("Win rate: "+experiment_description)
plt.gca().set_xlabel("win rate")
plt.gca().set_ylabel("#agents")
plt.savefig(out_folder+"win_rate_distribution.png")
plt.clf()
# db_full.hist(column=MAP_FITNESS)

db_rows = list()

hasWeights = MAP_WEIGHTS in space.df.columns

for index, row in db_full.iterrows():
	if hasWeights:
		db_row = [
		json.loads(row[MAP_CONFIG]),
		json.loads(row[MAP_BEHAVIOURS_V]),
		row[MAP_FITNESS], 
		json.loads(row[MAP_WEIGHTS])
		]
	else:
		db_row = [
		json.loads(row[MAP_CONFIG]),
		json.loads(row[MAP_BEHAVIOURS_V]),
		row[MAP_FITNESS]
		]
	db_rows.append(db_row);

db_map = {
  "name": agent_name,
  "agent": agent_type,
  "metrics": space.behaviours,
}
extra = {
	"path": folder,
	"agent count": db_full.shape[0],
	"export date": datetime.now().strftime("%d/%m/%Y %H:%M:%S")
}

if hasWeights:
	with open(folder+'p.json', 'r') as f:
	  p = json.load(f)
	heuristic = p["EB/heuristic"]
	converter = p["EB/converter"]
	db_map["heuristic"] = heuristic
	db_map["converter"] = converter

db_map["extra"] = extra
db_map["data"] = db_rows



with open(out_folder+name+'.json', 'w') as f:
    json.dump(db_map, f, indent=4)

print("DONE")
