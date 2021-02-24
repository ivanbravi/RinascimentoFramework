#!/bin/bash

# usage
[ $# -eq 0 ] && { 
	echo "Usage: $0 frames_folder max_frame_id [video_width video_height]"
	echo "frames_folder : location where the frames are stored"
	echo "max_frame_id  : frames should be named \"ID.txt\" with ID in the range [1,max_frame_id]"
	echo "video_width   : optional width of the video, the height is automatically computed."
	exit 1
}

# flags
RMINTERMEDIATE=1

# script
FRAMEFOLDER=$1
FRAMES=$2

if [ "$#" -eq 3 ]; then
    WIDTH=$3
else
	WIDTH=1280
fi

HEIGHT=$((WIDTH*1952/1017))

# 3 column graphs -> 200
# 4 column graphs -> 270
ARTWIDTH=270
# big number, height won't really change a thing as the .txt will 
ARTHEIGHT=700

# 
TIMEOUT=120

cd "$FRAMEFOLDER"

for FRAME in $(seq 1 $FRAMES)
do
	echo "processing frame $FRAME"
   	if [[ -f "$FRAME.png" ]]; then
    	echo "Frame $FRAME exists"
	else
		ansifilter -i $FRAME.txt -o $FRAME.html --html --art-cp437 --art-width $ARTWIDTH --art-height $ARTHEIGHT  > /dev/null
		webkit2png $FRAME.html -o $FRAME -F --timeout=$TIMEOUT > /dev/null
		convert $FRAME-full.png -resize ${WIDTH}x${HEIGHT}\! $FRAME-full.png
		mv $FRAME-full.png $FRAME.png
	fi
done

if [[ $RMINTERMEDIATE == 1 ]]; then
	for FRAME in $(seq 1 $FRAMES)
	do
		rm $FRAME.html
	done
fi

ffmpeg -y -r 10 -i %d.png -start_number 1 -vcodec libx264 -crf 25 -pix_fmt yuv420p -vf scale=$WIDTH:$HEIGHT -aspect:v $WIDTH:$HEIGHT console.mp4
ffmpeg -y -t 3 -i console.mp4 -vf "fps=10,scale=$WIDTH:$HEIGHT:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse" -loop 0 console.gif