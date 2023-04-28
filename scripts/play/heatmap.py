import seaborn as sns
import numpy as np
import matplotlib.pylab as plt
from matplotlib.colors import LinearSegmentedColormap

agents = ["BMRH*","BMRH**","EF-hc-linear","EF-hc-poly,2",
"EF-id-linear","EF-id-poly,2","SF-linear","SF-nn10%-12N","SF-nn10%-1N",
"SF-nn5%-12N","SF-nn5%-1N","SF-nn1%-12N","SF-nn1%-1N"]
opponents = ["RND","3xRND","delta","BMRH*","3xBMRH*","delta"]

vs_rnd =   [0.9598,	0.9475,	0.9823,	0.9839,	0.9857,	0.9854,	0.9407,	0.954,	0.9521,	0.9322,	0.9566,	0.8729,	0.9531]
vs_3rnd =  [0.8796,	0.8067,	0.9797,	0.9628,	0.973,	0.9696,	0.3785,	0.6619,	0.8496,	0.6982,	0.8464,	0.8327,	0.7638]
vs_bmrh =  [0.4951, 0.5356, 0.5405, 0.5141, 0.5361, 0.5983, 0.3155, 0.4658, 0.4606, 0.4387, 0.424 , 0.3388, 0.3841]
vs_3bmrh = [0.2519,	0.2244,	0.4414,	0.363,	0.4386,	0.4135,	0.0257,	0.0811,	0.1692,	0.0932,	0.1095,	0.1114,	0.0983]


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

cdict = {'red':    [(0.0,  0.0, 0.0),
					(0.5,  1.0, 1.0),
					(1.0,  1.0, 1.0),],

         'green':  [(0.0,  1.0, 1.0),
         			(0.5,  1.0, 1.0),
                    (1.0,  0.0, 0.0)],

         'blue':   [(0.0,  0.0, 0.0),    
         			(0.5,  1.0, 1.0),     			
                    (1.0,  0.0, 0.0)]}

red_white_green = LinearSegmentedColormap('White', cdict)
plt.register_cmap(cmap=red_white_green)

plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False
plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True

plt.figure(figsize=(10,4))

delta1 = np.subtract(vs_rnd,vs_3rnd)
delta2 = np.subtract(vs_bmrh,vs_3bmrh)
delta = delta2
vmin = min(min(abs(delta)),min(abs(delta)))*100
vmax = max(max(abs(delta)),max(abs(delta)))*100

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