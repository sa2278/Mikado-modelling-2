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

distance = []
entropy = [] 

def exponential(x, a, b, c):
    return (a*(x**b)) + c

#cuttoff = 283 for 50
#cuttoff = 337 for 75 radius 
#cuttoff = 368 for 100 radius 
cuttoff = 337 

# rename the file name to the latest csv output 
# n = "Entropy_output_of_radius(50.0)_2025-03-13_19-16-24_29.csv"
n = "Entropy_output2025-03-07_11-44-35_896.csv"  
# n = "Entropy_output_of_radius(100.0)_2025-03-13_18-50-57_815.csv"
with open(n, 'r') as f:
    reader = csv.reader(f)
    next(reader)

    for line in reader:
        distance.append(float(line[0]))
        entropy.append(float(line[1]))


#distance = np.log(distance)
model = np.poly1d(np.polyfit(distance, entropy, 4))
myline = np.linspace(min(distance), max(distance), num=len(distance)) 

plt.scatter(distance, entropy, s=1)
plt.plot(myline, model(myline), color="red", markersize=2) 
plt.xlabel("x") 
plt.ylabel("entropy")
plt.show()  
coef = model.coefficients
print("Coeficients: ", model.coefficients) 
# print(coef[0], "x^6 + ", coef[1], "x^5 + ", coef[2], "x^4 + ", coef[3], "x^3 + ", coef[4], "x^2 + ", coef[5], "x + ", coef[6])
# print(6 * coef[0], "x^5 + ", 5 * coef[1], "x^4 + ", 4 * coef[2], "x^3 + ", 3 * coef[3], "x^2 + ", 2 * coef[4], "x + ", coef[5]) 




myline2 = np.linspace(np.log(100), np.log(cuttoff), num=50)
deriv = model.deriv()   

print("Turning points: ", deriv.roots) 


reducedDistanceEntropyDict = {} 


#rather than have a noisy scatter graph, the values are stored in a dictionary if they are the same and averaged
for distanceLoc, entropyLoc in zip(distance, entropy): 
    if distanceLoc < cuttoff:  
        if distanceLoc in reducedDistanceEntropyDict:  
            reducedDistanceEntropyDict[distanceLoc][0] += entropyLoc 
            reducedDistanceEntropyDict[distanceLoc][1] += 1 
        else: 
            reducedDistanceEntropyDict[distanceLoc] = [entropyLoc, 1]

dict(sorted(reducedDistanceEntropyDict.items()))

distanceReduced = [] 
entropyReduced = []
for key in reducedDistanceEntropyDict: 
    distanceReduced.append(key) 
    entropyReduced.append(reducedDistanceEntropyDict[key][0] / reducedDistanceEntropyDict[key][1])



myline = np.linspace(min(distanceReduced), max(distanceReduced), num=len(distanceReduced)) 
plt.scatter(distanceReduced, entropyReduced, s=1)
plt.xlabel("x") 
plt.ylabel("entropy")
model2 = np.polynomial.polynomial.polyfit(distanceReduced, entropyReduced, 4)  
print(model2)  
plt.plot(myline, model2[0] + model2[1]*(myline) + model2[2]*(myline)*(myline) + model2[3]*(myline)*(myline)*(myline) + model2[4] *(myline)*(myline)*(myline)*(myline), color="red", markersize=2)  

deriv = np.polynomial.polynomial.polyder(model2) 
print(deriv) 
 
plt.show()   


logx = np.log(distanceReduced) 
logy = np.log(np.gradient(entropyReduced, distanceReduced))
myline2 = np.linspace(min(logx), max(logx), len(logx))
coef = np.polyfit(logx, logy, deg=1)   
plt.plot(myline2, coef[0] * myline2 + coef[1]) 
plt.scatter(logx, logy)  
plt.xlabel("log(x)") 
plt.ylabel("log(-ds/dx)")
plt.show()

logx = np.log(distance) 
logy = np.log(np.gradient(entropy, distance))
myline2 = np.linspace(min(logx), max(logx), len(logx))
coef = np.polyfit(logx, logy, deg=1)   
plt.plot(myline2, coef[0] * myline2 + coef[1]) 
plt.scatter(logx, logy, s=2)  
plt.xlabel("log(x)") 
plt.ylabel("log(-ds/dx)")
plt.show() 


print("x^6 coefficients ", model.coefficients)   
print("linear coeficients", coef)


