#!/bin/bash

folders=("dnn/100" "poly-2/100" "dnn/1000" "poly-2/1000" "dnn/10000" "poly-2/10000" "dnn/100000" "poly-2/100000")
path=${SCRATCH}/ntbea/SB/SSPP/

for f in "${folders[@]}"; do
                path_to_f="${path}${f}"
                env_path="${SCRATCH}/ntbea/SB/_env"

                #echo "$path_to_f"
                #echo "$env_path"/*

                cd "$path_to_f"
                cp -r "$env_path"/* .
done

