import time
import json
import sys
import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import multivariate_normal
import os

def print_data(x_range, y_range, mean, cov):
	x, y = np.mgrid[x_range[0]:x_range[1]:.01, y_range[0]:y_range[1]:.01]
	pos = np.dstack((x, y))
	rv = multivariate_normal(mean, cov)
	fig2 = plt.figure()
	ax2 = fig2.add_subplot(111)
	ax2.contourf(x, y, rv.pdf(pos),100)
	# ax2.set_aspect((x_range[1]-x_range[0])/(y_range[1]-y_range[0]))
	ax2.set_aspect(1)
	ax2.invert_yaxis()
	plt.show()

	for coord in np.concatenate(pos):
		print(coord)
	for v in np.concatenate(rv.pdf(pos)):
		print(v)

def unravel(data):
	for entry in data:
		print(str(entry['coordinates'])+" "+str(entry['performance']))

x_range = [0.0,2.0]
y_range = [0.0,2.0]
mean = [1.0,1.0]
cov  = 0.05385417
var_x = 0.10748209
var_y = 0.10560875

print_data(x_range, y_range,mean,[[var_x, cov], [cov, var_y]])

# path = sys.argv[1]
# print(path)
# dataset = json.load(open(path))

# unravel(dataset)


# mean = np.mean(data, axis=0)
# cov = np.cov(data, rowvar=0)


# mean =  [1.0, 2.0, 3.0, 4.0, 5.0]
# cov = [[1.1, 1.2, 1.3, 1.4, 1.5],
# 		[2.1, 2.2, 2.3, 2.4, 2.5],
# 		[3.1, 3.2, 3.3, 3.4, 3.5],
# 		[4.1, 4.2, 4.3, 4.4, 4.5],
# 		[5.1, 5.2, 5.3, 5.4, 5.5]]

# data = {
#     'mean' : mean,
#     'covariance' : cov,
# }


# json_string = json.dumps(data)