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

#cuttoff = 283 for 50
#cuttoff = 337 for 75 radius 
#cuttoff = 368 for 100 radius 
cuttoff = 337   

print("over 1000 steps the models variation is") 
n75 = "Entropy_output_of_radius(1milli)(1000)(75.0).csv"  
data = np.genfromtxt(n75, delimiter=',', skip_header=1)
distance = data[:, 0] 
entropy = data[:, 1]  
plt.scatter(distance, entropy, s=10, marker="x") 
plt.show()
print(np.var(data, axis=0)) 

# rename the file name to the latest csv output 
# n50 = "Entropy_output_of_radius(50.0)_2025-03-13_19-16-24_29.csv"
# n75 = "Entropy_output2025-03-07_11-44-35_896.csv"  
# n100 = "Entropy_output_of_radius(100.0)_2025-03-13_18-50-57_815.csv"  
n75 = "Entropy_output_of_radius(1milli)(1000)(75.0).csv"
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
negEntropy = -entropy 



logx = np.log(distance)  
xSegLog = np.linspace(min(logx), max(logx), len(logx))  
xSeg = np.linspace(min(distance), max(distance), len(distance)) 

plt.scatter(distance, entropy, s=10, marker="x")  
plt.plot(xSeg, line(xSeg))  
plt.ylabel("entropy")   
plt.xlabel("distance") 
plt.show()
print(np.polyfit(distance, entropy, 4))

xSegReduced = np.linspace(min(distanceReduced), max(distanceReduced), len(distanceReduced)) 


plt.scatter(distanceReduced, entropyReduced, s=10, marker="x")  
coef = np.polynomial.polynomial.polyfit(distanceReduced, entropyReduced, 4) 
# coef[0] + coef[1] * xSegReduced + coef[2] * (xSegReduced^2) + coef[3] * (xSegReduced^3) + coef[4] * (xSegReduced^4)
plt.plot(xSegReduced, line(xSegReduced))    
plt.ylabel("entropy")   
plt.xlabel("distance") 
print(np.polyfit(distanceReduced, entropyReduced, 4)) 
plt.show() 

y = -np.array(distEnt[:, 1]) 
logx = np.log(distEnt[:, 0]) 
logy = np.log(distEnt[:, 1])    


print("turning point: " , turningPoints) 



entropyReduced = distEnt[:, 1] 
distanceReduced = distEnt[:, 0]  

distanceUnique = []
distanceUnique = np.unique(distanceReduced) 
entropyFitted = [] 
entropyFitted = line(distanceUnique) 

gradients = [] 
for i in range(len(entropyReduced) - 1): 
    Ds = entropyReduced[i + 1] - entropyReduced[i]  
    Dx = distanceReduced[i + 1] - distanceReduced[i]  
    midpoint = distanceReduced[i] + (Dx / 2) 
    grad = Ds/Dx 
    if (grad < 0): 
        gradients.append((midpoint, grad)) 

gradients = np.array(gradients)
print("before" , gradients[0][1]) 

plt.scatter(gradients[:, 0], gradients[:, 1], s=1, marker="x")  
plt.show() 

negGrads = np.negative(gradients[:, 1]) 
print("after", negGrads[0])

#TODO calculate the derivative and then plot a straight line 
# do this by taking the fitted regression line and transforming its 
coef = np.polynomial.polynomial.polyfit(np.log(gradients[:, 0]), np.log(negGrads), 1)
line = np.linspace(min(gradients[:, 0]), max(gradients[:, 0]), len(gradients[:, 0]))
plt.plot(np.log(line), coef[0] + coef[1] * np.log(line))
plt.scatter(np.log(gradients[:, 0]), np.log(negGrads), s=10, marker="x")    
plt.xlabel("Ln(Distance)") 
plt.ylabel("Ln(-dS/dx)")   
plt.show()
print(coef[1] , "x + " , coef[0]) 

with open('readme.txt', 'a') as f: 
    f.write('\n') 
    f.write(str(coef[1])) 
    f.write("x + ") 
    f.write(str(coef[0]))
    f.write('\n') 

