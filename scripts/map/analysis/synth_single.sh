#!/bin/bash


# IBFOLDER=~/"Desktop/Simulazioni/mapelites/data/PB - 10000/frames/"
# ./synth_single.sh "$IBFOLDER" 1000 2560 1920

FRAMEFOLDER="$1"
FRAME=$2

if [ "$#" -eq 3 ]; then
    WIDTH=$3
else
	WIDTH=1280
fi

HEIGHT=$((WIDTH*1952/1017))

cd "$FRAMEFOLDER"

FRAMENAME=${FRAME}_single
ansifilter -i $FRAME.txt -o $FRAMENAME.html --html --art-cp437 --art-width 200 --art-height 200  > /dev/null
webkit2png $FRAMENAME.html -o $FRAMENAME -F > /dev/null
convert ${FRAMENAME}-full.png -resize ${WIDTH}x${HEIGHT}\! ${FRAMENAME}-full.png
rm $FRAMENAME.html

# for collating several pictures together
# pictures to be named in*.png
# output file will be named out[+/-].png
# horizontally:
# convert +append in*.png out+.png
# vertically:
# convert -append in*.png out-.png