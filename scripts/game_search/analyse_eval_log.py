import math
import numpy as np
import json
import matplotlib.pyplot as plt
import os
import pandas as pd


# # ---------------INPUT HERE---------------
# experiment_id = "GS - 22-09-09-14-10-28 - EFDB vs RND"
# root = "/Users/ivanbravi/git/Rinascimento/logs/"
# # root = "/Users/ivanbravi/Desktop/GAME SEARCH/behaviour_search/logs/"
# # ----------------------------------------
# in_folder = root+experiment_id+"/"
# analyse(in_folder)

def __fitness_histogram__(data, file_path, title):
	counts, bin_edges = np.histogram(data, bins=10, density=True)
	pdf=counts/sum(counts)
	plt.plot(bin_edges[1:],pdf,label="pdf")
	plt.title(title+" ["+
		str(round(min(data),5))+","+
		str(round(max(data),5))+"]")
	plt.savefig(file_path)
	plt.clf()

def count_logs(in_folder):
	count = 1
	while os.path.exists(in_folder+'evalLog['+str(count)+'].json'):
		count += 1
	return count

def count_datapoints(in_folder):
	count = count_logs(in_folder)
	dps_count = []
	for r in range(1,count):
		file_name = 'evalLog['+str(r)+'].json'
		with open(in_folder+file_name, 'r') as f:
			p = json.load(f)
			experimentalData = __matrix__(p[exp_string].replace(" -∞", "NaN").replace("\n","\t"))
			values_count = np.count_nonzero(~np.isnan(experimentalData))
			dps_count.append(values_count)
	return dps_count

exp_string = "experimentalData"
fil_string = "filteredData"
fil_eval = "filterEvaluation"
exp_eval = "preFilterEvaluation"

def __matrix__(strValues):
	a = np.fromstring(strValues, sep="\t")
	l = int(math.sqrt(a.shape[0]))
	m = np.reshape(a,[l,l])
	return np.matrix(m)

def analyse(in_folder, out_folder=None, full_report = False, singles=True):
	if out_folder is None:
		out_folder=in_folder+"analysis/"
	out_folder = out_folder+"eval_log/"
	os.makedirs(out_folder, exist_ok = True)

	def setAxisStyle(ax):
		ax.axes.xaxis.set_visible(True)
		ax.axes.yaxis.set_visible(True)

	if full_report:
		with open(in_folder+"PlayerSamples.json", 'r') as f:
			data = json.load(f)
			if "no actual sampling" not in data:
				acc = ()
				size = len(data["1"])
				for a in data:
					if len(data[a]) == size:
						acc = acc + (data[a],)
				hh = np.column_stack(acc)
				n_bins = np.max(hh)-np.min(hh)+1
				plt.hist(hh, n_bins, density=False, histtype='bar')
				plt.savefig(out_folder+"samples.pdf")
				plt.clf()

	if full_report:
		with open(in_folder+"EvaluationLog.json", 'r') as f:
			print("EvaluationLog")
			print(json.load(f))

	if full_report:
		with open(in_folder+"EvaluationDurations.json", 'r') as f:
			print("Average duration: "+str(np.mean(json.load(f)))+"\n")

	with open(in_folder+"TargetDistribution.json", 'r') as f:
		targetData = json.load(f)

	c_id = 'id'
	c_filtered = 'filtered fitness'
	c_experimental = 'experimental fitness'
	c_dp = 'DPs'

	df = pd.DataFrame(columns=[c_id, c_filtered, c_experimental, c_dp])

	count = count_logs(in_folder)

	plt.clf()

	if singles:
		out_singles = out_folder+"singles/"
		out_frames = out_folder+"singles/frames/"
		os.makedirs(out_frames, exist_ok=True)

	# print("Start...")
	for r in range(1,count):
		file_name = 'evalLog['+str(r)+'].json'
		out_name = 'evalLog['+str(r)+'].pdf'
		with open(in_folder+file_name, 'r') as f:
			p = json.load(f)

			experimentalData = __matrix__(p[exp_string].replace(" -∞", "NaN").replace("\n","\t"))
			filteredData = __matrix__(p[fil_string])
			deltaData = np.absolute(filteredData-targetData)

			values_count = np.count_nonzero(~np.isnan(experimentalData))

			df.loc[r] = [r, p[fil_eval], p[exp_eval], values_count]

			if singles:
				fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2)
				fig.suptitle(file_name)
				[setAxisStyle(ax) for ax in [ax1, ax2, ax3, ax4]]
				
				ax1.imshow(experimentalData)
				ax1.title.set_text('DP = '+str(values_count))
				
				ax2.imshow(filteredData)
				ax2.title.set_text('Filtered')
				
				ax3.imshow(targetData)
				ax3.title.set_text('Target')
				
				ax4.imshow(deltaData)
				ax4.title.set_text('Fitness = '+str(round(p[fil_eval],5)))

				plt.savefig(out_singles+out_name)
				plt.close(fig)

				plt.imshow(experimentalData)
				plt.savefig(out_frames+str(values_count)+"-["+str(r)+"].pdf")
				plt.clf()

	df.to_csv(out_folder+'evaluations.csv', index=False)

	__fitness_histogram__(df[c_filtered], out_folder+"fitness_pdf_filtered.pdf", "Filtered-matrix fitness pdf")
	__fitness_histogram__(df[c_experimental], out_folder+"fitness_pdf_experimental.pdf", "Experimental-matrix fitness pdf")

	counts, bin_edges = np.histogram(df[c_filtered], bins=10, density=True)
	pdf=counts/sum(counts)
	plt.plot(bin_edges[1:],pdf,label="filtered pdf")
	counts, bin_edges = np.histogram(df[c_experimental], bins=10, density=True)
	pdf=counts/sum(counts)
	plt.plot(bin_edges[1:],pdf,label="experimental pdf")
	plt.title("Fitness comparison: experimental vs filtered")
	plt.legend(bbox_to_anchor=(0, 1.02, 1, 0.2), loc="lower left",
                mode="expand", borderaxespad=0, ncol=2)
	plt.savefig(out_folder+"fitness_pdf_comparison.pdf")
	plt.clf()

	plt.plot(range(0,len(df[c_filtered])),df[c_filtered])
	plt.gca().set_xlabel("evaluations")
	plt.gca().set_ylabel("fitness")
	plt.title("Experimental fitness")
	plt.savefig(out_folder+"fitness_sample-by-sample.pdf")
	plt.clf()
	# print("DONE")















