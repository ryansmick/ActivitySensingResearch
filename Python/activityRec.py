#! /user/bin/env python

# this program will use the SciKit to recongnize which type of motion the app is executing. We will test this on  our three collected data files from our app. We have files for walking, running, and lying down. 

import csv
from sklearn import svm

trainingData = []
classifications = ["Walking", "Upstairs", "Lying Down"]
testData = []


# Load the data from the csv file
with open('walkingTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        i = i + 1
        print(row)


clf = svm.SVC()
clf.fit(trainingData, classifications)
results = clf.predict(testData)
for result in results:
    print "%s\n" % result