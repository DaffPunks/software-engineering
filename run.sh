#!/usr/bin/env bash

LIB="libs/*"
OUT_JAR="aaa.jar"
SRC="./src"

if [[ "$OSTYPE" == "linux" ]]; then
        CP="$LIB:$OUT_JAR:$SRC"
elif [[ "$OSTYPE" == "darwin"* ]]; then
        # Mac OSX
        CP="$LIB:$OUT_JAR:$SRC"
elif [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
        CP="$LIB;$OUT_JAR;$SRC"
elif [[ "$OSTYPE" == "msys" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        CP="$LIB;$OUT_JAR;$SRC"
elif [[ "$OSTYPE" == "win32" ]]; then
        # I'm not sure this can happen.
        CP="$LIB;$OUT_JAR;$SRC"
elif [[ "$OSTYPE" == "freebsd"* ]]; then
        CP="$LIB:$OUT_JAR:$SRC"
else
        CP="$LIB:$OUT_JAR:$SRC"
fi

java -cp $CP com.god.damn.Main $*
