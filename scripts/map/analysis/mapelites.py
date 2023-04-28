import os, math
from os.path import splitext, exists
import json as j
import pandas as pd 
from pandas import json_normalize
from itertools import combinations
import matplotlib.pyplot as plt
import matplotlib.tri as tri
import matplotlib.animation as animation
import numpy as np
import seaborn as sns
from ast import literal_eval
from PIL import Image
import io
from functools import reduce
from numpy import linalg as npla
from sklearn.preprocessing import MinMaxScaler, RobustScaler
import ffmpy

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TO ACCESS DATA ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #
#																										 #
#  p 							----- []: list of points												 #
#   [i]    							----- []: entry i 													 #
#	  [0]								----- {}: behavioural data 										 #
#		 ['value']							----- [int] : coordinates in the behavioural bins 			 #
#	  [1]								----- {}: data about the point 									 #
#		 ['x']								----- {}: search space coordinates 							 #
#			  [agentConfig]						----- [int]: hyperparameter indices of the agent 		 #
#			  [weights]							----- [double]: weights of the EB heuristic 			 #
#	     ['fitness']						----- double: win rate 										 #
#	     ['behaviours']						----- [double]: actual behavioural metrics   				 #
#																										 #
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ EXAMPLE DATA ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #
# [	[																									 #
# 		{"value": [14, 2, 32, 7]},																		 #
#  		{"x": 																							 #
#  			{"agentConfig": [1, 3, 2, 0, 0, 2, 1, 3, 0, 1],												 #
#  			 "weights": [0.6352716496284989,... , -0.5311921426627209, -0.7031426831211525],			 #
#  			 "playerName": "RFFSimulation",																 #
#  			 "converter": {}																			 #
#  			},																							 #
#  		 "fitness": 0.07,																				 #
#  		 "behaviours": [8.79, 0.07, 63.47, 3.4]}														 #
# 	],																									 #
# 	...																									 #
# ]																										 #
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #

DEBUG = True

MAP_BEHAVIOUR_D = 'Behave_D'
MAP_BEHAVIOURS_V = 'behaviours'
MAP_FITNESS = "fitness"
MAP_HP = "hyperparameters"
MAP_WEIGHTS = "weights"
MAP_CONFIG = 'agentConfig'

MAP_GRAPH_SCALE=6

def quiet():
	global DEBUG
	DEBUG = False

def verbose():
	global DEBUG
	DEBUG = True

def d_print(t):
	if(DEBUG): print(t)	


def print_summary(directory, name):
	with open(directory+"timing.json") as f:
		time = j.load(f)
	with open(directory+"summary.json") as f:
		summary = j.load(f)
	print("[SUMMARY: "+name+" @:"+directory+"]")
	print(time[0])
	print(summary)
	print("\n")
	# to print info about the space uncomment and add analysis code on _space_
	# space = MAPElitesSpaceLoader.load(directory, name)

