from mapelites import MAPElitesSpaceLoader, MAP_FITNESS
from overlap import MAPElitesOverlap
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
import os

def data_dir(root,game, opponent, agent):
	return root+"/_data/"+game+"/vs "+opponent+"/"+agent+"/"

def data_dirs(root, game, opponent, agents):
	return [data_dir(root, game, opponent, agent) for agent in agents ]

def pre_load(root, game,opponent,agents,space_names):
	space_dirs = data_dirs(root, game, opponent, agents)
	print("PRE-LOADING SPACES\n")
	for i in range(len(space_dirs)):
		MAPElitesSpaceLoader.load(space_dirs[i], space_names[i])
	print("DONE")

def __load_spaces(space_dirs, space_names, single_export, samples, out, show_arrows):
	spaces = []
	for i in range(len(space_dirs)):
		if(single_export[i]):
			space = load_and_export(samples,space_dirs[i], space_names[i],out,show_arrows=show_arrows)
		else:
			space = MAPElitesSpaceLoader.load(space_dirs[i], space_names[i])
		spaces.append(space)
	plt.close('all')
	return spaces

def __export_behaviour_exploration(spaces, space_dirs, samples, title, out, show_extras):
	f, ax = plt.subplots()
	for i in range(len(space_dirs)):
		spaces[i].get_axes_behaviours_exploration(samples, ext_ax=ax, show_extras=show_extras)

	file_name = out+"all_space_exploration"+("_extra" if show_extras else "")+".pdf"
	ax.set_title(title)
	f.tight_layout()
	f.savefig(out+"all_space_exploration.pdf", format="pdf")
	plt.close(f)

def __export_information_quality(spaces, space_dirs, samples, title, out):
	f, ax = plt.subplots()
	for i in range(len(space_dirs)):
		spaces[i].get_information_quality_ax(ext_ax=ax, samples=samples)
	ax.set_title(title)
	f.tight_layout()
	f.savefig(out+"all_information_quality.pdf", format="pdf")
	plt.close(f)

def __export_performance_distribution(spaces, space_dirs, title, out):
	f, ax = plt.subplots()

	# OVERLAY
	# for i in range(len(space_dirs)):
	# 	bins = np.linspace(0, 1, 10)
	# 	ax.hist(spaces[i].df[MAP_FITNESS], bins = bins, alpha = 0.25, label=spaces[i].name)

	# ONE SHOT
	data = [space.df[MAP_FITNESS] for space in spaces]
	labels  = [space.name+" IQ" for space in spaces]
	ax.hist(data, bins = 10, label=labels) #, histtype="barstacked"
	ax.legend()
	ax.set_title(title)
	f.tight_layout()
	f.savefig(out+"all_performance_distribution.pdf", format="pdf")
	plt.close(f)

def __export_comparisons(spaces, space_dirs, out):
	for i in range(len(space_dirs)):
		for j in range(i+1,len(space_dirs)):
			export_comparison(spaces[i],spaces[j],out)
	plt.close('all')

def out_dir(root,game, opponent):
	return root+game+"/vs"+opponent+"results/"

AN_SINGLE = "san"
AN_BEHAVIOUR_SIMPLE = "bsan"
AN_BEHAVIOUR_EXTRA = "bean"
AN_INFORMATION = "ian"
AN_DISTRIBUTION = "dan"
AN_COMPARISON = "can"

def run_analysis(game,opponent,agents,space_names,single_export, samples = 30, preload = False,
	partial=[AN_SINGLE, AN_BEHAVIOUR_SIMPLE, AN_BEHAVIOUR_EXTRA, AN_INFORMATION, AN_DISTRIBUTION, AN_COMPARISON],
	show_arrows=False, root="./out/"):
	out = out_dir(root, game, opponent)
	space_dirs = data_dirs(root, game, opponent, agents)
	spaces_count = len(space_names)

	os.makedirs(out, exist_ok=True)

	if preload:
		pre_load(root, game, opponent, agents, space_names)

	if AN_SINGLE not in partial:
		single_export = [False for _ in range(spaces_count)]

	print("LOADING SPACES (EXPORTING:"+str(single_export)+")")
	spaces = __load_spaces(space_dirs, space_names, single_export, samples, out, show_arrows=show_arrows)
	title = game+" (vs "+opponent+")"
	if AN_BEHAVIOUR_SIMPLE in partial:
		print("EXPORTING BEHAVIOUIR EXPLORATION SIMPLE PLOTS")
		__export_behaviour_exploration(spaces, space_dirs, samples, title, out, False)

	if AN_BEHAVIOUR_EXTRA in partial:
		print("EXPORTING BEHAVIOUIR EXPLORATION EXTRA PLOTS")
		__export_behaviour_exploration(spaces, space_dirs, samples, title, out, True)
	
	if AN_INFORMATION in partial:
		print("EXPORTING INFORMATION QUALITY PLOTS")
		__export_information_quality(spaces, space_dirs, samples, title, out)

	if AN_DISTRIBUTION in partial:
		print("EXPORTING PERFORMANCE DISTRIBUTION PLOTS")
		__export_performance_distribution(spaces, space_dirs, title, out)

	if AN_COMPARISON in partial:
		print("EXPORTING COMPARISON PLOTS")
		__export_comparisons(spaces, space_dirs, out)



def load_and_export(samples,folder, name, out, show_arrows):
	out_folder = out+name+"/"
	space = MAPElitesSpaceLoader.load(folder, name)
	space.export_plots_2D(folder=out_folder, show_arrows=show_arrows)
	space.export_plots_2D_single(folder=out_folder, show_arrows=show_arrows)
	space.export_behaviours_exploration(folder=out_folder,samples=samples, show_extras=True)
	space.export_search_progress(folder=out_folder,samples=samples)
	return space

def export_comparison(space1, space2, out):
	overlap = MAPElitesOverlap(space1,space2)
	plot_dir = out+space1.name+" vs "+space2.name+"/"
	overlap.export_coverage_plots_2D(plot_dir)
	overlap.export_variation_plots_2D(plot_dir)