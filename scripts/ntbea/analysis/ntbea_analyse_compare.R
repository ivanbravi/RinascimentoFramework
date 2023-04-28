library(ggplot2)
library(reshape2)
library(dplyr)

### Section LOAD DATA #############################
data_folder = "../../../Data/NTBEA - COG20/"
setwd(data_folder)

folders <- c( 
              "RHEA+PT/100/",
              "RHEA+PT/1000/",
              "RHEA+PT/10000/",
              "RHEA+PT/100000/",
              "RHEA+PT/500000/",
              
              "RHEA+EH-5 features/linear/100/",
              "RHEA+EH-5 features/linear/1000/",
              "RHEA+EH-5 features/linear/10000/",
              "RHEA+EH-5 features/linear/100000/",
              "RHEA+EH-5 features/linear/500000/",

              "RHEA+EH-5 features/poly,2/100/",
              "RHEA+EH-5 features/poly,2/1000/",
              "RHEA+EH-5 features/poly,2/10000/",
              "RHEA+EH-5 features/poly,2/100000/",
              "RHEA+EH-5 features/poly,2/500000/",
              
              "RHEA+EH-18 features/linear/100/",
              "RHEA+EH-18 features/linear/1000/",
              "RHEA+EH-18 features/linear/10000/",
              "RHEA+EH-18 features/linear/100000/",
              "RHEA+EH-18 features/linear/500000/",
              
              "RHEA+EH-18 features/poly,2/100/",
              "RHEA+EH-18 features/poly,2/1000/",
              "RHEA+EH-18 features/poly,2/10000/",
              "RHEA+EH-18 features/poly,2/100000/",
              "RHEA+EH-18 features/poly,2/500000/",
                
              "SB/SSPP/lin/100/",
              "SB/SSPP/lin/1000/",
              "SB/SSPP/lin/10000/",
              "SB/SSPP/lin/100000/",
              "SB/SSPP/lin/500000/",
              
              "SB/SSPP/pdnn1(1N)/100/",
              "SB/SSPP/pdnn1(1N)/1000/",
              "SB/SSPP/pdnn1(1N)/10000/",
              "SB/SSPP/pdnn1(1N)/100000/",
              "SB/SSPP/pdnn1(1N)/500000/",
              
              "SB/SSPP/pdnn5(1N)/100/",
              "SB/SSPP/pdnn5(1N)/1000/",
              "SB/SSPP/pdnn5(1N)/10000/",
              "SB/SSPP/pdnn5(1N)/100000/",
              "SB/SSPP/pdnn5(1N)/500000/",
              
              "SB/SSPP/pdnn10(1N)/100/",
              "SB/SSPP/pdnn10(1N)/1000/",
              "SB/SSPP/pdnn10(1N)/10000/",
              "SB/SSPP/pdnn10(1N)/100000/",
              "SB/SSPP/pdnn10(1N)/500000/",
              
              "SB/SSPP/pdnn1/100/",
              "SB/SSPP/pdnn1/1000/",
              "SB/SSPP/pdnn1/10000/",
              "SB/SSPP/pdnn1/100000/",
              "SB/SSPP/pdnn1/500000/",
              
              "SB/SSPP/pdnn5/100/",
              "SB/SSPP/pdnn5/1000/",
              "SB/SSPP/pdnn5/10000/",
              "SB/SSPP/pdnn5/100000/",
              
              "SB/SSPP/pdnn10/100/",
              "SB/SSPP/pdnn10/1000/",
              "SB/SSPP/pdnn10/10000/"
);

agents <- c(
            "PB",
            "PB",
            "PB",
            "PB",
            "PB",
            
            "EF-hc-lin",
            "EF-hc-lin",
            "EF-hc-lin",
            "EF-hc-lin",
            "EF-hc-lin",
            
            "EF-hc-poly,2",
            "EF-hc-poly,2",
            "EF-hc-poly,2",
            "EF-hc-poly,2",
            "EF-hc-poly,2",
            
            "EF-id-lin",
            "EF-id-lin",
            "EF-id-lin",
            "EF-id-lin",
            "EF-id-lin",
            "EF-id-lin",
            
            "EF-id-poly,2",
            "EF-id-poly,2",
            "EF-id-poly,2",
            "EF-id-poly,2",
            "EF-id-poly,2",
  
            "SSPP-linear",
            "SSPP-linear",
            "SSPP-linear",
            "SSPP-linear",
            
            "SSPP-pann1(1N)",
            "SSPP-pann1(1N)",
            "SSPP-pann1(1N)",
            "SSPP-pann1(1N)",
            "SSPP-pann1(1N)",
            
            "SSPP-pann5(1N)",
            "SSPP-pann5(1N)",
            "SSPP-pann5(1N)",
            "SSPP-pann5(1N)",
            "SSPP-pann5(1N)",
            
            "SSPP-pann10(1N)",
            "SSPP-pann10(1N)",
            "SSPP-pann10(1N)",
            "SSPP-pann10(1N)",
            "SSPP-pann10(1N)",
            
            "SSPP-pann1",
            "SSPP-pann1",
            "SSPP-pann1",
            "SSPP-pann1",
            "SSPP-pann1",
            
            "SSPP-pann5",
            "SSPP-pann5",
            "SSPP-pann5",
            "SSPP-pann5",
            
            "SSPP-pann10",
            "SSPP-pann10",
            "SSPP-pann10"
);

