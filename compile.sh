#!/bin/sh

if [ ! -d ./classes ]; then
    mkdir ./classes
fi

cd src/main/java
javac -Xlint:deprecation -Xlint:unchecked -d ../../../classes -classpath .:../../../lib/vsdk.jar co/cubestudio/DebianAnalyzer.java
cd ..
