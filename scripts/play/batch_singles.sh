#!/bin/bash
#$ -pe smp 10
#$ -l h_vmem=2G
#$ -l h_rt=1:0:0
#$ -cwd
#$ -j y
#$ -t 0-12
#$ -tc 13
#$ -N play
#$ -m bea
#$ -M ivan.bravi.hpc@gmail.com

module purge
module load java/11.0.2

a=(10 13 15 17 12 14 16 18 7 5 2 6 9)

gameVersion=assets/defaultx2/
configPath="batch-journal-all/${a[$i]}/"
nGames=10
threads=8
isLoggingActive=true
statsType=metrics

echo "START: $(date)"

${a[$i]}

java -Xmx8900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar play.jar $gameVersion $configPath $nGames $threads $isLoggingActive $statsType

echo "END: $(date)"

echo 'EOF'
