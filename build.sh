#!/usr/bin/env bash

mkdir out &> /dev/null
javac -d "./out" -sourcepath './src' -classpath "libs/*" src/com/god/damn/Main.java

jar -cfe "aaa.jar" com.god.damn.Main -C "out/" .


if [ $? == 0 ] ; then
    echo "Successfully compilated."
    exit 0
else
    echo "Unsuccessfully. Something wrong"
    exit 1
fi