import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

def load_csvs(path, start=1, end=100):
	files = [path+"r"+str(i)+".csv" for i in range(start,end+1)]
	df = pd.concat((pd.read_csv(f) for f in files))
	df["id"] = list(range(1,end+1))
	return df

def append_agent(df, agent):
	df["agent"] = agent
	return df
base_path='~/Desktop/Simulazioni/ntbea/data/SB/SSPP/'
paths = (base_path+"pdnn/100/",base_path+"pdnn/1000/",base_path+"lin/100/",base_path+"lin/1000/")
agents = ("pdnn100", "pdnn1K","lin100", "lin1K")

data = pd.concat([ append_agent(load_csvs(paths[a]),agents[a]) for a in range(len(agents))])


print(data)

for a in agents:
	print('plotting '+a)
	sns.distplot(data[data.agent==a]["truefitness"], label=a) #or distplot

plt.legend()
plt.savefig('ntbea_data.pdf')
