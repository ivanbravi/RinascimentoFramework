#!/bin/bash
#$ -pe smp 1
#$ -l h_vmem=10G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -j y
#$ -t 1-100
#$ -tc 100
#$ -N Nlin.500K.SVB
#$ -m be
#$ -M ivan.bravi.hpc@gmail.com
module purge
module load java/11.0.2
 
TID=$SGE_TASK_ID
SEED=$((TID % 10))
BUDGET=1000
 
mkdir $TID
cd $TID
cp -r /data/scratch/acw383/ntbea/_files/* .
 
echo "{
 \"agent/type\": \"SVB-SSPP-RHEA-linear-11\",
 \"agent/space\": \"id\",
 \"game/opponents\": \"agents/opponents.json\",
 \"game/version\": \"assets/defaultx2/\",
 \"game/simbudget\": 1000,
 \"ntbea/1Tuple\": true,
 \"ntbea/2Tuple\": true,
 \"ntbea/3Tuple\": false,
 
\"ntbea/nTuple\": true,
 \"ntbea/k\": 1,
 \"ntbea/e\": 0.7,
 \"ntbea/budget\": 500000,
 \"ntbea/truefitnessbudget\": 1000,
 \"ntbea/printreport\": false,
 \"ntbea/result\": \"r\"
}" >> ntbea_setup.sh
 
java -Xmx9900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar ntbea.jar ntbea_setup.sh $TID
mv ./*.csv ../results/
 