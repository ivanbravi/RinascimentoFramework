import mapelites
from mapelites import d_print, plot_bin_formatter
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import os
from itertools import combinations

coverage_cmap = sns.color_palette("coolwarm", 3)
variation_cmap = sns.color_palette("coolwarm",7)

class MAPElitesOverlap:

	def __new__(cls, space1, space2):
		if(not MAPElitesOverlap.spaces_are_compatible(space1,space2)):
			print("[ERROR] "+space1.name+" and "+space2.name+" are incompatible")
			return None
		return super(MAPElitesOverlap, cls).__new__(cls)

	def spaces_are_compatible(space1, space2):
		d_print("Checking compatibility of space "+space1.name+" and "+space2.name)
		d_print("Computing shared metrics...")
		shared_metrics = MAPElitesOverlap.shared_metrics(space1,space2)
		
		d_print("Computing shared bins...")
		space1_metric_i = [space1.from_behaviour_to_index(b) for b in shared_metrics]
		space2_metric_i = [space2.from_behaviour_to_index(b) for b in shared_metrics]
		
		space1_shared_bin = [space1.bins[i] for i in space1_metric_i]
		space2_shared_bin = [space2.bins[i] for i in space2_metric_i]

		if( len(shared_metrics) <= 0):
			d_print("[ERROR] No common metrics")
			return False

		if( space1_shared_bin != space2_shared_bin):
			d_print("[ERROR] Bins not compatible")
			return False

		d_print("Spaces "+space1.name+" and "+space2.name+" are compatible.")
		return True

	def shared_metrics(space1, space2):
		return set(space1.behaviours) & set(space2.behaviours)


	def export_behaviour_explorations(self, folder="./out/", samples=10, file_format="pdf", file_name=None):
		if(file_name is None):
			file_name = self.space1.name+" vs "+self.space2.name
		f, ax = plt.subplots()
		self.space1.get_axes_behaviours_exploration(samples, ext_ax=ax, show_extras=False)
		self.space2.get_axes_behaviours_exploration(samples, ext_ax=ax, show_extras=False)
		f.tight_layout()
		os.makedirs(folder, exist_ok=True)
		f.savefig(folder+file_name+"."+file_format, format = file_format)

	def __init__(self, space1, space2):
		self.space1 = space1
		self.space2 = space2
		self.name = space1.name+" vs "+space2.name
		self.shared_metrics = MAPElitesOverlap.shared_metrics(space1,space2)

	def shared_projections_2D(self):
		c = list(combinations(self.shared_metrics,2))
		return c

	def export_coverage_plots_2D(self, dir_path, file_format = "pdf", store_csv=False, prefix="c", sub_dir="coverage/"):
		coverage_dir = dir_path+self.name+"/"+sub_dir
		os.makedirs(coverage_dir, exist_ok=True)

		space = self.space1

		plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True
		plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False

		for bb in self.shared_projections_2D():
			plot_path = coverage_dir+prefix+str(bb)+"."+file_format

			dd = [space.from_behaviour_to_index(b) for b in bb]
			m = self.coverage(bb)

			n_ticks = 10
			x_formatter = plot_bin_formatter(bb[1], space.bins[dd[1]],n_ticks)
			y_formatter = plot_bin_formatter(bb[0], space.bins[dd[0]],n_ticks)

			plt.clf()

			plt.title(str(self.name), loc='left', fontweight="bold")

			plot = sns.heatmap(m, cmap=coverage_cmap, vmin=0, vmax=2, linewidth=1.0)
			
			cbar = plot.collections[0].colorbar

			cbar.set_ticks([0.33, 1, 1.66])
			cbar.set_ticklabels([self.space1.name,"both", self.space2.name])

			plot.set_xticklabels(x_formatter.labels(),x_formatter.labels_style())
			plot.set_yticklabels(y_formatter.labels(),y_formatter.labels_style())
			
			plot.set_xticks(x_formatter.ticks())
			plot.set_yticks(y_formatter.ticks())

			plot.xaxis.set_label_position('top')

			plt.xlabel(x_formatter.label, style='italic')
			plt.ylabel(y_formatter.label, style='italic')

			plt.tight_layout()
			
			plt.savefig(plot_path, format=file_format)
			if(store_csv): np.savetxt(plot_path+".csv", m, delimiter=',', fmt="%1.0f")

	def export_variation_plots_2D(self, dir_path, format = "pdf", store_csv=False, prefix="v", sub_dir="variation/"):
		variation_dir = dir_path+self.name+"/"+sub_dir
		os.makedirs(variation_dir, exist_ok=True)

		space = self.space1

		plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True
		plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False

		for bb in self.shared_projections_2D():
			plot_path = variation_dir+prefix+str(bb)

			dd = [space.from_behaviour_to_index(b) for b in bb]
			m = self.variation(bb)

			n_ticks = 10
			x_formatter = plot_bin_formatter(bb[1], space.bins[dd[1]],n_ticks)
			y_formatter = plot_bin_formatter(bb[0], space.bins[dd[0]],n_ticks)

			plt.clf()

			plt.title(str(self.name), loc='left', fontweight="bold")

			plot = sns.heatmap(m, cmap=variation_cmap, vmin=-1, vmax=1, linewidth=1.0)

			cbar = plot.collections[0].colorbar

			cbar.set_ticks([-1, -0.75, -0.5, -0.25, 0, 0.25, 0.5, 0.75, 1])
			cbar.set_ticklabels([str(-1)+" "*7+self.space1.name, -0.75, -0.5, -0.25, "same", 0.25, 0.5, 0.75, str(1)+" "*7+self.space2.name])
			
			plot.set_xticklabels(x_formatter.labels(),x_formatter.labels_style())
			plot.set_yticklabels(y_formatter.labels(),y_formatter.labels_style())
			
			plot.set_xticks(x_formatter.ticks())
			plot.set_yticks(y_formatter.ticks())

			plot.xaxis.set_label_position('top')

			plt.xlabel(x_formatter.label, style='italic')
			plt.ylabel(y_formatter.label, style='italic')

			plt.tight_layout()

			plt.savefig(plot_path+"."+format, format=format)
			if(store_csv): np.savetxt(plot_path+".csv", m, delimiter=',', fmt="%1.4f")


	def variation(self,bb):
		m1 = self.space1.np_behaviour_projection_2D(bb)
		m2 = self.space2.np_behaviour_projection_2D(bb)

		flat_m1 = m1
		flat_m2 = m2

		m1_nan_mask = np.isnan(m1)
		m2_nan_mask = np.isnan(m2)

		m12_nan_mask = np.logical_and(m1_nan_mask,m2_nan_mask)

		flat_m1[m1_nan_mask] = 0.0
		flat_m2[m2_nan_mask] = 0.0

		delta = flat_m2-flat_m1
		delta[m12_nan_mask] = np.nan

		return delta

	def coverage(self,bb):
		m1 = self.space1.np_behaviour_projection_2D(bb)
		m2 = self.space2.np_behaviour_projection_2D(bb)

		categorise = np.vectorize(MAPElitesOverlap.__to_categorical)

		nonnan1_mask = np.logical_not(np.isnan(m1)).astype(int)
		nonnan2_mask = np.logical_not(np.isnan(m2)).astype(int)
		
		nodata12 = np.logical_and(np.isnan(m1),np.isnan(m2))
		out = (nonnan1_mask-nonnan2_mask).astype(float)
		out[nodata12] = np.nan

		out = categorise(out)

		return out

	def __to_categorical(i):
		switcher={
			0:1,
			1:0,
			-1:2
		}
		return switcher.get(i,np.nan)


# [active testing code]
# s1 = mapelites.MAPElitesSpaceLoader.load("./_data/1C2W/vs RHEA/eb-linear-simple/", "EB-l")
# s2 = mapelites.MAPElitesSpaceLoader.load("./_data/1C2W/vs RHEA/pb/", "PB")
# overlap = MAPElitesOverlap(s1,s2);
# # overlap.export_behaviour_explorations(samples=3)
# overlap.export_coverage_plots_2D("./plots/")
# overlap.export_variation_plots_2D("./plots/")
