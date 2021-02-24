import os

agent = "SVB"
agent_code = ("SVB-SSPP-RHEA-nn,TANH,192,(5184),30,1,[${SEED}]-11",
	"SVB-SSPP-RHEA-nn,TANH,192,(5472),30,1,[${SEED}]-11",
	"SVB-SSPP-RHEA-nn,TANH,192,(5702),30,1,[${SEED}]-11")
funs = ("pdnn10","pdnn5","pdnn1")
budgets = (100,1000,10000,100000)
memory = (4, 6, 8, 10)
b_texts = ("100","1K","10K","100K")
times = ("00:59:00", "240:00:00")


for f in range(len(agent_code)):
	for b in range(len(budgets)):
		if budgets[b] <= 100:
			time = times[0]
		else:
			time = times[1]

		dir = './'+agent+'/'+funs[f]+'/'+str(budgets[b])

		os.makedirs(dir, exist_ok=True)
		os.makedirs(dir+"/results/", exist_ok=True)


		job_txt=('#!/bin/bash',
			'#$ -pe smp 1',
			'#$ -l h_vmem='+str(memory[b])+'G',
			'#$ -l h_rt='+time,
			'#$ -cwd',
			'#$ -j y',
			'#$ -t 1-100',
			'#$ -tc 100',
			'#$ -N N'+funs[f]+'.'+b_texts[b]+'.'+agent,
			'#$ -m be',
			'#$ -M ivan.bravi.hpc@gmail.com',
			'module purge',
			'module load java/11.0.2',
			' ',
			'TID=$SGE_TASK_ID',
			'SEED=$((TID % 10))',
			'BUDGET=1000',
			' ',
			'mkdir $TID',
			'cd $TID',
			'cp -r /data/scratch/acw383/ntbea/_files/* .',
			' ',
			'echo "{\n \\"agent/type\\": \\"'+agent_code[f]+'\\",\n \\"agent/space\\": \\"id\\",\n \\"game/opponents\\": \\"agents/opponents.json\\",',
			' \\"game/version\\": \\"assets/defaultx2/\\",\n \\"game/simbudget\\": 1000,\n \\"ntbea/1Tuple\\": true,\n \\"ntbea/2Tuple\\": true,',
			' \\"ntbea/3Tuple\\": false,\n \\"ntbea/nTuple\\": true,\n \\"ntbea/k\\": 1,\n \\"ntbea/e\\": 0.7,',
			' \\"ntbea/budget\\": '+str(budgets[b])+',\n \\"ntbea/truefitnessbudget\\": 1000,\n \\"ntbea/printreport\\": false,\n \\"ntbea/result\\": \\"r\\"\n}" >> ntbea_setup.sh',
			' ',
			'java -Xmx'+str(memory[b]*1000-100)+'m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar ntbea.jar ntbea_setup.sh $TID',
			'mv ./*.csv ../results/',
			' ')

		with open(dir+'/job.sh','w') as file:
			file.write("\n".join(job_txt))
		
