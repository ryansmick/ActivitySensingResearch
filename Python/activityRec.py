#! /user/bin/env python3

# this program will use the SciKit to recongnize which type of motion the app is executing. We will test this on  our three collected data files from our app. We have files for walking, running, and lying down. 

import csv
from sklearn import svm
import pylab as pl
import numpy as np
from sklearn.cross_validation import StratifiedKFold
from sklearn.grid_search import GridSearchCV
from sklearn.svm import SVC

X = []
Y = []

# Load the data from the csv file
with open('2d_example.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
        i = 0
        for row in reader:
            i = i + 1
                if i==1:
                    continue
                X.append(row[1:3])
                Y.append(row[3])


count = 0
pl.figure(figsize=(10, 8))

C_range = 10.0 ** np.arange(-2, 9)
gamma_range = 10.0 ** np.arange(-5, 4)
param_grid = dict(gamma=gamma_range, C=C_range)
cv = StratifiedKFold(y=Y, n_folds=3)
grid = GridSearchCV(SVC(), param_grid=param_grid, cv=cv)
grid.fit(X, Y)
print("The best classifier is: ", grid.best_estimator_)

C = [1.0, 10.0, 100.0, 500.0]
gam_ma = [0.1, 10, 100.0, 1000.0]

for i in C:
    for j in gam_ma:
        count += 1
        clf = svm.SVC(kernel='rbf', C= i, gamma = j)
        clf.fit(X, Y)
        h = .02
        x = np.array(X, dtype= float)
        y = np.array(Y, dtype= int)
        x_min, x_max = x[:, 0].min() - 1, x[:, 0].max() + 1
        y_min, y_max = x[:, 1].min() - 1, x[:, 1].max() + 1
        xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                             np.arange(y_min, y_max, h))
            
        # classify all grid points
        Z = clf.predict(np.c_[xx.ravel(), yy.ravel()])
                
        # plot the data (circles in right hand plot), the classified grid points (solid color) and the support vectors (left hand plot)
        Z = Z.reshape(xx.shape)
                    
        pl.contourf(xx, yy, Z, cmap=pl.cm.Paired)
        pl.scatter(clf.support_vectors_[:, 0], clf.support_vectors_[:, 1], c=y[clf.support_], cmap=pl.cm.Paired)
        pl.xticks(())
        pl.yticks(())
        pl.title('C=' + str(i) + ' Gamma=' + str(j))
        pl.show()