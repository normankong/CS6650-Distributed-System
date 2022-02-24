#!/bin/zsh

export JAR_PATH=
export JAR_PATH=$JAR_PATH:../../lib/amqp-client-5.7.1.jar
export JAR_PATH=$JAR_PATH:../../lib/slf4j-api-1.7.26.jar
export JAR_PATH=$JAR_PATH:../../lib/slf4j-simple-1.7.26.jar

SERVER_THREAD=100

execute () {
    echo Executing with server thread $1 with client thread $2
    java -cp $JAR_PATH socket.SocketClient $2
    echo Wait for 10 seconds
    sleep 10
}

#java -cp $JAR_PATH socket.SocketServer $SERVER_THREAD

cd target/classes

execute $SERVER_THREAD 1
execute $SERVER_THREAD 5
execute $SERVER_THREAD 10
execute $SERVER_THREAD 15
execute $SERVER_THREAD 20

echo Result :
grep tps ../../result.txt

mv ../../result.txt ../../data/${SERVER_THREAD}_result.txt

