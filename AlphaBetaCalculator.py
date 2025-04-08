import numpy as np 
import scipy.optimize
import seaborn
import pandas as pd
import matplotlib.pyplot as plt 
import scipy
import csv  
from sklearn.linear_model import LinearRegression 
from sklearn.linear_model import RidgeCV
from sklearn.preprocessing import PolynomialFeatures  
import collections
distance = []
entropy = [] 
alpha = [] 
logbeta = []  

n75 = "entropy_vs_distance.csv"
data = np.genfromtxt(n75, delimiter=',', skip_header=1)
distance = data[:, 0] 
entropy = data[:, 1]
distanceReduced = [] 
entropyReduced = []
distEnt = np.column_stack((distance,entropy))  
distEnt = distEnt[distEnt[:,0].argsort()]  
line = np.poly1d(np.polyfit(distance, entropy, 4)) 

# using https://stackoverflow.com/questions/29634217/get-minimum-points-of-numpy-poly1d-curve from matiasg
region = [75, 600] 
turningPoints = region + [x for x in line.deriv().r if x.imag == 0 and region[0] < x.real < region[1]]
distEnt = distEnt[distEnt[:,0] < turningPoints[2], :] 
distanceReduced = distEnt[:,0]
entropyReduced = distEnt[:,1]

gradients = [] 
for i in range(len(entropyReduced) - 1): 
    Ds = entropyReduced[i + 1] - entropyReduced[i]  
    Dx = distanceReduced[i + 1] - distanceReduced[i]  
    midpoint = distanceReduced[i] + (Dx / 2) 
    grad = Ds/Dx 
    if (grad < 0): 
        gradients.append((midpoint, grad))  

gradients = np.array(gradients) 
negGrads = np.negative(gradients[:, 1])  
coef = np.polynomial.polynomial.polyfit(np.log(gradients[:, 0]), np.log(negGrads), 1) 
logbeta.append(coef[0])  
alpha.append(coef[1]) 

for i in range(1,31):  
    distance = []
    entropy = [] 
    n75 = str("entropy_vs_distance" + str(i) + ".csv")
    data = np.genfromtxt(n75, delimiter=',', skip_header=1)
    distance = data[:, 0] 
    entropy = data[:, 1]
    distanceReduced = [] 
    entropyReduced = []
    distEnt = np.column_stack((distance,entropy))  
    distEnt = distEnt[distEnt[:,0].argsort()]  
    line = np.poly1d(np.polyfit(distance, entropy, 4)) 

    # using https://stackoverflow.com/questions/29634217/get-minimum-points-of-numpy-poly1d-curve from matiasg
    region = [75, 600] 
    turningPoints = region + [x for x in line.deriv().r if x.imag == 0 and region[0] < x.real < region[1]]
    distEnt = distEnt[distEnt[:,0] < turningPoints[2], :] 
    distanceReduced = distEnt[:,0]
    entropyReduced = distEnt[:,1]

    gradients = [] 
    for i in range(len(entropyReduced) - 1): 
        Ds = entropyReduced[i + 1] - entropyReduced[i]  
        Dx = distanceReduced[i + 1] - distanceReduced[i]  
        midpoint = distanceReduced[i] + (Dx / 2) 
        grad = Ds/Dx 
        if (grad < 0): 
            gradients.append((midpoint, grad))  

    gradients = np.array(gradients) 
    negGrads = np.negative(gradients[:, 1])  
    coef = np.polynomial.polynomial.polyfit(np.log(gradients[:, 0]), np.log(negGrads), 1) 
    logbeta.append(coef[0])  
    alpha.append(coef[1])  

plt.hist(alpha, color='blue', ec='black', bins=20) 
plt.show() 

plt.hist(logbeta, color='blue', ec='black', bins=20) 
plt.show()