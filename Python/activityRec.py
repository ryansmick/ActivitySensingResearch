#! /user/bin/env python

# this program will use the SciKit to recongnize which type of motion the app is executing. We will test this on  our three collected data files from our app. We have files for walking, running, and lying down. 

import csv
from sklearn import svm

trainingData = []
classifications = ["Walking", "Upstairs", "Lying Down"]
testData = []
walking = []
upstairs = []
lyingDown = []

stop = 180

# Load the data from the walkingTable.csv file
with open('walkingTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i == stop:
            break
        for word in row:
            if i == stop:
                break
            try:
                walking.append(float(word))
                i = i + 1
            except ValueError, e:
                print "Error: ", e, " in walking"

# Load the data from the stairsTable.csv file
with open('stairsTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i==stop:
            break
        for word in row:
            if i==stop:
                break
            try:
                upstairs.append(float(word))
                i = i + 1
            except ValueError, e:
                print "Error: ", e, " in upstairs"
# Load data from lyingDownTable.csv file
with open('lyingDownTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i==stop:
            break
        for word in row:
            if i==stop:
                break
            try:
                lyingDown.append(float(word))
                i = i + 1
            except ValueError, e:
                print "Error: ", e, " in lying down"


# Load test data
with open('walkingTestData.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        for word in row:
            if i==stop:
                break
            try:
                testData.append(float(word))
                i = i + 1
            except TypeError, e:
                print "Error: ", e, " in test data"


trainingData.append(walking)
trainingData.append(upstairs)
trainingData.append(lyingDown)

clf = svm.SVC()
clf.fit(trainingData, classifications)
results = clf.predict(testData)
for result in results:
    print "%s\n" % result