#!/usr/bin/env bash

mkdir out &> /dev/null
javac -d './out' -sourcepath './src' -cp "./libs/commons-cli-1.3.1.jar" src/com/god/damn/Main.java &> /dev/null

if [ $? == 0 ] ; then
    echo "Successfully compilated."
else
    echo "Unsuccessfully. Something wrong"
fi