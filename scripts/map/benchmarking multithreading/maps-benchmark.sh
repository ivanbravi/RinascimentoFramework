threads=15
init=50
budget=100
agent=EB
# search parameters (*game* played by the agent against the opponent with *opponentid* from the file agents/agents.json for *samples* times)
game=assets/defaultx2/
opponentid=0
samples=30
# agent hyper parameter
agenthp=agents/RHEAParams.json
# agent heuristic parameters
heuristic=nn,TANH,5,15,1
converter=simple

function test {
	threads=$1
	samples=$2
	echo "$threads threads - $samples samples - $budget budget"
	start=$SECONDS
	for (( i = 0; i < 3; i++ )); do
		java -Xmx2900m -XX:+UseSerialGC -XX:+DisableAttachMechanism -XX:+ReduceSignalUsage -jar mapelites.jar $threads $init $budget $agent $game $opponentid $samples $agenthp $heuristic $converter > /dev/null
	done
	elapsed=$(($SECONDS-$start))
	echo "time: $elapsed s"
}


# - - - - - - - - - RUN - - - - - - - - -
for t in 1 2 3 4 5; do
	for s in 5 10 20 30 40 50 60; do #10 20 30 40 50 60
		test $t $s	
	done
done
# - - - - - - - - - - - - - - - - - - - -