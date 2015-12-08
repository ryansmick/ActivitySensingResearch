#! /user/bin/env python

# this program will use the SciKit to recongnize which type of motion the app is executing. We will test this on  our three collected data files from our app. We have files for walking, running, and lying down. 

import csv
from sklearn import svm

trainingData = []
classifications = ["Walking", "Upstairs", "Lying Down"]
testData = []
walking=[]
upstairs=[]
lyingDown=[]

stop = 180

# Load the data from the walkingTable.csv file
with open('walkingTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i==stop:
            break
        for word in row:
            i = i + 1
            if i==stop:
                break
            walking.append(word)


# Load the data from the stairsTable.csv file
with open('stairsTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i==stop:
            break
        for word in row:
            i = i + 1
            if i==stop:
                break
            upstairs.append(word)

# Load data from lyingDownTable.csv file
with open('lyingDownTable.csv', 'rb') as csvfile:
    reader = csv.reader(csvfile)
    i = 0
    for row in reader:
        if i==stop:
            break
        for word in row:
            i = i + 1
            if i==stop:
                break
            lyingDown.append(word)

trainingData.append(walking)
trainingData.append(upstairs)
trainingData.append(lyingDown)

print(trainingData)

clf = svm.SVC()
clf.fit(trainingData, classifications)
results = clf.predict(testData)
for result in results:
    print "%s\n" % result