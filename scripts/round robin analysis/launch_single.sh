#!/bin/bash
#$ -pe smp 10
#$ -l h_vmem=2G
#$ -l h_rt=240:0:0
#$ -cwd
#$ -j y
#$ -N rr-serial
#$ -m be
#$ -M ivan.bravi.hpc@gmail.com

module purge
module load java/11.0.2


game=assets/defaultx2/
games=10000
agents=agents/archive.json
threads=8
range=111111111111

echo "Batch job is processing."

java -Xmx5900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar roundrobin.jar $agents $game $games $threads $range $range

echo 'EOF'

