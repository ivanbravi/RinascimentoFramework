#!/bin/bash
#$ -pe smp 14
#$ -l h_vmem=2G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -m bea
#$ -M ivan.bravi.hpc@gmail.com
#$ -N N-500

module load java/17.0.0

echo "Job [START]" 

java -Xmx20000m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar Neighbourhood.jar sampling_config.json

echo "Job [END]" 

zip -q -r log.zip logs/


