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

#All successful requests
testing "-login jdoe -pass sup3rpaZZ" 0
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2" 0
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2 -ds 12-01-2016 -de 13-01-2016 -val 1" 0
#Shuffled request
testing "-pass sup3rpaZZ -ds 12-01-2016 -res a.b -role 2 -val 1 -de 13-01-2016 -login jdoe " 0
#Incorrect login
testing "-login logologo -pass somepass" 1
#Incorrect password
testing "-login jdoe -pass wrongPass" 2
#Wrong role
testing "-login jdoe -pass sup3rpaZZ -role 10 -res a.b" 3
testing "-login jdoe -pass sup3rpaZZ -role Write -res a.b" 3
#Access denied
testing "-login jdoe -pass sup3rpaZZ -res a -role 2" 4
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 4" 4
#Incorrect value or date
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2 -ds 12-01-2016 -de 13-01-2016 -val One" 5
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2 -ds 12_01_2016 -de 13_01_2016 -val 1" 5
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2 -ds 12-01-2016 -de 13/01/2016 -val 1" 5
#Some cases with not enough parameters
testing "-login jdoe -pass sup3rpaZZ -res a.b -role 2 -ds 12-01-2016 -de 13-01-2016" 0
testing "-login jdoe -pass sup3rpaZZ -ds 12-01-2016 -de 13-01-2016" 0

isSuccessfullTest

