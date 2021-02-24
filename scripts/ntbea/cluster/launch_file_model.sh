#!/bin/bash
#$ -pe smp 1
#$ -l h_vmem=2G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -j y
#$ -t 1-100
#$ -tc 100
#$ -N ntbeaSBlin100K
#$ -m be
#$ -M ivan.bravi.hpc@gmail.com

module purge
module load java/11.0.2

TID=$SGE_TASK_ID
SEED=$((TID % 10))
BUDGET=1000

mkdir $TID
cd $TID

cp -r ../_files/* .

AGENT="{
  \"agent/type\": \"EHB-id-RHEA-nn,TANH,18,(50),9,1,[${SEED}]-11\",
  \"agent/space\": \"id\",
  \"game/opponents\": \"agents/rndopp.json\",
  \"game/version\": \"assets/defaultx2/\",
  \"game/simbudget\": 1000,
  \"ntbea/1Tuple\": true,
  \"ntbea/2Tuple\": true,
  \"ntbea/3Tuple\": false,
  \"ntbea/nTuple\": true,
  \"ntbea/k\": 1,
  \"ntbea/e\": 0.7,
  \"ntbea/budget\": 1000,
  \"ntbea/truefitnessbudget\": 1000,
  \"ntbea/printreport\": false,
  \"ntbea/result\": \"r\"
}"

echo $AGENT >> ntbea_setup.sh

java -Xmx1900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar ntbea_setup.sh $TID


mv ./*.csv ../results/

