#!/bin/bash
#$ -pe smp 10
#$ -l h_vmem=2G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -m bea
#$ -M ivan.bravi.hpc@gmail.com
#$ -N map-nn-5-15-1

module load java/11.0.2

echo "Batch job is processing." 

java -Xmx2900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar mapelites.jar p.json

zip -q -r frames.zip frames/
zip -q -r log.zip logs/


