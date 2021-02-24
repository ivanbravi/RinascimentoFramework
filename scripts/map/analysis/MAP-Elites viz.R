#map-elites
library("rjson")
library("ggplot2")
library("reshape2")

plotGridData <- function(data){
  #clrs <- rainbow(5)
  clrs <- c("blue", "cyan", "green", "yellow", "red")
  clrVls <- seq(from = 0, to = 1, by=1.0/(5-1))
  df <- melt(data)
  p <- ggplot(data = df, aes(x=factor(Var1),y=factor(Var2),fill=value))+
    geom_tile()+
    scale_fill_gradientn(colours = clrs, values = clrVls, na.value = 'gray85')
  return(p)
}

projectData <- function(data, index1, index2, size1, size2){
  m <- matrix(NA, ncol = size1, nrow = size2)
  for(i in 1:length(data)){
    point <- data[[i]][[1]]$value
    x <- point[index1]+1
    y <- point[index2]+1
    fitness <- data[[i]][[2]]$fitness
    if(is.na(m[x,y]) || m[x,y]<fitness){
      m[x,y] = fitness
    }
  }
  return(m)
}

compileLabels <- function(labs){
  l_count <- length(labs)+1
  v <- c("-inf",labs,"inf")
  l <- rep("",l_count)
  for(i in 1:(l_count)){
    l[i] <- paste0("(",v[i],",",v[i+1],")")
  }
  return(l)
}

analyseMAPEliteRun <- function(folder){
  space <- fromJSON(file=paste0(folder,"space.json"))
  d <- fromJSON(file=paste0(folder,"spaceSize.json"))
  b <- fromJSON(file=paste0(folder,"behaviours.json"))
  bins <- fromJSON(file=paste0(folder,"bins.json"))
  
  projections <- combn(length(b),2)
  
  folder <- paste0(folder,"pics/")
  dir.create(folder)
  
  for(i in 1:ncol(projections)){
    d1 <- projections[1,i]
    d2 <- projections[2,i]
    data <- projectData(space,d1,d2,d[d1],d[d2])
    projName <-paste0("[",(d1-1),",",(d2-1),"]")
    
    plot <- plotGridData(data)
    plot <- plot + xlab(b[d1])+ylab(b[d2])+ggtitle(projName)
    plot <- plot + scale_x_discrete(expand=c(0,0), breaks=1:(d[d1]),labels=compileLabels(round(bins[[d1]]$limits, 2)))
    plot <- plot + scale_y_discrete(expand=c(0,0), breaks=1:(d[d2]),labels=compileLabels(round(bins[[d2]]$limits, 2)))
    plot <- plot + theme(axis.text.x = element_text(angle=90))
    
    filename <- paste0(folder,projName,"-[",b[d1],",",b[d2],"].pdf")
    ggsave(filename, plot, width = 30, height = 30,device = "pdf",units = c("cm"))
  }
}
