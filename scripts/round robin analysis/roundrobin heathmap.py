import seaborn as sns
import numpy as np
import matplotlib.pylab as plt
from matplotlib.colors import LinearSegmentedColormap

agents = [
"RND","BMRH*","BMRH**","EF-hc-linear","EF-hc-poly,2",
"EF-id-linear","EF-id-poly,2","SF-linear","SF-nn10%-12N","SF-nn10%-1N",
"SF-nn5%-12N","SF-nn5%-1N","SF-nn1%-12N","SF-nn1%-1N"
]

agents_extra = agents.copy()
agents_extra.append("avg")

data = np.array([                                                                                                     #AVG
[0.48965, 0.029 , 0.0386, 0.0151, 0.0165, 0.014 , 0.0143, 0.0423, 0.0243, 0.025 , 0.0309, 0.0224, 0.0681, 0.0243,     0.0610],
[0.9614,  0.4987, 0.4655, 0.4654, 0.4793, 0.4462, 0.4043, 0.6816, 0.5184, 0.5206, 0.5332, 0.5672, 0.6068, 0.604 ,     0.5538],
[0.9453,  0.529 , 0.4966, 0.4979, 0.4993, 0.4865, 0.4397, 0.7089, 0.5597, 0.5699, 0.5737, 0.604 , 0.6294, 0.6457,     0.5847],
[0.9847,  0.5345, 0.5018, 0.5001, 0.5292, 0.4986, 0.4608, 0.6945, 0.5991, 0.6025, 0.6249, 0.6304, 0.7117, 0.6698,     0.6102],
[0.9821,  0.5201, 0.4997, 0.4708, 0.5   , 0.4912, 0.4345, 0.6761, 0.5755, 0.5691, 0.6019, 0.6043, 0.6901, 0.6485,     0.5903],
[0.9852,  0.5536, 0.5128, 0.5015, 0.5088, 0.5000, 0.4529, 0.6984, 0.6061, 0.6052, 0.6343, 0.6372, 0.7234, 0.6823,     0.6144],
[0.9849,  0.5953, 0.5598, 0.5393, 0.5655, 0.5473, 0.5   , 0.7553, 0.6376, 0.6457, 0.6687, 0.6793, 0.7508, 0.7271,     0.6540],
[0.9393,  0.3079, 0.2792, 0.305 , 0.323 , 0.2998, 0.2435, 0.4964, 0.3807, 0.3735, 0.3874, 0.3853, 0.5077, 0.4416,     0.4050],
[0.9542,  0.4702, 0.4289, 0.4005, 0.4225, 0.3921, 0.361 , 0.6069, 0.49545,0.4849, 0.5235, 0.5196, 0.5988, 0.5406,     0.5142],
[0.9522,  0.4652, 0.4179, 0.397 , 0.4274, 0.3916, 0.3517, 0.6156, 0.4952, 0.496 , 0.4829, 0.5166, 0.5987, 0.5433,     0.5108],
[0.932 ,  0.4444, 0.4039, 0.3742, 0.3956, 0.3623, 0.3286, 0.5867, 0.4496, 0.46  , 0.4964, 0.4354, 0.5389, 0.5061,     0.4796],
[0.9578,  0.4221, 0.3841, 0.369 , 0.3948, 0.3614, 0.3197, 0.6058, 0.4626, 0.4678, 0.5303, 0.4954, 0.6   , 0.5337,     0.4932],
[0.8754,  0.3495, 0.313 , 0.287 , 0.3079, 0.272,  0.2449, 0.4482, 0.382,  0.3811, 0.4312, 0.35  , 0.4905, 0.3809,     0.3938],
[0.9602,  0.384,  0.3439, 0.3298, 0.3507, 0.3166, 0.2712, 0.552 , 0.4469, 0.445 , 0.468 , 0.4578, 0.5731, 0.4969,     0.4569]
])

data = np.multiply(data, 100)

colorbar_ticks = np.multiply([0, 0.3, 0.5, 0.7, 1],100)

cdict = {'red':   [(0.0,  1.0, 1.0),
				           (0.3,  1.0, 1.0),
                   (0.5,  1.0, 1.0),
                   (0.7,  0.0, 0.0),
                   (1.0,  0.0, 0.0)],
               
         'green': [(0.0,  0.0, 0.0),
                   (0.3,  0.0, 0.0),
                   (0.5,  1.0, 1.0),
                   (0.7,  1.0, 1.0),
                   (1.0,  1.0, 1.0)],

         'blue':  [(0.0,  0.0, 0.0),
         		       (0.3,  0.0, 0.0),         			
                   (0.5,  1.0, 1.0),
                   (0.7,  0.0, 0.0),
                   (1.0,  0.0, 0.0)]}

red_white_green = LinearSegmentedColormap('RedWhiteGreen', cdict)
plt.register_cmap(cmap=red_white_green)

plt.rcParams['xtick.bottom'] = plt.rcParams['xtick.labelbottom'] = False
plt.rcParams['xtick.top'] = plt.rcParams['xtick.labeltop'] = True

plt.figure(figsize=(10,9))

plot = sns.heatmap(data, linewidth=1, annot=True, square=True,
	vmin=0, vmax=100, fmt=".1f",
	cmap="RedWhiteGreen",
	xticklabels = agents_extra, yticklabels = agents,
  cbar_kws={"shrink": 0.835,"pad": 0.01},
  annot_kws={'size':14})

plt.title("Round robin tournament", loc="left", fontdict={"fontweight":"bold"})
plt.xticks(rotation=45)

cbar = plot.collections[0].colorbar
cbar.set_ticks(colorbar_ticks)
cbar.set_ticklabels(np.char.mod('%d%%', colorbar_ticks))

if False:
	for i in range(0,13):
		plot.texts[13*10+i].set_weight('bold')

plt.tight_layout()

plt.savefig("roundrobintournament.pdf", format="pdf")