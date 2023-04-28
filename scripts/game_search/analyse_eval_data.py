import matplotlib.pyplot as plt
import json
import os

# # ---------------INPUT HERE---------------
# folder = "GS - 22-09-09-14-10-28 - EFDB vs RND/"
# root = "/Users/ivanbravi/git/Rinascimento/logs/"
# # ----------------------------------------
# in_folder = root+folder
# analyse(in_folder)

def analyse(in_folder, out_folder=None):
	if out_folder is None:
		out_folder=in_folder+"analysis/"
	out_folder = out_folder+"eval_data/"
	os.makedirs(out_folder, exist_ok = True)

	count = 1
	while os.path.exists(in_folder+'evalData['+str(count)+'].json'):
		count += 1

	plt.clf()
	
	for i in range(1,count):
		file = "evalData["+str(i)+"].json"
		with open(in_folder+file, 'r') as f:
			data = json.load(f)

		fields = ["CardCount", "TotalCoins"]
		ranges = [[0,40],[0,120]]
		breaks = 22

		xTicks = [i*(ranges[0][1]-ranges[0][0])/breaks for i in range(0,breaks+1)]
		yTicks = [i*(ranges[1][1]-ranges[1][0])/breaks for i in range(0,breaks+1)]

		x = []
		y = []

		for key in data.keys():
			x.append(data[key]["stats"][fields[0]]["avg"])
			y.append(data[key]["stats"][fields[1]]["avg"])


		plt.scatter(x,y, s=1)
		plt.xticks(xTicks)
		plt.yticks(yTicks)
		plt.xlim(ranges[0])
		plt.ylim(ranges[1])
		plt.grid()
		plt.savefig(out_folder+"evalData["+str(i)+"]_scatter.pdf")
		plt.close()
		plt.clf()