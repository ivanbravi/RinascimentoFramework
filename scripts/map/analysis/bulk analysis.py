from space_analysis import run_analysis, pre_load, data_dir
from space_analysis import AN_BEHAVIOUR_SIMPLE, AN_BEHAVIOUR_EXTRA, AN_INFORMATION, AN_DISTRIBUTION, AN_COMPARISON, AN_SINGLE
from mapelites import print_summary


games = ["SP2P","W2","1C2W"] #"SP2P","W2","1C2W"
opponents = ["RHEA","RND"] #"RHEA","RND"

agents = ["pb",	"eb-linear-id",	"eb-linear-simple",	"sb-linear"]
space_names = ["PB","EF -id", "EF-hc", "SF"]
single_export = [True, True, True, True]

preload = True
show_arrows = False
# root = "./AAAI/"
root = "/Volumes/Lacie/_lavoro/Rinascimento/MAP-Elites/AAAI21/"
samples = 30


for game in games:
	for opponent in opponents:
		run_analysis(game,opponent,agents,space_names,single_export, partial=[],
			samples=samples, show_arrows=show_arrows, root=root, preload=preload)

# for game in games:
# 	for opponent in opponents:
# 		for agent_id in range(len(agents)):
# 			folder = exp_folder(game, opponent, agents[agent_id])
# 			print_summary(folder, space_names[agent_id])