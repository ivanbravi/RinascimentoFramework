library("rjson")
library("ggplot2")
library("reshape2")


data <- fromJSON(file=paste0(folder,"tournament results-list.json"));
playerNames <- fromJSON(file=paste0(folder,"player names.json"));
#playerNames <- c("EBH-id-RHEA-poly,3_11");
players <- length(playerNames);

filterPlayerStats <- function(data, player){
  df <- data.frame();
  statsToSkip <- c("win ratio", "Result");
  playersToSkip <- c("OneStepLookAhead_1",
                     "RHEA0_2",
                     "MCTS0_3",
                     "SeededRHEA0_4",
                     "RHEA1_5",
                     #"EBH-simple-RHEA-linear_6",
                     #"EBH-simple-RHEA-poly,2_7",
                     #"EBH-simple-RHEA-poly,3_8",
                     #"EBH-id-RHEA-linear_9",
                     #"EBH-id-RHEA-poly,2_10",
                     #"EBH-id-RHEA-poly,3_11"
                     "SafeRandomPlayer_0");
  for(i in 1:length(data)){
    pNames <-names(data[[i]]$playersStats);
    pIndex <- match(player,pNames, nomatch = 0);
    if(pIndex!=0){
      pData <- data[[i]]$playersStats[[pIndex]];
      opp <- paste(pNames[-pIndex], collapse = " ");
      if(!is.element(opp, playersToSkip)){
        for(sIndex in 1:length(pData$stats)){
          sName <- names(pData$stats[sIndex]);
          if(!is.element(sName,statsToSkip)){
            s <- pData$stats[[sIndex]];
            vCount <- length(s$history);
            pStats <- data.frame(opponent=rep(opp,vCount), stat=rep(sName,vCount), data=s$history, stringsAsFactors = FALSE);
            df <- rbind(df, pStats);
          }
        }
      }
    }
  }
  return(df);
}
  
for(player in playerNames){
  
  stats <- filterPlayerStats(data, player);
  statsList <- unique(stats$stat);
  playerDir <- paste0("plots/",player,"/");
  dir.create(playerDir, recursive = TRUE, showWarnings = FALSE)
  
  for(statToShow in statsList){
    
    stat <- stats[stats$stat==statToShow,]
    plot <- ggplot(stat, aes(x=data, color=opponent,fill=opponent))+
      geom_histogram(binwidth = 1, alpha=0.1, position="identity")+
      xlab(statToShow)+ylab("count")+ggtitle(player);
    filePath <- paste0(playerDir,statToShow,".pdf");
    ggsave(filePath, plot, width = 30, height = 30,device = "pdf",units = c("cm"));
  }
}






