import seaborn as sns
import numpy as np
import matplotlib.pylab as plt
from matplotlib.colors import LinearSegmentedColormap

agents = ["BMRH*","MCTS*","SRH*","BMRH**","l-5f","p2-5f","p3-5f","l-18f","p2-18f","p3-18f"]
opponents = ["RND","3xRND","delta","BMRH*","3xBMRH*","delta"]

vs_rnd =   [0.963,	0.965,	0.791,	0.947 ,	0.985 ,	0.984,	0.982,	0.986,	0.986,	0.964]
vs_3rnd =  [0.864,	0.902,	0.474,	0.821,	0.986,	0.959,	0.971,	0.98,	0.98,	0.926]
vs_bmrh =  [0.498,	0.488,	0.143,	0.545,	0.536,	0.516,	0.528,	0.548,	0.600 ,	0.538]
vs_3bmrh = [0.257,	0.236,	0.012,	0.227,	0.444,	0.375,	0.388,	0.458,	0.437,	0.306]


data = np.array([
	vs_rnd, # vs RND
	vs_3rnd, # vs 3 RND
	abs(np.subtract(vs_rnd,vs_3rnd)), # delta
	vs_bmrh, # vs BMRH*
	vs_3bmrh, 
	np.subtract(vs_bmrh,vs_3bmrh)
	])

data = np.multiply(data, 100)

colorbar_ticks = np.multiply([0, 0.5, 1],100)

cdict = {'red':   [(0.0,  1.0, 1.0),
                   (1.0,  1.0, 1.0)],

         'green': [(0.0,  1.0, 1.0),
                   (1.0,  0.0, 0.0)],

         'blue':  [(0.0,  1.0, 1.0),         			
                   (1.0,  0.0, 0.0)]}

red_white_green = LinearSegmentedColormap('White', cdict)
plt.register_cmap(cmap=red_white_green)

plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False
plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True

plt.figure(figsize=(10,4))

delta1 = np.subtract(vs_rnd,vs_3rnd)
delta2 = np.subtract(vs_bmrh,vs_3bmrh)
vmin = min(min(abs(delta2)),min(abs(delta2)))*100
vmax = max(max(abs(delta2)),max(abs(delta2)))*100

print(vmin)
print(vmax)

# vmin = 0.001 *100
# vmax = 0.352 *100

plot = sns.heatmap(data, linewidth=1, annot=True, square=True,
	vmin=vmin, vmax=vmax, fmt=".1f",
	cmap="White",
	xticklabels = agents, yticklabels = opponents,
  cbar_kws={"shrink": 0.835,"pad": 0.01})

plt.title("1 opponent vs 3 opponents", loc="left", fontdict={"fontweight":"bold"})
plt.xticks(rotation=45)

cbar = plot.collections[0].colorbar
cbar.set_ticks(colorbar_ticks)
cbar.set_ticklabels(np.char.mod('%d%%', colorbar_ticks))

plt.tight_layout()

plt.savefig("batch.pdf", format="pdf")