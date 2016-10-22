#!/usr/bin/env bash

numberOfCompletedTests=0
numberOfTests=0
mode=$*

testing(){
    let "numberOfTests += 1"
    if [[ $mode = -dev ]] ; then
        ./run.sh $1
    else
        ./run.sh $1 &> /dev/null
    fi

    if [ $? == $2 ] ; then
        echo "Successfully of $numberOfTests $1"
        echo ""
        let "numberOfCompletedTests += 1"
    else
        echo "Unsuccessfully of $numberOfTests $1"
        echo ""
    fi
}
isSuccessfullTest(){
    echo "$numberOfCompletedTests/$numberOfTests"

    if [ $numberOfCompletedTests == $numberOfTests ] ; then
        echo "All Tests Completed Successfuly"
        exit 0
    else
        echo "Some tests are failed"
        exit 1
    fi
}

#Helps
testing "" 0
testing "-h" 0
#Authentication
testing "-login XXX -pass XXX" 1
testing "-login jdoe -pass XXX" 2
testing "-login jdoe -pass sup3rpaZZ" 0
#Authorization
testing "-login jdoe -pass sup3rpaZZ -role READ -res a" 0
testing "-login jdoe -pass sup3rpaZZ -role READ -res a.b" 0
testing "-login jdoe -pass sup3rpaZZ -role X -res a.b" 3
testing "-login jdoe -pass sup3rpaZZ -role READ -res XXX" 4
testing "-login jdoe -pass sup3rpaZZ -role WRITE -res a" 4
testing "-login jdoe -pass sup3rpaZZ -role WRITE -res a.bc" 4
#Accounting
testing "-login jdoe -pass sup3rpaZZ -role READ -res a.b -ds 12-01-2016 -de 13-01-2016 -val 100" 0
testing "-login jdoe -pass sup3rpaZZ -role READ -res a.b -ds 12/01/2016 -de 13/01/2016 -val 100" 5
testing "-login jdoe -pass sup3rpaZZ -role READ -res a.b -ds 12-01-2016 -de 13-01-2016 -val XXX" 5

isSuccessfullTest

