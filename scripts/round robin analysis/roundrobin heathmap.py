import seaborn as sns
import numpy as np
import matplotlib.pylab as plt
from matplotlib.colors import LinearSegmentedColormap

agents = ["RND","OSLA","BMRH*","MCTS*","SRH*","BMRH**",
"l-5f",
"p2-5f",
"p3-5f",
"l-18f",
"p2-18f",
"p3-18f"]
agents_extra = agents.copy()
agents_extra.append("avg")

data = np.array([
[0.48885  , 0.4941  , 0.028 , 0.0266  , 0.1916  , 0.0391  , 0.0148  , 0.0154  , 0.0172  , 0.0131  , 0.0134  , 0.0275  ,      0.1141],
[0.4853 , 0.4905  , 0.0258  , 0.0285  , 0.1299  , 0.0378  , 0.0166  , 0.0159  , 0.0146  , 0.0117  , 0.0117  , 0.0277  ,      0.1080],
[0.9625 , 0.9632  , 0.49835 , 0.5099  , 0.851   , 0.4507  , 0.4641  , 0.4842  , 0.4714  , 0.4518  , 0.399   , 0.4554  ,      0.5801],
[0.9651 , 0.9626  , 0.4875  , 0.49905 , 0.7878  , 0.436   , 0.4742  , 0.4807  , 0.4703  , 0.4493  , 0.3958  , 0.4374  ,      0.5705],
[0.7912 , 0.8561  , 0.1425  , 0.2066  , 0.4944  , 0.1423  , 0.115   , 0.0964  , 0.1011  , 0.1146  , 0.0951  , 0.1855  ,      0.2784],
[0.947  , 0.9477  , 0.5445  , 0.5598  , 0.8482  , 0.4966  , 0.4902  , 0.5044  , 0.5054  , 0.4852  , 0.4358  , 0.4951  ,      0.605 ],
[0.985  , 0.983   , 0.5358  , 0.5257  , 0.8849  , 0.5095  , 0.5     , 0.5165  , 0.5097  , 0.4921  , 0.447   , 0.534   ,      0.6186],
[0.9842 , 0.9835  , 0.5158  , 0.5193  , 0.9033  , 0.4951  , 0.4834  , 0.50005 , 0.4912  , 0.4811  , 0.4376  , 0.5227  ,      0.6098],
[0.9823 , 0.9849  , 0.5281  , 0.5294  , 0.8984  , 0.4943  , 0.4903  , 0.5086  , 0.5     , 0.4921  , 0.4451  , 0.5378  ,      0.6159],
[0.9861 , 0.9877  , 0.5478  , 0.5502  , 0.8849  , 0.5143  , 0.5079  , 0.519   , 0.5081  , 0.5     , 0.4588  , 0.5492  ,      0.6262],
[0.9864 , 0.9875  , 0.6003  , 0.604   , 0.9046  , 0.5636  , 0.553   , 0.5624  , 0.5552  , 0.5414  , 0.50005 , 0.5877  ,      0.6622],
[0.9638 , 0.9627  , 0.5375  , 0.5578  , 0.8049  , 0.4972  , 0.4656  , 0.4769  , 0.4618  , 0.4501  , 0.4118  , 0.49915 ,      0.5908]
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