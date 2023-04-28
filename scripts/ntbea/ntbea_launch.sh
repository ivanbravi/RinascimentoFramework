#!/bin/bash
#$ -pe smp 11
#$ -l h_vmem=2G
#$ -l h_rt=240:00:00
#$ -cwd
#$ -m bea
#$ -M ivan.bravi.hpc@gmail.com
#$ -N GameSearch

module load java/11.0.2

echo "Game Search Starting"

java -Xmx20000m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar GameSearch.jar gs.json

