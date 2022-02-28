#!/bin/bash

LANGUAGE=java-lab6

execute () {
   echo 'Executing with thread' $1' with '$2
   touch Phase1.csv
   rm Phase*.csv

#   mvn exec:java -Dexec.mainClass="cs6650.client.SkierClient" -Dexec.args="$2 20000 40 20 http://localhost:8080/MyApp"
   mvn exec:java -Dexec.mainClass="cs6650.client.SkierClient" -Dexec.args="$2 20000 40 20 http://cs6650-alb-119407693.us-east-1.elb.amazonaws.com:8080/MyApp"

   mkdir -p      ~/Desktop/NEU/CS6650/analystic/data/$1/$2
   cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/data/$1/$2

   echo Sleep for 10 seconds before trigger new batch
   sleep 10
}
#execute $LANGUAGE 512
#execute $LANGUAGE 256
#execute $LANGUAGE 128
#execute $LANGUAGE 64

echo Result :
grep -e Executing -e tps result.txt

cp result.txt ~/Desktop/NEU/CS6650/analystic/data/$LANGUAGE/
