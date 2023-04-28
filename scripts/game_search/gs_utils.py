import matplotlib.pyplot as plt
import json, os
from numpy import sqrt
from scipy.ndimage import uniform_filter1d
from operator import mul
import warnings

def fitness(in_folder):
	with open(in_folder+"fitness.json", 'r') as f:
		f_data = json.load(f)
	return f_data["avg"]

def evaluations(in_folder):
	with open(in_folder+"FitnessTrend.json", 'r') as f:
		data = json.load(f)
	return [e["v2"] for e in data]

def solution(in_folder):
	with open(in_folder+"solution.json", 'r') as f:
		solution = json.load(f)
	return solution
	
def space(in_folder):
	with open(in_folder+"ActualGameSpace.json", 'r') as f:
		space = json.load(f)
	return space

def space_index_to_values(space, index):
	return space["params"][index]["a"]

def space_index_to_name(space, index):
	return space["params"][index]["name"]

def sliding_mean(values, size):
	return uniform_filter1d(values, size=size, mode="nearest", origin=size//2-1)

def sliding_stderr(values, size):
	return sliding_stddev(values, size) / sqrt(size)	

def sliding_stddev(values, size):
	warnings.filterwarnings("ignore")
	mean = sliding_mean(values,size)
	mean_of_squares =  sliding_mean(list(map(mul, values, values)), size)
	variance = mean_of_squares - list(map(mul,mean,mean))
	return sqrt(variance)

def cmp_thus_far(values, cmp):
    v = values[0]
    a = []
    for e in values:
        if cmp(v,e) != v:
            v = e
        a.append(v)
    return a

def max_thus_far(values):
    return cmp_thus_far(values, max)

def min_thus_far(values):
    return cmp_thus_far(values, min)