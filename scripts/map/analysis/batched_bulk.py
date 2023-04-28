from bulk_analysis import run

def run_from_to(path, from_g, to_g):
	games = [str(from_g)+"-"+str(to_g)]
	opponents = ["RND"]
	agents = ["g_"+str(i) for i in range(from_g,to_g+1)]
	space_names = ["game_"+str(i) for i in range(from_g,to_g+1)]
	run(path, games, opponents, agents, space_names)

root = "/Users/ivanbravi/Desktop/GAME SEARCH/MAP/_results (MAP)/"

# folder = root+"GROUPED"
# run_from_to(folder,20,22)
# run_from_to(folder,23,25)
# run_from_to(folder,26,28)
# run_from_to(folder,29,31)

folder = root+"COMPARISON_(20-31)"
run(folder,["all"],["RND"]
	["g_"+str(i) for i in range(20,31+1)],
	["game_"+str(i) for i in range(20,31+1)])

# folder = root+"SINGLES"
# run(folder,
# 	["game_"+str(i) for i in range(20,31+1)],
# 	["RND"],
# 	["eb-linear-id"],
# 	["EF-id"])