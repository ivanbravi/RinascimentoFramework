#!/bin/bash
#$ -pe smp 14
#$ -l h_vmem=2G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -m bea
#$ -M ivan.bravi.hpc@gmail.com
#$ -N GS-

module load java/17.0.0

echo "Batch job is processing." 

java -Xmx20000m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar GameSearch.jar gs_config.json

zip -q -r log.zip logs/


