#!/bin/bash

folders=("pdnn10/10000" "pdnn10/100000" "pdnn5/100" "pdnn5/1000" "pdnn5/10000" "pdnn5/100000")
path=${SCRATCH}/ntbea/SVB/
ORPWD=$PWD

for f in "${folders[@]}"; do
	path_to_f="${path}${f}"
	cd $path_to_f
	qsub job.sh
done

cd $ORPWD
