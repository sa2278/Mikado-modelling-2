import numpy as np 
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

#283 for 50
#337 for 75 radius 
#368 for 100 radius 
cuttoff = 337 

# rename the file name to the latest csv output 
# Entropy_output_of_radius(50.0)_2025-03-13_19-16-24_29.csv
# Entropy_output2025-03-07_11-44-35_896.csv  
# Entropy_output_of_radius(100.0)_2025-03-13_18-50-57_815.csv
with open("Entropy_output2025-03-07_11-44-35_896.csv", 'r') as f:
    reader = csv.reader(f)
    next(reader)

    for line in reader:
        distance.append(float(line[0]))
        entropy.append(float(line[1]))


#distance = np.log(distance)
model = np.poly1d(np.polyfit(distance, entropy, 6))


myline = np.linspace(100, 800, num=50) 

plt.scatter(distance, entropy, s=1)
plt.plot(myline, model(myline), color="red", markersize=2) 
plt.xlabel("x") 
plt.ylabel("entropy")
plt.show()  
coef = model.coefficients
print("Coeficients: ", model.coefficients) 
# print(coef[0], "x^6 + ", coef[1], "x^5 + ", coef[2], "x^4 + ", coef[3], "x^3 + ", coef[4], "x^2 + ", coef[5], "x + ", coef[6])
# print(6 * coef[0], "x^5 + ", 5 * coef[1], "x^4 + ", 4 * coef[2], "x^3 + ", 3 * coef[3], "x^2 + ", 2 * coef[4], "x + ", coef[5]) 
# print("Roots: ", model.roots)  



myline2 = np.linspace(np.log(100), np.log(cuttoff), num=50)
deriv = model.deriv()   

print("Turning points: ", deriv.roots) 
# plt.plot(myline, -deriv(myline))   
# plt.show() 

distanceRedux = [] 
entropyRedux = []

for distance, entropy in zip(distance, entropy): 
    if distance < cuttoff: 
        distanceRedux.append(distance) 
        entropyRedux.append(entropy)

# plt.plot(myline, deriv(myline)) 
# plt.show()   

# plt.scatter(distanceRedux, deriv(distanceRedux))
# plt.plot(myline2, deriv(myline2))  
# plt.show()  


# model2 = np.poly1d(np.polyfit(np.log(distanceRedux), (np.log(-deriv(distanceRedux))), deg=1)) 
# plt.scatter(distanceRedux, np.log(-deriv(distanceRedux)),marker='x')  
# plt.plot(myline2, model2(myline2))  
# plt.plot(myline2, model2(myline2))
# plt.show()  

logx = np.log(distanceRedux) 
logy = np.log(-deriv(entropyRedux))  
myline2 = np.linspace(min(logx), max(logx), len(logx))
coef = np.polyfit(logx, logy, deg=1)   
plt.plot(myline2, coef[0] * myline2 + coef[1]) 
plt.scatter(logx, logy)  
plt.xlabel("log(x)") 
plt.ylabel("log(-ds/dx)")
plt.show()

print("x^6 coefficients ", model.coefficients)   
print("linear coeficients", coef)


