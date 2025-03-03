import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import csv 
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

distance = []
log_entropy = []

# rename the file name to the latest csv output
with open('Entropy_output2025-02-27_20-21-05_510.csv', 'r') as f:
    reader = csv.reader(f)
    next(reader)

    for line in reader:
        distance.append(float(line[0]))
        log_entropy.append(float(line[1]))

dist_array = np.array(distance) 
log_entropy_array = np.array(log_entropy)  

polynimial = PolynomialFeatures(degree= 4) 
Xpoly = polynimial.fit_transform(dist_array, log_entropy_array) 
polynimial.fit(Xpoly) 

linReg = LinearRegression() 
model = linReg.fit(Xpoly, log_entropy_array)  

print('Regression coefficients: ', model.coef_)
print('Intercept: ', model.intercept_)


