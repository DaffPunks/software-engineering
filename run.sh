#!/usr/bin/env bash

LIB="libs/*"
OUT_JAR="aaa.jar"

if [[ "$OSTYPE" == "linux" ]]; then
        CP="$LIB:$OUT_JAR"
elif [[ "$OSTYPE" == "darwin"* ]]; then
        # Mac OSX
        CP="$LIB:$OUT_JAR"
elif [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
        CP="$LIB;$OUT_JAR"
elif [[ "$OSTYPE" == "msys" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        CP="$LIB;$OUT_JAR"
elif [[ "$OSTYPE" == "win32" ]]; then
        # I'm not sure this can happen.
        CP="$LIB;$OUT_JAR"
elif [[ "$OSTYPE" == "freebsd"* ]]; then
        CP="$LIB:$OUT_JAR"
else
        CP="$LIB:$OUT_JAR"
fi

java -cp $CP com.god.damn.Main $*