class MAPElitesSpaceLoader:
	def __load_json_space(data_file):
		d_print("Opening json file...")
		with open(data_file) as f:
		    data = j.load(f)

		df = pd.DataFrame(data=data)

		d_print("Normalizing json...")
		df_values = json_normalize(df[0])
		c = [MAP_BEHAVIOUR_D+str(i) for i in range(len(df_values['value'][0]))]
		df_values_c = pd.DataFrame(df_values['value'].to_list(), columns=c)

		d_print("Building dataframe...")
		normalised = json_normalize(df[1])
		
		if 'x.weights' in normalised.columns.values:
			df_info = pd.DataFrame(normalised[['fitness', 'behaviours', 'x.agentConfig', 'x.weights']])
			df_info.columns = [MAP_FITNESS, MAP_BEHAVIOURS_V, MAP_HP, MAP_WEIGHTS]
		else:
			df_info = pd.DataFrame(normalised[['fitness', 'behaviours', 'x.agentConfig']])
			df_info.columns = [MAP_FITNESS, MAP_BEHAVIOURS_V, MAP_HP]

		data = df_values_c.join(df_info)
		d_print("Dataframe built.")

		csv_file_path = splitext(data_file)[0]+".csv"
		d_print("Saving to "+csv_file_path)
		data.to_csv(csv_file_path, index=False)
		d_print(".csv saved, return.")
		return data

	def __load_csv_space(data_file):
		d_print("Loading csv...")
		df = pd.read_csv(data_file)
		d_print("csv loaded.")
		return df

	def __load_space_points(data_file):
		d_print("[LOADING SPACE POINTS]")
		# if the file specifies a valid extension
		if((not data_file.endswith(".csv")) and (not data_file.endswith(".json"))):
			if(exists(data_file+str(".csv"))):
				return MAPElitesSpaceLoader.__load_csv_space(data_file+".csv");
			elif(exists(data_file+str(".json"))):
				return MAPElitesSpaceLoader.__load_json_space(data_file+".json")
		# else, if the file has no extension check if .json or .csv exist
		elif(exists(data_file)):
			if(data_file.endswith(".csv")):
				return MAPElitesSpaceLoader.__load_csv_space(data_file)
			elif(data_file.endswith(".json")):
				return MAPElitesSpaceLoader.__load_json_space(data_file)
		# if nothing succeded, report error.
		print("Error loading space "+str(data_file))

	def __load_bins(bins_file):
		with open(bins_file) as f:
			bins = j.load(f)
			bins = json_normalize(bins)
			return list(bins["limits"])

	def __load_size(size_file):
		with open(size_file) as f:
			size = j.load(f)
			return size

	def __load_behaviours(behaviours_file):
		with open(behaviours_file) as f:
			behaviours = j.load(f)
			return behaviours

	def __load_supports(supports_file):
		with open(supports_file) as f:
			support = j.load(f)
			return support

	def __load_history(history_file, bins, behaviours_count, supports_count):
		d_print("[LOADING SPACE HISTORY]")
		file_no_extension = history_file.replace(".json","");
		file_csv =file_no_extension+".csv"
		file_binned_csv = file_no_extension+"_binned"+".csv"
		if(exists(file_binned_csv)):
			return MAPElitesSpaceLoader.__load_csv_space(file_binned_csv)
		else:
			with open(history_file) as f:
				history = j.load(f)
				df = pd.DataFrame(data=history)
				d_print("Normalizing json...")
				agent_df = json_normalize(df["x"])
				
				behaviour_columns = ["statsWithHistory.stats.behaviour["+str(i)+"].history" for i in range(behaviours_count)]
				support_columns   = ["statsWithHistory.stats.support["+str(i)+"].history" for i in range(supports_count)]

				d_print("Extracting behaviour stats...")
				behaviour_data = agent_df[behaviour_columns]
				d_print("Extracting support stats...")
				support_data = agent_df[support_columns]


				behaviour_columns_names = [MAPElitesSpace.behaviour_history_field(i) for i in range(behaviours_count)]
				support_columns_names = [MAPElitesSpace.support_history_field(i) for i in range(supports_count)]

				behaviour_data.columns = behaviour_columns_names
				support_data.columns = support_columns_names
				
				agent_columns = [MAP_CONFIG]
				if 'weights' in agent_df.columns:
					agent_columns.append('weights')

				d_print("Combining columns...")
				hdf = pd.DataFrame(df[['time', 'fitness', 'behaviours']])
				hdf = hdf.join(agent_df[agent_columns]).join(behaviour_data).join(support_data)

				d_print("Dataframe built.")

				d_print("Saving history csv to "+file_csv)
				hdf.to_csv(file_csv, index=False)
				d_print(".csv saved.")

				# hdf['behaviours'] = hdf['behaviours'].apply(literal_eval)
				
				d_print("Adding bin indices...")
				t = hdf.apply(lambda r: MAPElitesSpace.bin_behaviours(r['behaviours'],bins), axis = 1)
				b_columns = [MAP_BEHAVIOUR_D+str(i) for i in range(behaviours_count)]

				hdf[b_columns] = pd.DataFrame(t.values.tolist(), index= t.index)

				d_print("Bin indices added.")
				
				d_print("Saving binned history csv to "+file_binned_csv)
				hdf.to_csv(file_binned_csv, index=False)
				d_print(".csv saved, return.")

				return hdf

	def load(folder, name="unknown_space"):
		
		df = MAPElitesSpaceLoader.__load_space_points(folder+"space")
		bins = MAPElitesSpaceLoader.__load_bins(folder+"bins.json")
		size = MAPElitesSpaceLoader.__load_size(folder+"spaceSize.json")
		behaviours = MAPElitesSpaceLoader.__load_behaviours(folder+"behaviours.json")
		supports = MAPElitesSpaceLoader.__load_supports(folder+"support.json")
		history = MAPElitesSpaceLoader.__load_history(folder+"spaceHistory.json", bins, len(behaviours), len(supports))
		
		with open(folder+"p.json") as json_file:
			exp_params = j.load(json_file)

		return MAPElitesSpace(name, df, bins, size, behaviours, history, exp_params)

