from gs_utils import fitness as gs_fitness, evaluations as gs_evaluations
from gs_utils import solution as gs_solution, space as gs_space
from gs_utils import space_index_to_values as space_values, space_index_to_name as space_name
from analyse_fitness_distribution import analyse as fitness_analyse, singles_data as fitness_singles
import pandas as pd
import matplotlib.pyplot as plt
import os
from scipy.stats import sem
from statistics import mode, mean
import numpy as np

# MULTIPLE EXPERIMENTS WITH SEQUENTIAL IDS
from_id = 40
to_id = 51
experiment_ids = [str(i) for i in range(from_id,to_id+1)]
root = "/Users/ivanbravi/Desktop/GAME SEARCH/_results/40-51_multiple/"
alias = ["EE(W, R*)","EE(W, EF*)","EE(W, EF)","EE(T, R*)","EE(T, EF*)","EE(T, EF)","EE(WT, R*)","EE(WT, EF*)","EE(WT, EF)","EE(LT, R*)","EE(LT, EF*)","EE(LT, EF )"]
max_exp = 20

skippable = ["Deck Count", "Suit Count", "Player Count"]

custom_out = True
out_overall = root+"analysis/_overall/"
os.makedirs(out_overall, exist_ok=True)

def get_folders(folder):
	return [x for x in os.listdir(curr_folder) if os.path.isdir(curr_folder+x)]

def out_path_make(r, ex):
	if custom_out:	
		return r+"analysis/"+ex #results saved in the root
	else:
		return r+ex+"analysis/" #results saved inside each experiment's folder

C_id = "id"
C_singles = "singles"

trend = pd.DataFrame(columns = ["values", "error", "exp"])
singles_data = pd.DataFrame(columns=[C_id, C_singles])
exp_outcomes = pd.DataFrame(columns=["exp","value", "stderr"])

