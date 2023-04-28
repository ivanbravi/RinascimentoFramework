from analyse_eval_data import analyse as data_analyse
from analyse_eval_log import analyse as log_analyse
from analyse_fitness_distribution import analyse as fitness_analyse
import os

# CUSTOM SINGLE EXPERIMENT
# experiment_ids = ["GS - 22-10-07-14-36-19/"]
# root = "/Users/ivanbravi/git/Rinascimento/logs/"

# MULTIPLE EXPERIMENTS WITH INCREASING IDS
from_id = 20
to_id = 31
experiment_ids = ["log_"+str(i)+"/" for i in range(from_id,to_id+1)]
root = "/Users/ivanbravi/Desktop/GAME SEARCH/_results/20-31/"

custom_out = True

should_analyse_log = False
should_analyse_log_singles = False
should_analyse_data = False

def out_path_make(r, ex):
	if custom_out:	
		return r+"analysis/"+ex
	else:
		return None

for experiment_id in experiment_ids:
	print("[PROCESSING: "+experiment_id+"]…", end='')
	isBase = not os.path.isfile(root+experiment_id+"evalLog[1].json")
	in_path = root+experiment_id
	out_path = out_path_make(root, experiment_id)
	if isBase:
		# BASE
		fitness_analyse(in_path,out_path)
	else:
		# BEHAVIOUR SEARCH
		fitness_analyse(in_path,out_path)
		if should_analyse_data: data_analyse(in_path,out_path)
		if should_analyse_log: log_analyse(in_path,out_path, singles=should_analyse_log_singles)
		fitness_analyse(in_path,out_path)
	print("✔️")

#### CREATE GIF FROM SINGLE EVALUATIONS ####
# PYTHON
# import os
# files = [ f for f in os.listdir('.') if os.path.isfile(os.path.join('.',f)) and f.endswith('.png') ]
# for i, file in enumerate(sorted(files)):
#    	os.rename(file, 'image%03d.png' % i)
# TERMINAL
# ffmpeg -i image%03d.png -vf palettegen palette.png
# ffmpeg -v warning -i image%03d.png -i palette.png  -lavfi "paletteuse,setpts=2*PTS" -y out_2.gif