#!/bin/bash
#$ -pe smp 10
#$ -l h_vmem=2G
#$ -l h_rt=0:59:0
#$ -cwd
#$ -j y
#$ -N play
#$ -m be
#$ -M ivan.bravi.hpc@gmail.com

module purge
module load java/11.0.2

gameVersion=assets/defaultx2/
configPath=batch-journal-all/
nGames=10
threads=8
isLoggingActive=true
statsType=metrics

echo "START: $(date)"

java -Xmx8900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar play.jar $gameVersion $configPath $nGames $threads $isLoggingActive $statsType

echo "END: $(date)"

echo 'EOF'