for e_index in range(0,len(experiment_ids)):
	plt.figure(figsize=(6.4, 4.8))
	plt.close()
	experiment_id = experiment_ids[e_index]+"/"
	print("[PROCESSING: "+experiment_id+"]…")
	curr_folder = root+experiment_id+"logs/";
	out_path = out_path_make(root, experiment_id)
	out_path_parameters = out_path+"_parameters/"
	out_path_singles = out_path+"_singles/"
	os.makedirs(out_path, exist_ok=True)
	os.makedirs(out_path_parameters, exist_ok=True)
	os.makedirs(out_path_singles, exist_ok=True)

	C_fitness = 'fitness' 
	C_evaluations = 'evaluations'
	C_solution = 'solution'
	C_folder = 'folder'

	data = pd.DataFrame(columns=[C_fitness, C_evaluations, C_solution, C_folder])

	fitness_singles_data = []

	folders = get_folders(curr_folder)[:max_exp]
	for f_id in range(0,len(folders)):
		f = folders[f_id]
		print("\t"+f, end='')
		in_path = curr_folder+f+"/"

		curr_data = {
			C_fitness: [gs_fitness(in_path)],
			C_evaluations: [gs_evaluations(in_path)],
			C_solution: [gs_solution(in_path)],
			C_folder: [f]
		}

		tmp_df = pd.DataFrame(curr_data)
		data = pd.concat([data, tmp_df], ignore_index = True)
		data.reset_index()

		fitness_analyse(in_path,out_path_singles+str(f_id)+"/")
		fitness_singles_data.extend(fitness_singles(in_path))

		print("✔️")

	curr_singles = {
		C_id : [experiment_id],
		C_singles : [fitness_singles_data]
	}

	tmp_singles = pd.DataFrame(curr_singles)
	singles_data = pd.concat([singles_data, tmp_singles], ignore_index=True)
	singles_data.reset_index()

	# use data[C_fitness] for average solution fitness and error
	plt.hist(data[C_fitness], range=[0,1], density=False)
	plt.title("Final solution fitness distribution")
	plt.savefig(out_path+"solution_fitness.pdf")
	plt.close()

	# use data[C_evaluations] for average trend with error bars
	x = [x for x in range(0,len(data[C_evaluations][0]))]
	progress = []
	progress_error = []

	for i in range(0, len(x)):
		values = [e[i] for e in data[C_evaluations]]
		progress.append(mean(values))
		progress_error.append(sem(values))

	trend_data ={
		"values":[progress],
		"error":[progress_error],
		"exp": [experiment_id]
	}
	trend = pd.concat([trend, pd.DataFrame(trend_data)], ignore_index = True)
	trend.reset_index()

	plt.plot(x,progress)
	plt.errorbar(x, progress, progress_error, capsize = 4)#, uplims=True, lolims=True)
	plt.title("Mean NTBEA current-best fitness trend")
	plt.savefig(out_path+"search_progess_ranged.pdf")
	plt.close()

	plt.plot(x,progress)
	plt.ylim([0,1])
	plt.errorbar(x, progress, progress_error, capsize = 4)#, uplims=True, lolims=True)
	plt.title("Mean NTBEA current-best fitness trend")
	plt.savefig(out_path+"search_progess_squeeze.pdf")
	plt.close()

	# use data[C_solution] for plotting mode solution for each parameter and "error"

	space = gs_space(curr_folder+get_folders(curr_folder)[0]+"/")

	# parameters singles
	for i in range(0,len(data[C_solution][0])):
		actual_values_list = space_values(space, i)
		name = space_name(space, i)

		if name in skippable:
			continue

		values = [ e[i] for e in data[C_solution] ]
		actual_values = [actual_values_list[v] for v in values]

		plt.hist(actual_values, bins=len(actual_values_list), density=False)
		plt.xticks(actual_values_list)
		plt.title(name)
		plt.savefig(out_path_parameters+"["+str(i)+"]_"+name+".pdf")
		plt.close()

	# parameters all

	fig, axs = plt.subplots(3,4, figsize=(16, 12))
	axs = np.array(axs).reshape(-1)
	ax_index = 0
	for i in range(0,len(data[C_solution][0])):
		actual_values_list = space_values(space, i)
		name = space_name(space, i)

		if name in skippable:
			continue

		values = [ e[i] for e in data[C_solution] ]
		actual_values = [actual_values_list[v] for v in values]

		axs[ax_index].hist(actual_values, bins=len(actual_values_list), density=False)
		axs[ax_index].set_xticks(actual_values_list)
		axs[ax_index].title.set_text(name)
		ax_index+=1
	
	fig.suptitle(experiment_id)
	fig.savefig(out_path_parameters+"all.pdf")
	plt.close()
	
	fitness_avg = mean(data[C_fitness])
	fitness_err = sem(data[C_fitness])
	tmp_outcome = pd.DataFrame({
		"exp": [alias[e_index]],
		"value": [fitness_avg],
		"stderr": [fitness_err]
	})
	exp_outcomes = pd.concat([exp_outcomes,tmp_outcome], ignore_index=True)
	exp_outcomes.reset_index()

	data = data.sort_values(C_folder, ascending=True)
	data.to_csv(out_path+experiment_id.replace("/","")+"_solutions.csv")

	print("[DONE: "+experiment_id+"]\n")

cmap = plt.cm.get_cmap('tab20b')
colors_styles = []
colors_ordered = []
for i in range(0,4):
	for j in range(0,3):
		colors_styles.append({
			"color": cmap((j+i*4)/20),
			"style": ["solid","dotted","dashed"][j]
			})
		colors_ordered.append(cmap((j+i*4)/20))


plt.close()
plt.grid(color='lightgray', linestyle='dotted', linewidth=0.5, axis="y")
plt.scatter(exp_outcomes["exp"], exp_outcomes["value"], s=3, color = colors_ordered)
plt.errorbar(exp_outcomes["exp"], exp_outcomes["value"], yerr=exp_outcomes["stderr"], capsize = 6, linestyle='')#, color = colors_ordered)
plt.title("Final Fitness by Experiment")
plt.xlabel("Experiments")
plt.xticks(rotation=45, ha='right')
plt.ylabel("Fitness")
plt.ylim([-0.01,1.01])
plt.yticks(np.linspace(0, 1, 11))
plt.savefig(out_overall+"experiments_fitness.pdf")
plt.close()

