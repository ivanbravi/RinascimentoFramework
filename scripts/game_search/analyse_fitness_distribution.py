import matplotlib.pyplot as plt
import json, os
from gs_utils import sliding_mean, sliding_stderr

def analyse(in_folder, out_folder = None):
	if out_folder is None:
		out_folder = in_folder+"analysis/"
	os.makedirs(out_folder, exist_ok = True)

	eval_values_file = in_folder+"SingleEvaluations.json"
	if os.path.exists(eval_values_file):
		with open(eval_values_file, 'r') as f:
			window_size = 50
			eval_values = json.load(f)
			eval_values_mean = sliding_mean(eval_values,window_size)
			eval_values_stderr = sliding_stderr(eval_values,window_size)
			stderr_upper_limit = eval_values_mean + eval_values_stderr
			stderr_lower_limit = eval_values_mean - eval_values_stderr
			x = [v for v in range(0,len(eval_values))]
			
			plt.clf()
			plt.scatter(x,eval_values, label="Single values", s=2, color="steelblue")
			plt.plot(x[window_size:len(x)],eval_values_mean[window_size:len(x)], label="Sliding mean (size:"+str(window_size)+")", color="red")
			plt.fill_between(x[window_size:len(x)], 
				stderr_lower_limit[window_size:len(x)], stderr_upper_limit[window_size:len(x)],
				alpha=0.4, facecolor = "red", edgecolor="none",
				label="Sliding mean stderr (size:"+str(window_size)+")")
			plt.axvline(window_size,  linestyle="dotted", color="lightgray", label="Sliding window size")
			plt.title("NTBEA fitness evaluations")
			lgd = plt.legend(bbox_to_anchor=[0.5, -0.19], loc='center', ncol=2)
			plt.xlabel('evaluation count')
			plt.ylabel('fitness')
			plt.savefig(out_folder+"fitness_singles.pdf", bbox_extra_artists=(lgd,), bbox_inches='tight')
			plt.close()

	with open(in_folder+"FitnessTrend.json", 'r') as f:
		in_data = json.load(f)

	with open(in_folder+"exp_parameters.json", 'r') as f:
		params = json.load(f)
		budget = params["ntbea/budget"]

	window_size = 6
	fitness = [e["v2"] for e in in_data]
	fitness_size = len(fitness)
	fitness_mean = sliding_mean(fitness,window_size)
	fitness_stderr = sliding_stderr(fitness,window_size)
	stderr_upper_limit = fitness_mean + fitness_stderr
	stderr_lower_limit = fitness_mean - fitness_stderr

	ticks = 8
	x = [round(i/fitness_size*budget) for i in range(1,fitness_size+1)]
	x_labels = [t for t in range(0, budget+1, budget//ticks)]

	plt.close()
	plt.plot(x,fitness, label="True Fitness", color="steelblue")
	plt.plot(x[window_size:len(x)],fitness_mean[window_size:len(x)], label="Sliding mean (size:"+str(window_size)+")", color="red")
	plt.fill_between(x[window_size:len(x)], 
				stderr_lower_limit[window_size:len(x)], stderr_upper_limit[window_size:len(x)],
				alpha=0.4, facecolor = "red", edgecolor="none",
				label="Sliding mean stderr (size:"+str(window_size)+")")
	plt.axvline((window_size+1)/fitness_size*budget,  linestyle="dotted", color="lightgray", label="Sliding window size")
	plt.title("NTBEA current-best true fitness")
	plt.xlabel('budget')
	plt.ylabel('fitness')
	plt.xticks(ticks=x_labels, labels=x_labels)
	lgd = plt.legend(bbox_to_anchor=[0.5, -0.19], loc='center', ncol=2)
	plt.savefig(out_folder+"fitness_trend.pdf", bbox_extra_artists=(lgd,), bbox_inches='tight')
	plt.close()
			
def singles_data(in_folder):
	eval_values_file = in_folder+"SingleEvaluations.json"
	if os.path.exists(eval_values_file):
		with open(eval_values_file, 'r') as f:
			return json.load(f)
# FOR TESTING PUPOSE
# analyse("/Users/ivanbravi/Desktop/GAME SEARCH/_results/20-31/log_26/")