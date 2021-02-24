library("rjson")
library("ggplot2")
library("reshape2")

folder <- "";
data <- fromJSON(file=paste0(folder,"tournament results-list.json"));
playerNames <- fromJSON(file=paste0(folder,"player names.json"));
players <- length(playerNames);

playerIndex <- function(name){
  for(i in 1:length(playerNames)){
    if(startsWith(name,playerNames[i]))
      return(i)
  }
  return (0)
}

winRate <- matrix(data=NA, nrow=players, ncol = players)
draw <- matrix(data=NA, nrow=players, ncol = players)

#for(p1 in 1:players){
#  for(p2 in p1:players){
for(i in 1:length(data)){
    tData <- data[[i]]$playersStats;
#    tData <- data[[p1]][[p2]]$playersStats;
    
    p1Id <- playerIndex(names(tData)[1]);
    p2Id <- playerIndex(names(tData)[2]);
    
    p1WR <- mean(tData[[1]]$stats$`win ratio`$history);
    p2WR <- mean(tData[[2]]$stats$`win ratio`$history);
    
    if(p1Id!=p2Id){
      winRate[p1Id,p2Id] <- p1WR
      winRate[p2Id,p1Id] <- p2WR
    }else{
      winRate[p2Id,p1Id] <- (p1WR+p2WR)/2
    }
    draw[p1Id,p2Id] <- 1-(p1WR+p2WR);
    draw[p2Id,p1Id] <-draw[p1Id,p2Id];
    print(paste(p1Id,p2Id,winRate[p1Id,p2Id],winRate[p2Id,p1Id],draw[p1Id,p2Id],sep=" "));
  }
#}

write.csv(winRate,"winrate.csv",)
write.csv(draw,"draw.csv")
