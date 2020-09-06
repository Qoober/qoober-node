#!/bin/sh
APPLICATION="qoober"
if [ -e ~/.${APPLICATION}/qoober.pid ]; then
    PID=`cat ~/.${APPLICATION}/qoober.pid`
    ps -p $PID > /dev/null
    STATUS=$?
    echo "stopping"
    while [ $STATUS -eq 0 ]; do
        kill `cat ~/.${APPLICATION}/qoober.pid` > /dev/null
        sleep 5
        ps -p $PID > /dev/null
        STATUS=$?
    done
    rm -f ~/.${APPLICATION}/qoober.pid
    echo "Qoober server stopped"
fi