class plot_bin_formatter:
	def __init__(self, label, bins, n_ticks, scale=1):
		self.label = label
		self.bin = bins
		self.size = len(bins)
		self.n_ticks = n_ticks
		self.scale = scale

	def ticks(self):
		return np.linspace(0,self.size-1, self.n_ticks)

	def labels(self, f='{:,.1f}'):
		labels = np.linspace(self.bin[0],self.bin[-1], self.n_ticks)
		return [f.format(x) for x in labels]

	def labels_style(self, f_size=8, f_weight='light'):
		return {'weight' : f_weight,'size' : f_size*self.scale, 'rotation' : 0}
		

class MAPElitesSpace:

	def __init__(self, name, df, bins, size, behaviours, history, exp_params):
		self.df = df
		self.bins = bins
		self.size = size
		self.name = name
		self.behaviours = behaviours
		self.exp_params = exp_params
		self.duration = exp_params["search/totaliterations"]
		self.history = history
		if(not history is None):
			self.df = self.get_timed_slice(1)

	def projections(self, dimensions_count):
		dims = range(len(self.size))
		return list(combinations(dims,dimensions_count))

	def projections_2D(self):
		return self.projections(2)

	def project(self, dd, extra_fields=[]):
		fields = MAPElitesSpace.behaviour_field(dd)
		d_print("Projecting over dimensions: "+str(dd)+" fields: "+str(fields))
		idx = self.df.groupby(fields)[MAP_FITNESS].idxmax()
		fields = fields + [MAP_FITNESS] + extra_fields
		df = self.df.loc[idx][fields].reset_index()
		
		return df

	def np_behaviour_projection_2D(self, bb):
		dd = [self.from_behaviour_to_index(b) for b in bb]
		return self.np_projection_2D(dd)

	def from_behaviour_to_index(self, b):
		return self.behaviours.index(b)

	def from_index_to_behaviour(self, i):
		return self.behaviours[i]
	
	def np_projection_2D(self, dd):
		pj = self.project(dd)
		field_0 = MAPElitesSpace.behaviour_field(dd[0])
		field_1 = MAPElitesSpace.behaviour_field(dd[1])
		
		x = pj[field_0]
		y = pj[field_1]
		z = pj[MAP_FITNESS]

		x_size  = self.size[dd[0]]
		y_size  = self.size[dd[1]]
		with np.errstate(invalid='ignore'):
			m = np.empty((x_size, y_size))*np.nan
		try:
			m[x,y] = z
		except IndexError as error:
			print("[X] min: "+str(min(x))+" max: "+str(max(x)))
			print("[Y] min: "+str(min(y))+" max: "+str(max(y)))
		return m

	def np_projection_2D_cov(self, dd):
		scaling = 30
		field_0 = MAPElitesSpace.behaviour_field(dd[0])
		field_1 = MAPElitesSpace.behaviour_field(dd[1])

		h_field_0 = MAPElitesSpace.behaviour_history_field(dd[0])
		h_field_1 = MAPElitesSpace.behaviour_history_field(dd[1])

		pj = self.project(dd, extra_fields=[h_field_0, h_field_1])

		def arrows_synth(r):
			data_h0 = literal_eval(r[h_field_0])
			data_h1 = literal_eval(r[h_field_1])
			corr_mat = np.cov([data_h0,data_h1])
			(s0, s1), (v0, v1) = npla.eig(corr_mat)
			return pd.Series([s0,v0,s1,v1], index=["s0","v0","s1","v1"])


		eig = pj.apply(arrows_synth, axis=1)

		scaler = MinMaxScaler() # MinMaxScaler RobustScaler+
		scaler.fit(np.reshape(list(eig["s0"])+list(eig["s1"]), (-1,1)))
		s0_scaled = scaler.transform(eig[["s0"]])
		s1_scaled = scaler.transform(eig[["s1"]])
		
		dir0 = np.multiply(s0_scaled, eig[["v0"]])
		dir1 = np.multiply(s1_scaled, eig[["v1"]])

		arrows_data = pj[[field_0,field_1]].join([dir0,dir1])
		arrows_data.columns = ['x','y','dir0','dir1']
			
		return arrows_data



	def bin_behaviours(behaviours, bins):
		binning = [MAPElitesSpace.bin_behaviour(behaviours[i],bins[i]) for i in range(len(behaviours))] 
		return binning

	def bin_behaviour(behaviour, b_bin):
		for index in range(len(b_bin)):
			if behaviour < b_bin[index]:
				return index
		return len(b_bin)

	def coverage(self):
		tot_behaviours = reduce((lambda x, y: x * y), self.size)
		seen_behaviuours = self.behaviours_count()
		return seen_behaviuours/tot_behaviours

	def behaviours_count(self):
		return len(self.df.index)

	def behaviours_count_history(self, samples=10):
		seq=[]
		for time in np.delete(np.linspace(0, 1, samples+1),0):
			space = self.get_timed_space(time)
			seq.append(space.behaviours_count())
		return seq

	def export_behaviours_exploration(self, folder="./out/", file_format="pdf", samples=10, show_extras=False):
		ax = self.get_axes_behaviours_exploration(samples, show_extras=show_extras)
		os.makedirs(folder, exist_ok=True)
		ax.get_figure().tight_layout()
		ax.get_figure().savefig(folder+self.name+"_behaviours_exploration."+file_format, format=file_format)

	def get_axes_behaviours_exploration(self, samples, ext_ax=plt.figure().subplots(), show_extras=False):
		seq = self.behaviours_count_history(samples=samples)
		data = [0]
		data.extend(seq)
		x = np.linspace(0,self.duration,len(data))
		ext_ax.plot(x,data, label=self.name+" exploration")
		if(show_extras):
			dt = [data[i]-data[i-1] for i in range(1,len(data))]
			ext_ax.plot(x,x, label=self.name+" ideal")
			ext_ax.plot(x[1:],dt, label=self.name+" delta")
			ext_ax.axvline(self.exp_params["search/bootiterations"], 0, x[-1],
				linestyle ="--", color ='red', label=self.name+" booting")
		ext_ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left', borderaxespad=0.0)
		return ext_ax

	def export_search_progress(self, folder="./out/", file_name="search_progress", samples=10):
		frames = []
		for time in np.delete(np.linspace(0, 1, samples+1),0):
			slice_name=str(int(time*samples))+":"+str(samples)
			d_print("> Slicing hitory ["+slice_name+"]")
			space = self.get_timed_space(time, suffix=slice_name)
			
			d_print("Creating frame...")
			f = space.export_plots_2D_single(save=False, show_arrows=False)
			d_print("Frame created.")

			d_print("Rastering frame...")
			buf = io.BytesIO()
			f.savefig(buf, dpi=50, format="png")
			buf.seek(0)
			img = Image.open(buf)
			frames.append(img)
			d_print("Frame rastered.")
			plt.close(f)

		os.makedirs(folder, exist_ok=True)
		full_file_name = folder+file_name
		full_file_name_gif = full_file_name+".gif"
		full_file_name_mp = full_file_name+".mp4"
		frames[0].save(full_file_name_gif,save_all=True, append_images=frames[1:], optimize=False, duration=150, loop=0)
		ff = ffmpy.FFmpeg(inputs={full_file_name_gif: '-y'}, outputs={full_file_name_mp: '-pix_fmt yuv420p -vf "scale=trunc(iw/2)*2:trunc(ih/2)*2"'})
		ff.run()
		
	def get_timed_slice(self, time):
		actual_time = self.duration*time
		d_print("Slicing at "+str(int(actual_time))+" of "+str(self.duration))
		sel = self.history['time'] <= actual_time
		df = self.history[sel]

		groups = [MAP_BEHAVIOUR_D+str(i) for i in range(len(self.behaviours))]
		selects = df.columns

		df = df.groupby(groups, as_index=False).apply(lambda s: s.loc[s['fitness'].idxmax(), selects]).reset_index(drop=True)
		
		d_print("Sliced "+str(len(df.index))+" items.")
		return df

	def get_timed_space(self, time, suffix=""):
		df = self.get_timed_slice(time)
		name = self.name+"["+suffix+"]"

		return MAPElitesSpace(name, df, self.bins, self.size, self.behaviours, None, self.exp_params)
		
	def export_plots_2D(self, folder="./out/", prefix = "h", plots_folder = "plots/", file_format = "pdf", show_arrows=True):
		plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True
		plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False

		plots_path = folder+plots_folder
		os.makedirs(plots_path, exist_ok=True)

		for dd in self.projections_2D():
			
			bb = [self.from_index_to_behaviour(d) for d in dd]
			m = self.np_projection_2D(dd)
			arrows = self.np_projection_2D_cov(dd) if(show_arrows) else None

			ax = self.get_axes_2D(dd,bb,m, title=self.name, arrows=arrows)

			plot_name = plots_path+prefix+str(dd)+" "+str(bb)+"."+file_format
			ax.get_figure().tight_layout()
			ax.get_figure().savefig(plot_name, format=file_format)

	def export_plots_2D_single(self, folder="./out/", columns=4, prefix = "full_", plots_folder = "plots/", file_format = "pdf", show_arrows=True, save=True):
		pjs = self.projections_2D()
		rows = math.ceil(len(pjs)/columns);
		f, axes = plt.subplots(rows,columns,figsize=(30*columns,30*rows))

		for i in range(len(pjs)):
			dd = pjs[i]
			bb = [self.from_index_to_behaviour(d) for d in dd]
			m = self.np_projection_2D(dd)
			
			c = int(i%columns) 
			r = int(i/columns)

			arrows = self.np_projection_2D_cov(dd) if(show_arrows) else None
			self.get_axes_2D(dd,bb,m, ext_ax=axes[r,c], arrows=arrows)

		for i in range(len(pjs),rows*columns):
			c = int(i%columns) 
			r = int(i/columns)
			axes[r,c].remove()

		f.suptitle(self.name)

		if(save):
			plots_path = folder+plots_folder
			os.makedirs(plots_path, exist_ok=True)
			plot_name = plots_path+prefix+self.name+"_space"+"."+file_format
			d_print("Saving plot to \""+plot_name+"\"")
			f.savefig(plot_name, format=file_format)

		return f

	def get_axes_2D(self, dd, bb, m, title="", n_ticks=10, ext_ax=None, scale=1, arrows=None):

		if(ext_ax is None):
			ext_ax=plt.figure(figsize=(30,30)).subplots()

		x_formatter = plot_bin_formatter(bb[1], self.bins[dd[1]],n_ticks, scale=scale)
		y_formatter = plot_bin_formatter(bb[0], self.bins[dd[0]],n_ticks, scale=scale)

		sns.heatmap(m, ax=ext_ax, annot=False, square=True, cmap="coolwarm", linewidth=0, vmin=0, vmax=1,
			cbar_kws={"fraction": 0.045})

		cbar = ext_ax.collections[0].colorbar
		cbar.ax.tick_params(labelsize=8*scale*MAP_GRAPH_SCALE, length=3*scale*MAP_GRAPH_SCALE, width=1*scale*MAP_GRAPH_SCALE) #, pad=5*scale*MAP_GRAPH_SCALE

		ext_ax.set_xticklabels(x_formatter.labels(),x_formatter.labels_style())
		ext_ax.set_yticklabels(y_formatter.labels(),y_formatter.labels_style())
		
		ext_ax.tick_params(axis='both',labelsize=8*scale*MAP_GRAPH_SCALE,length=3*scale*MAP_GRAPH_SCALE, width=1*scale*MAP_GRAPH_SCALE, pad=5*scale*MAP_GRAPH_SCALE)
		ext_ax.tick_params(bottom=False, top=True, left=True, right=False)
		ext_ax.tick_params(labelbottom=False, labeltop=True, labelleft=True, labelright=False)
		
		if(not (arrows is None)):
			for _,a in arrows.iterrows():
				[y,x, [dy0, dx0] ,[dy1, dx1]] = a.T.values #[['x','y','dx','dy']]
				max_size = 0.5
				ext_ax.arrow(x+0.5,y+0.5, max_size*dx0,max_size*dy0, width=0.001)
				ext_ax.arrow(x+0.5,y+0.5, max_size*dx1,max_size*dy1, width=0.001)

		ext_ax.set_xticks(x_formatter.ticks())
		ext_ax.set_yticks(y_formatter.ticks())

		ext_ax.xaxis.set_label_position('top')

		ext_ax.set_xlabel(x_formatter.label, style='italic', fontsize=12*scale*MAP_GRAPH_SCALE)
		ext_ax.set_ylabel(y_formatter.label, style='italic', fontsize=12*scale*MAP_GRAPH_SCALE)

		if(title != ""):
			ext_ax.set_title(title, loc='left', fontweight="bold", fontsize=10*scale*MAP_GRAPH_SCALE)

		return ext_ax


	def get_performance_distribution_ax(self, ext_ax=plt.figure().subplots()):
		data = self.df[MAP_FITNESS]
		sns.distplot(data, kde = False, bins=10, norm_hist = False, label = self.name, ax=ext_ax)

		# ext_ax.hist(data, stacked=True, bins = 10, label=self.name+" IQ") # histtype="barstacked" step
		# ext_ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left', borderaxespad=0.0)
		return ext_ax

	def export_plot_performance_distribution(self, folder="./out/", prefix = "pd", plots_folder = "plots/", file_format = "pdf"):
		plots_path = folder+plots_folder
		os.makedirs(plots_path, exist_ok=True)
		ax = self.get_performance_distribution_ax()
		ax.get_figure().tight_layout()
		ax.get_figure().savefig(folder+self.name+"_performance_distribution."+file_format, format=file_format)

	def information_quality(self):
		return self.df[MAP_FITNESS].sum()

	def get_information_quality_series(self, samples=10):
		seq=[]
		for time in np.delete(np.linspace(0, 1, samples+1),0):
			space = self.get_timed_space(time)
			seq.append(space.information_quality())
		return seq

	def get_information_quality_ax(self, ext_ax=plt.figure().subplots(), samples=10):
		seq = self.get_information_quality_series(samples=samples)
		data = [0]
		data.extend(seq)
		x = np.linspace(0,self.duration,len(data))
		ext_ax.plot(x, data, label=self.name+" IQ")
		ext_ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left', borderaxespad=0.0)
		return ext_ax

	def export_plot_information_quality(self, samples=10, folder="./out/", prefix = "q", plots_folder = "plots/", file_format = "pdf"):
		plots_path = folder+plots_folder
		os.makedirs(plots_path, exist_ok=True)
		ax = self.get_information_quality_ax(samples)
		ax.get_figure().tight_layout()
		ax.get_figure().savefig(folder+self.name+"_information_quality."+file_format, format=file_format)

	def behaviour_field(indeces):
		if (isinstance(indeces, tuple) or isinstance(indeces, list)):
			return [MAP_BEHAVIOUR_D+str(i) for i in indeces]
		else:
			return MAP_BEHAVIOUR_D+str(indeces)

	def behaviour_history_field(indeces):		
		if (isinstance(indeces, tuple) or isinstance(indeces, list)):
			return ["Behaviour["+str(i)+"]" for i in indeces]
		else:
			return "Behaviour["+str(indeces)+"]"

	def support_history_field(indeces):		
		if (isinstance(indeces, tuple) or isinstance(indeces, list)):
			return ["Support["+str(i)+"]" for i in indeces]
		else:
			return "Support["+str(indeces)+"]"


# [active testing code]
# pd.set_option('display.max_columns', None)
# pd.set_option('display.width', 200)

# folder = "./_data/SP2P/vs RND/eb-linear-id/"
# out_folder = "./test/"
# space = MAPElitesSpaceLoader.load(folder, "EB-id")
# space.export_plot_performance_distribution(folder=out_folder)

# space.export_plot_information_quality(folder=out_folder,samples=10)
# space.export_behaviours_exploration(folder=out_folder,samples=30, show_extras=True)
# space.export_plots_2D(folder=out_folder)
# space.export_plots_2D_single(folder=out_folder)
# space.export_search_progress(folder=out_folder,samples=30)

