#!/usr/bin/env bash

mkdir out &> /dev/null
javac -d './out' -sourcepath './src' -classpath "libs/commons-cli-1.3.1.jar;libs/flyway-core-4.0.3.jar;libs/h2-1.4.192.jar" src/com/god/damn/Main.java

if [ $? == 0 ] ; then
    echo "Successfully compilated."
else
    echo "Unsuccessfully. Something wrong"
fi