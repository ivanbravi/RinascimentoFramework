from space_analysis import run_analysis, pre_load, data_dir
from space_analysis import AN_BEHAVIOUR_SIMPLE, AN_BEHAVIOUR_EXTRA, AN_INFORMATION, AN_DISTRIBUTION, AN_COMPARISON, AN_SINGLE
from mapelites import print_summary
import os, platform

########################### USUAL PARAMETERS ###########################
#
# games = [] 
# "SP2P","W2","1C2W"
#
# opponents = [] 
# "RHEA","RND"
#
# agents = []
# "pb", "eb-linear-id", "eb-linear-simple",
# "sb-linear", "sb-pann10", "sb-pann5", "sb-pann1"
# "long", "long_bunched"
#
# space_names = []
# "PB", "EF-id", "EF-hc", 
# "SF-l", "SF-nn10", "SF-nn5", "SF-nn1"
# "EF-id-long", "EF-id-bunched"
#
# root = ""
# "./AAAI/"
# "/Volumes/Lacie/___PhD/Rinascimento/MAP-Elites/AAAI21/"
# "/Volumes/Lacie/___PhD/Rinascimento/MAP-Elites/ToG/"
# "/Volumes/Lacie/___PhD/Rinascimento/MAP-Elites/_ALL/"
# "/Volumes/Lacie/___PhD/Rinascimento/MAP-Elites/Experimental runs/"
# "/Users/ivanbravi/Desktop/GAME SEARCH/MAP/_results/"
#
########################################################################

def run(root, games, opponents, agents, space_names, preload=True, show_arrows=False, single_export=[True, True, True, True]):
	isMac = platform.system() == "Drawin"
	samples = 30
	try:

		for game in games:
			for opponent in opponents:
				run_analysis(game,opponent,agents,space_names,single_export,
					partial=[AN_SINGLE,AN_BEHAVIOUR_SIMPLE, AN_BEHAVIOUR_EXTRA, AN_INFORMATION, AN_DISTRIBUTION, AN_COMPARISON],
					samples=samples, show_arrows=show_arrows, root=root, preload=preload)
				# AN_SINGLE, AN_BEHAVIOUR_SIMPLE, AN_BEHAVIOUR_EXTRA, AN_INFORMATION, AN_DISTRIBUTION, AN_COMPARISON

		# for game in games:
		# 	for opponent in opponents:
		# 		for agent_id in range(len(agents)):
		# 			folder = exp_folder(game, opponent, agents[agent_id])
		# 			print_summary(folder, space_names[agent_id])
		print("RUN: SUCCESSFUL")
		if isMac:
			os.system('say -v "Samantha" "Run successful"')
			os.system('afplay /System/Library/Sounds/Glass.aiff')
	except Exception as e:
		print("RUN: EXITED WITH ERROR")
		print(e)
		if isMac:
			os.system('say -v "Samantha" "Error"')
			os.system('afplay /System/Library/Sounds/Sosumi.aiff')