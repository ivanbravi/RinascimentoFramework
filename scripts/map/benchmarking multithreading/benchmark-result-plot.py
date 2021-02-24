import seaborn as sns
import numpy as np
import matplotlib.pylab as plt

samples = [5,10,20,30,40,50,60]
threads = [1,2,3,4,5]

data_raw = np.array([
np.array([ 57 , 54 , 48, 52 , 39 ]),    # 5
np.array([ 111, 103, 89, 81 , 106]),    # 10
np.array([ 223, 175, 171, 192, 169]),    # 20
np.array([ 332, 311, 256, 264, 258]),    # 30
np.array([ 460, 378, 319, 346, 374]),    # 40
np.array([ 477, 542, 398, 419, 432]),    # 50 
np.array([ 693, 563, 566, 460, 497])])   # 60
#		   1      2      3      4      5

data_per_game = np.array([
np.divide(np.array([ 57 , 54 , 48, 52 , 39 ]),samples[0]),    # 5
np.divide(np.array([ 111, 103, 89, 81 , 106]),samples[0]),    # 10
np.divide(np.array([ 223, 175, 171, 192, 169]),samples[1]),    # 20
np.divide(np.array([ 332, 311, 256, 264, 258]),samples[2]),    # 30
np.divide(np.array([ 460, 378, 319, 346, 374]),samples[3]),    # 40
np.divide(np.array([ 477, 542, 398, 419, 432]),samples[4]),    # 50 
np.divide(np.array([ 693, 563, 566, 460, 497]),samples[5])])   # 60
#		             1      2      3      4      5

data_ratio = np.array([
np.divide(np.array([ 57 , 54 , 48, 52 , 39 ]),57 ),    # 5
np.divide(np.array([ 111, 103, 89, 81 , 106]),111),    # 10
np.divide(np.array([ 223, 175, 171, 192, 169]),223),    # 20
np.divide(np.array([ 332, 311, 256, 264, 258]),332),    # 30
np.divide(np.array([ 460, 378, 319, 346, 374]),460),    # 40
np.divide(np.array([ 477, 542, 398, 419, 432]),477),    # 50 
np.divide(np.array([ 693, 563, 566, 460, 497]),693)])   # 60

plot = sns.heatmap(data_raw, linewidth=1, annot=True, square=True, fmt=".1f",
	cmap="RdBu", xticklabels = threads, yticklabels = samples)
plot.set(xlabel="threads", ylabel="samples")
plt.title("Duration [s]", loc="left", fontdict={"fontweight":"bold"})
plt.savefig("benchmark_raw.pdf", format="pdf")
plt.clf()

plot = sns.heatmap(data_per_game, linewidth=1, annot=True, square=True, fmt=".1f",
	cmap="RdBu", xticklabels = threads, yticklabels = samples)
plot.set(xlabel="threads", ylabel="samples")
plt.title("Single game duration [s]", loc="left", fontdict={"fontweight":"bold"})
plt.savefig("benchmark_per_game.pdf", format="pdf")
plt.clf()

plot = sns.heatmap(data_ratio, linewidth=1, annot=True, square=True, fmt=".2f",
	cmap="RdBu", xticklabels = threads, yticklabels = samples)
plot.set(xlabel="threads", ylabel="samples")
plt.title("Duration ratio w.r.t 1 thread", loc="left", fontdict={"fontweight":"bold"})
plt.savefig("benchmark_ratio.pdf", format="pdf")
plt.clf()


# 1 5  100 57
# 1 10 100 111
# 1 20 100 223
# 1 30 100 332
# 1 40 100 460
# 1 50 100 477
# 1 60 100 693

# 2 5  100 54
# 2 10 100 103
# 2 20 100 175
# 2 30 100 311
# 2 40 100 378
# 2 50 100 542
# 2 60 100 563

# 3 5  100 48
# 3 10 100 89
# 3 20 100 171
# 3 30 100 256
# 3 40 100 319
# 3 50 100 398
# 3 60 100 566

# 4 5  100 52
# 4 10 100 81
# 4 20 100 192
# 4 30 100 264
# 4 40 100 346
# 4 50 100 419
# 4 60 100 460

# 5 5  100 39
# 5 10 100 106
# 5 20 100 169
# 5 30 100 258
# 5 40 100 374
# 5 50 100 432
# 5 60 100 497

