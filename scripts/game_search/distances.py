def chebyshev(a, b):
	v = [abs(a[i]-b[i]) for i in range(0,len(a))]
	return max(v)

def manhattan(a, b):
	v = [abs(a[i]-b[i]) for i in range(0,len(a))]
	return sum(v)