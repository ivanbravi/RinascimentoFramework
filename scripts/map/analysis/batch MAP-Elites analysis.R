source("~/git/Rinascimento/scripts/MAP-Elites viz.R")

#runs <- c("", "")
runs <- list.dirs(path = ".", full.names = FALSE, recursive = FALSE)

for(run in runs){
  analyseMAPEliteRun(paste0(run,"/data/"))
}