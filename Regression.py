import numpy as np 
import seaborn
import pandas as pd
import matplotlib.pyplot as plt
import csv  
from sklearn.linear_model import LinearRegression 
from sklearn.linear_model import RidgeCV
from sklearn.preprocessing import PolynomialFeatures

distance = []
entropy = []

# rename the file name to the latest csv output
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
plt.show()  
coef = model.coefficients
print("Coeficients: ", model.coefficients) 
# print(coef[0], "x^6 + ", coef[1], "x^5 + ", coef[2], "x^4 + ", coef[3], "x^3 + ", coef[4], "x^2 + ", coef[5], "x + ", coef[6])
# print(6 * coef[0], "x^5 + ", 5 * coef[1], "x^4 + ", 4 * coef[2], "x^3 + ", 3 * coef[3], "x^2 + ", 2 * coef[4], "x + ", coef[5]) 
# print("Roots: ", model.roots)  



#337
myline2 = np.linspace(100, 337, num=50)
deriv = model.deriv()   

# print("Roots: ", deriv.roots) 
# plt.plot(myline, -deriv(myline))   
# plt.show() 

distanceRedux = [] 
entropyRedux = []

for distance, entropy in zip(distance, entropy): 
    if distance < 337: 
        distanceRedux.append(distance) 
        entropyRedux.append(entropy)

# plt.plot(myline, deriv(myline)) 
# plt.show()   

plt.plot(myline2, deriv(myline2)) 
plt.show()  
 
model2 = np.poly1d(np.polyfit(np.log(distanceRedux), np.log(-deriv(distanceRedux)), deg=1)) 
plt.scatter(distanceRedux, model2(distanceRedux),marker='x') 
plt.plot(myline2, model2(myline2)) 
plt.show()

print(model2.coefficients)
# plt.plot(np.log(myline), np.log(-deriv(myline)))  
# plt.show() 

# print("Deriv coefficients: ", deriv.coefficients)    


""" print("taking logs of both sides"
"/ log distance")

distance = np.log(distance)
model2 = np.poly1d(np.polyfit(distance, log_entropy, 6))

myline = np.linspace(5, 12, num=1) 

plt.scatter(distance, log_entropy, s=1)
plt.plot(myline, model2(myline), color="red", markersize=2)
plt.show()  
coef = model2.coefficients
print(model2.coefficients) 
print(coef[0], "x^6 + ", coef[1], "x^5 + ", coef[2], "x^4 + ", coef[3], "x^3 + ", coef[4], "x^2 + ", coef[5], "x + ", coef[6])
print(6 * coef[0], "x^5 + ", 5 * coef[1], "x^4 + ", 4 * coef[2], "x^3 + ", 3 * coef[3], "x^2 + ", 2 * coef[4], "x + ", coef[5]) 

deriv = model2.deriv()  
plt.plot(deriv)  
plt.show()

 """