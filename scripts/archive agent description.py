import json

path = "../agents/archive.json"
archive = json.load(open(path))
i = 0

short = True

for p in archive["agents"]:
	if "name" in p:
		print(f"{i}\t{p['name']} [{p['type']}]")
	else:
		print(f"{i}\t{p['type']}")
	if not short :
		if "comment" in p:
			print("\t"+p["comment"])
		if "params" in p:
			print("\t"+str(len(p["params"])))
		print("")
	i = i+1
