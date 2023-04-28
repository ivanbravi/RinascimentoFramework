from analyse_neighbourhood_fitness import analyse as fitness_analyse
from analyse_neighbourhood_sampling import analyse as sampling_analyse
from analyse_eval_data import analyse as data_analyse
from analyse_eval_log import analyse as log_analyse

experiment_id = "NEIGHBOURHOOD - 22-09-20-18-08-45 (expanding - long)/"
root = "/Users/ivanbravi/Desktop/neighbourhood/_results/"
# root = "/Users/ivanbravi/Desktop/GAME SEARCH/_results/"

in_path = root+experiment_id;

fitness_analyse(in_path)
sampling_analyse(in_path)
data_analyse(in_path)
log_analyse(in_path)