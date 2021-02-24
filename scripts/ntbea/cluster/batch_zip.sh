#!/bin/bash

folders=("lin/100" "lin/1000" "lin/10000" "lin/100000" "pdnn/100" "pdnn/1000")
path=${SCRATCH}/ntbea/SVB/
ORPWD=$PWD

for f in "${folders[@]}"; do
		path_to_f="${path}${f}"
		type=$(echo "${f}" | tr  '\/' '-')
		file=$SCRATCH/$type.zip
	
		#echo $path_to_f
		#echo $file

		cd $path_to_f
		zip -q -r $file r*
done

cd $ORPWD
