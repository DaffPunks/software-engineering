#!/usr/bin/env bash

numberOfCompletedTests=0
numberOfTests=0

testing(){
    let "numberOfTests += 1"
    ./run.sh $1 &> /dev/null
    if [ $? == $2 ] ; then
        echo "Successfully of $numberOfTests"
        let "numberOfCompletedTests += 1"
    else
        echo "Unsuccessfully of $numberOfTests"
    fi
}
isSuccessfullTest(){
    echo "$numberOfCompletedTests/$numberOfTests"

    if [ $numberOfCompletedTests == $numberOfTests ] ; then
        echo "All Tests Completed Successfuly"
    else
        echo "Some tests are failed"
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
testing "-login jdoe -pass sup3rpaZZ -role 1 -res a" 0
testing "-login jdoe -pass sup3rpaZZ -role 1 -res a.b" 0
testing "-login jdoe -pass sup3rpaZZ -role X -res a.b" 3
testing "-login jdoe -pass sup3rpaZZ -role 1 -res XXX" 4
testing "-login jdoe -pass sup3rpaZZ -role 2 -res a" 4
testing "-login jdoe -pass sup3rpaZZ -role 2 -res a.bc" 4
#Accounting
testing "-login jdoe -pass sup3rpaZZ -role 1 -res a.b -ds 12-01-2016 -de 13-01-2016 -val 100" 0
testing "-login jdoe -pass sup3rpaZZ -role 1 -res a.b -ds 12/01/2016 -de 13/01/2016 -val 100" 5
testing "-login jdoe -pass sup3rpaZZ -role 1 -res a.b -ds 12-01-2016 -de 13-01-2016 -val XXX" 5

isSuccessfullTest

