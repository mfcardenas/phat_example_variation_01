#!/bin/bash

mvn clean compile
mvn exec:java -Dexec.mainClass=phat.ActivityVariationDemo