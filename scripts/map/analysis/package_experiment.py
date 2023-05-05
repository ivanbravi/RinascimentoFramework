import csv 
import json 
import sys
import os

def main():

	# USAGE
	
	# [0]	(Auto) 	Script file name
	# [1] 	(!)		Path
	# [2] 	(?)		Name
	# [3] 	(?)		Out path

	# eg: python3.7 package_experiment.py "${DATA}/SP2P/vs\ RND/pb/" "SP2P-RND-pb" "$(OUT)/packaged"

	if(len(sys.argv)<2):
		print("Please, provide at least the path to the files")
		exit()
	
	path = sys.argv[1]
	name = sys.argv[2] if (len(sys.argv)>=3) else "package"
	out_path = sys.argv[3] if (len(sys.argv)>=4) else path

	if(not path.endswith("/")):
		path = path+"/"

	if(not out_path.endswith("/")):
		out_path = out_path+"/"

	culo(path, out_path)
	# process(path, name, out_path)
	# hyper_process(path)

def culo(path,out_path):
	games = ["SP2P","1C2W","W2"]
	opponents = ["RND", "RHEA"]
	agents = ["eb-linear-id","eb-linear-simple","sb-linear","pb"]
	agents_short = ["id","hc","sf","pb"]
	for agent in range(len(agents)):
		for game in games:
			for opponent in opponents:
				ccpath = path+game+"/vs "+opponent+"/"+agents[agent]+"/"
				name = game+"-"+opponent+"-"+agents_short[agent]
				process(ccpath, name, out_path)

def axes_process(path):
	d = json.load(open(path+"bins.json"))
	x = [[d[i]["limits"][j] for j in (0,-1)] for i in range(len(d))]
	return(x)

def hyper_process(path):
	da_path = path+"agentSpace.json"	
	da = json.load(open(da_path))
	return {v["name"]:v["a"] for v in da["ass"]["params"]}

def process(path, name, out_path):
	space_dic = csvToDic(path+"space.csv")
	fields = space_dic[0].keys()
	exp = json.load(open(path+"p.json"))
	exp_keep = ["search", "game", exp["search/type"]]
	out_file = out_path+name+".json"

	space_package = {
		"name" : name,
		"data" : space_dic,
		"experiment" : {key:value for (key,value) in exp.items() if any([key.startswith(b) for b in exp_keep])},
		"fields" : [field for field in fields if field.startswith("Behave_D")],
		"behaviours" : json.load(open(path+"behaviours.json")),
		"axes" : axes_process(path),
		"hyper": hyper_process(path),
		"size" : json.load(open(path+"spaceSize.json"))
	}

	with open(out_file,"w") as out_json:
		json.dump(space_package, out_json, indent=4)
	

def csvToDic(csvFilePath): 
	data = []
	index = 0
	with open(csvFilePath, encoding='utf-8') as csvf: 
		csvReader = csv.DictReader(csvf) 
		for rows in csvReader:
			data.append(rows)
	return(data)

if __name__ == "__main__":
	main()