import matplotlib.pyplot as plt
import json
import os
import pandas as pd
import numpy as np
from scipy.stats import sem
from scipy import mean
from distances import chebyshev, manhattan
from analyse_eval_log import count_datapoints

# experiment_id = "NEIGHBOURHOOD - 22-08-25-15-44-37 - 0.5/"
# root = "/Users/ivanbravi/git/Rinascimento/logs/"
# analyse(root+experiment_id)

def analyse(in_folder, out_folder=None):
	if out_folder is None:
		out_folder = in_folder+"analysis/neighbourhood/"
	os.makedirs(out_folder, exist_ok = True)

	def plot_with_distance(d_function):
		data = pd.read_json(in_folder+"neighbourhood_sampling_out.json")
		data.columns = ["config", "fitness"]
		data["distance"] = np.nan
		data["datapoints"] = np.nan
		data["eval_id"] = np.nan

		with open(in_folder+"center.json", 'r') as f:
			center = json.load(f)

		c_dp = count_datapoints(in_folder)

		for i, row in data.iterrows():
			data.at[i,"distance"] = d_function(center, row["config"])
			data.at[i,"datapoints"] = c_dp[i]
			data.at[i,"eval_id"] = i+1

		distances = list(set(data["distance"]))
		
		f_data_m = []
		f_data_m_e = []

		pd_data_m = []
		pd_data_m_e = []

		for d in distances:
			d_data = data[data["distance"] == d]
			pd_data = d_data["datapoints"]
			f_data = d_data["fitness"]

			f_data_m.append(mean(f_data))
			pd_data_m.append(mean(pd_data))

			if len(d_data)<=1:
				f_data_m_e.append(0)
				pd_data_m_e.append(0)
			else:
				f_data_m_e.append(sem(f_data))
				pd_data_m_e.append(sem(pd_data))

		plt.clf()
		plt.scatter(data["distance"], data["fitness"])
		plt.plot(distances,f_data_m)
		plt.errorbar(distances, f_data_m, f_data_m_e, capsize = 4)#, uplims=True, lolims=True)
		plt.title(d_function.__name__+" / fitness")
		plt.xlabel('distance')
		plt.ylabel('fitness')
		plt.savefig(out_folder+"fitness_"+d_function.__name__+".pdf")
		plt.clf()

		plt.scatter(data["distance"], data["datapoints"])
		plt.plot(distances,pd_data_m)
		plt.errorbar(distances, pd_data_m, pd_data_m_e, capsize = 4)#, uplims=True, lolims=True)
		plt.title(d_function.__name__+" / data points")
		plt.xlabel('distance')
		plt.ylabel('data points count')
		plt.savefig(out_folder+"datapoints_"+d_function.__name__+".pdf")
		plt.clf()
		data.to_csv(out_folder+""+d_function.__name__+".csv", index=False)


	plot_with_distance(chebyshev)
	plot_with_distance(manhattan)