y = [i for i in range(0,12)]

for i,e in singles_data.iterrows():
	out_path = out_path_make(root, e[C_id])
	plt.grid(color='lightgray', linestyle='dotted', linewidth=0.5, axis='y')
	plt.hist(e[C_singles], bins=11, density=True, range=[0,1], label=alias[i], color=colors_styles[i]["color"])
	plt.xlabel("Fitness Value")
	plt.ylabel("Probability Density")
	plt.ylim([-0.1,11.1])
	plt.yticks(ticks=y, labels=y)
	plt.title("Fitness distribution "+alias[i])
	plt.legend(loc="lower center", bbox_to_anchor=(0.5, -0.2))
	plt.savefig(out_path+"fitness_evaluation_distribution.pdf")
	plt.savefig(out_path+(e[C_id].replace("/",""))+"_fitness_evaluation_distribution.pdf")
	plt.close()

for i in range(0,3):
	plt.figure()
	plt.ylim([0,11])
	name = ""
	title = ""
	aliases = []
	yy = []
	cs = []
	for j in range(0,4):
		index = i*4+j
		c_index = j*3+i
		e = singles_data.loc[c_index]
		name = name+e[C_id].replace("/","")+"_"
		title = title+alias[c_index]+" "
		aliases.append(alias[c_index])
		yy.append(e[C_singles])
		cs.append(colors_styles[c_index]["color"])
	plt.grid(color='lightgray', linestyle='dotted', linewidth=0.5, axis='y')
	plt.hist(yy, bins=11, density=True, range=[0,1], label=aliases, color=cs)
	plt.title("Fitness distribution comparison: "+name)
	plt.xlabel("Fitness Value")
	plt.ylabel("Probability Density")
	plt.ylim([-0.1,11.1])
	plt.yticks(ticks=y, labels=y)
	plt.legend(loc="lower center", ncol=len(aliases), bbox_to_anchor=(0.5, -0.2))
	plt.savefig(out_overall+name+"fitness_evaluation_distribution.pdf")
	plt.close()

x = [x for x in range(0,len(trend["values"][0]))]
x_labels=[int((l+1)/len(x)*1000) for l in range(0,len(x))]

plt.close()
plt.figure(figsize=(8, 6))
plt.grid(color='lightgray', linestyle='dotted', linewidth=0.5)
for i,e in trend.iterrows():
	plt.errorbar(x, e["values"], e["error"], capsize = 4, label=alias[i],color=colors_styles[i]["color"], linestyle=colors_styles[i]["style"])
plt.xlabel("Budget Used")
plt.xticks(ticks=x, labels=x_labels)
plt.ylabel("Fitness")
plt.ylim([-0.05,1.05])
plt.yticks(np.linspace(0, 1, 11))
plt.legend(loc="lower center", ncol=4, bbox_to_anchor=(0.5, -0.17))
plt.title("Fitness Trend Comparison")
plt.savefig(out_overall+("trend["+str(from_id)+"-"+str(to_id)+"].pdf").replace(" ","_"))
plt.close()

for i in range(0,3):
	plt.figure(figsize=(8, 6))
	plt.grid(color='lightgray', linestyle='dotted', linewidth=0.5)
	for j in range(0,4):
		index = i*4+j
		c_index = j*3+i
		e = trend.loc[c_index]
		plt.errorbar(x, e["values"], e["error"], capsize = 4, label=alias[c_index],color=colors_styles[c_index]["color"], linestyle=colors_styles[c_index]["style"])
	plt.xlabel("Budget Used")
	plt.xticks(ticks=x, labels=x_labels)
	plt.ylabel("Fitness")
	plt.ylim([-0.05,1.05])
	plt.yticks(np.linspace(0, 1, 11))
	plt.legend(loc="lower center", ncol=4, bbox_to_anchor=(0.5, -0.17))
	plt.title("Fitness Trend Comparison")
	plt.savefig(out_overall+("trend["+["RHEA", "EFRHEA*", "EFRHEA 100 and EFRHEA 75"][i]+"].pdf").replace(" ","_"))
	plt.close()

