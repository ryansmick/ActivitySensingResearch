#! /user/bin/env python3

# this program will use the SciKit to recongnize which type of motion the app is executing. We will test this on  our three collected data files from our app. We have files for walking, running, and lying down. 

import csv
from sklearn import svm

trainingData = []
classifications = ["Walking", "Upstairs", "Lying Down"]
testData = []


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


clf = svm.SVC()
clf.fit(trainingData, classifications)
results = clf.predict(testData)
for result in results:
    print "%s\n" % result