budgets <- c(
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
  
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              "500000",
              
              "100",
              "1000",
              "10000",
              "100000",
              
              "100",
              "1000",
              "10000"
);

stderr <- function(x) sd(x)/sqrt(length(x))

load_ntbea_results <- function(folder){
  
  dataCount <- 100;
  acc <- data.frame();
  allParamSets<-matrix(nrow = dataCount, ncol=1);
  
  for(id in 1:dataCount){
    dataFile <- paste(folder,"r",id,".csv", sep = "")
    print(dataFile)
    data <- read.csv(dataFile, header = TRUE);
    data <- cbind(data,id)
    acc <- rbind(acc,data)
  }
  
  return(acc)
}

df <- data.frame()

for(f in 1:length(folders)){
  print("loads…")
  curr_data <- load_ntbea_results(folders[f]);
  data_count <- nrow(curr_data);
  f_data <- data.frame(agent=agents[f],
                       budget=budgets[f],
                       fitness=curr_data$truefitness,
                       id=curr_data$id);
  df <- rbind.data.frame(df,f_data)
}

print("plots")

### Section PLOTS ############################# 
# cmd+alt+t
pc = c(
  rgb(166, 206, 227, maxColorValue = 255),  # 1 blu
  rgb( 31, 120, 180, maxColorValue = 255),
  
  rgb(178, 223, 138, maxColorValue = 255),  # 3 verde
  rgb( 51, 160,  44, maxColorValue = 255),
  
  rgb(251, 154, 153, maxColorValue = 255),  # 5 rosso
  rgb(227,  26,  28, maxColorValue = 255),
  
  rgb(253, 191, 111, maxColorValue = 255),  # 7 arancio
  rgb(253, 191, 111, maxColorValue = 255),
  
  rgb(202, 178, 214, maxColorValue = 255),  # 9 viola
  rgb(106,  61, 154, maxColorValue = 255),
  
  rgb(255, 255, 153, maxColorValue = 255),  # 11 giallo
  rgb(177,  89,  40, maxColorValue = 255)
)

pc2 = c(
  rgb(146,205,155, maxColorValue = 255),  # 1 green
  rgb(60,180,74, maxColorValue = 255),
  
  rgb(245,153,154, maxColorValue = 255),  # 3 red
  rgb(236,28,36, maxColorValue = 255),
  
  rgb(201,189,221, maxColorValue = 255),  # 5 purple
  rgb(116,88,164, maxColorValue = 255),
  
  rgb(224,184,153, maxColorValue = 255),  # 7 brown
  rgb(163,71,35, maxColorValue = 255),
  
  rgb(238,179,209, maxColorValue = 255),  # 9 lilac
  rgb(218,95,162, maxColorValue = 255),
  
  rgb(206,206,206, maxColorValue = 255),  # 11 grey
  rgb(161,161,161, maxColorValue = 255),
  
  rgb(255,246,164, maxColorValue = 255),  # 13 yellow
  rgb(253,188,17, maxColorValue = 255),
  
  rgb(184,226,229, maxColorValue = 255),  # 15 azure
  rgb(44,194,231, maxColorValue = 255),
  
  rgb(154,202,236, maxColorValue = 255),  # 17 blu
  rgb(34,101,175, maxColorValue = 255),
  
  rgb(249,173,128, maxColorValue = 255),  # 19 orange
  rgb(243,119,133, maxColorValue = 255)
)


clrs = c(
         pc2[5+1],                                       # EF linear hc
         pc2[5],                                     # EF poly-2 hc
         pc2[9+1],                                       # EF linear id
         pc2[9],                                     # EF poly-2 id
         rgb(255, 21, 82, maxColorValue = 255),       # pb
         pc2[1+1],                                      # SF linear
         pc2[15+1],                                       # SF nn 1 12N
         pc2[15],                                     # SF nn 1 1N
         pc2[17+1],                                       # SF nn 10 12N
         pc2[17],                                     # SF nn 10 1N
         pc2[19+1],                                       # SF nn 5 12N
         pc2[19]                                     # SF nn 5 1N
)

p<-ggplot(df, aes(x=budget,y=fitness, fill=agent)) + geom_boxplot() +
  ylim(0,0.65) + theme(legend.position="bottom") + scale_fill_manual(values=clrs) +
  geom_hline(yintercept=0.5, linetype="dashed", color = "red") +
  ggtitle("Agent tuning with NTBEA")
ggsave("ntbea_double_fill.pdf", p, width = 40, height = 20, device = "pdf",units = c("cm"))

p<-ggplot(df, aes(x=budget,y=fitness, color=agent)) + geom_boxplot() +
  ylim(0,0.65) + scale_colour_manual(values=clrs) +
  theme(legend.position="bottom") +
  geom_hline(yintercept=0.5, linetype="dashed", color = "red")+
  ggtitle("Agent tuning with NTBEA")
ggsave("ntbea_double.pdf", p, width = 40, height = 20, device = "pdf",units = c("cm"))

### Section CSV #############################

best <- data.frame()
for(b in unique(budgets)){
  for(a in unique(agents)){
    aData <- df[df$agent==a & df$budget==b,]
    el <- aData[aData$fitness==max(aData$fitness),]
    best <- rbind(best,el)
  }
}
write.csv(best,"best.csv", row.names = FALSE)

print("fatto")


