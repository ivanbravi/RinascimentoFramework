import matplotlib.pyplot as plt
import numpy as np
import os, json

# # ---------------INPUT HERE---------------
# experiment_id="/"
# folder = "/Users/ivanbravi/git/Rinascimento/logs/"
# # ----------------------------------------
# analyse(folder+experiment_id)


def analyse(in_folder, out_folder=None):
	if out_folder is None:
		out_folder = in_folder+"analysis/neighbourhood/"
	os.makedirs(out_folder, exist_ok = True)

	with open(in_folder+"neighbourhood_sampling_out.json", 'r') as f:
		in_data = json.load(f)
		data = []
		# complile data from the configuration entries in in_data
		for entry in in_data:
			data.append(entry["v1"])
		data = np.array(data)

	with open(in_folder+"ActualGameSpace.json", 'r') as f:
		space_data = json.load(f)
		dims = space_data["dimensions"]
		# fetch dims from the search space (should be a field "dims")

	i=0
	for samples in data.T:
		plt.clf()
		plt.hist(samples, bins=max(samples)+1, range=[0, dims[i]])
		plt.savefig(out_folder+"param["+str(i)+"].pdf")
		plt.clf()
		i=i+1