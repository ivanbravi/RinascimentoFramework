library("rjson")
library("ggplot2")
library("reshape2")

folder <- "";
data <- fromJSON(file=paste0(folder,"results.json"));

players <- names(data$playersStats);
playerCount <- length(players);
match <- paste(players, collapse = " vs ");

statsList <- names(data$playersStats[[1]]$stats);

for(s in 1:length(statsList)){
  
  df <- data.frame(player=character(),
                   data=double(),
                   stringsAsFactors = FALSE);
  
  statToShow <- statsList[s];
  
  for(i in 1:playerCount){
    values <- data$playersStats[[i]]$stats[[statToShow]]$history;
    playerData <- data.frame(player=rep(players[i],length(values)), data=values, stringsAsFactors = FALSE);
    df <- rbind(df, playerData);
  }
  
  plot <- ggplot(df, aes(x=data, color=player,fill=player))+
    geom_histogram(binwidth = 3, alpha=0.4, position="identity")+
    xlab(statToShow)+ylab("count")+ggtitle(match);
  ggsave(paste0(statToShow,".pdf"), plot, width = 30, height = 30,device = "pdf",units = c("cm"));
}