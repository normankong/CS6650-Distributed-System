# Lab 6

## Installation Guide
## 1) Follow this guide to install the binary
https://www.rabbitmq.com/install-debian.html#apt-quick-start-packagecloud  

## 2) Run below command as root

### Installce the Management plugin console by root
```bash
rabbitmq-plugins enable rabbitmq_management  

echo ##########################################################################
echo # Adding "test"/"test" user permission  
echo ##########################################################################
rabbitmqctl add_user test test  
rabbitmqctl set_user_tags test administrator  
rabbitmqctl set_permissions -p / test ".*" ".*" ".*"  

echo ##########################################################################
echo Retrive the Link  
echo http://`curl -s http://169.254.169.254/latest/meta-data/public-ipv4`:15672  
echo ##########################################################################

echo Open browser and login as test/test  
```

## 3) In Java, add the following code to login after 
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(New Host);
    factory.setUsername("test");
    factory.setPassword("test");



### Telemetry
-javaagent:opentelemetry-javaagent.jar -Dotel.service.name=Lab4-Client

### Rabbit MQ Admin
```shell
rabbitmqadmin -f long -d 3 list queues
rabbitmqadmin list queues name messages message_stats.publish_details.rate message_stats.deliver_get_details.rate
```

### Direct Trigger Rabbit MQ API Status.sh
```shell
#!/bin/bash
while true
do
	clear
	date
	curl -s -u test:test http://localhost:15672/api/queues/  | jq '.[0] | {name, messages, publish : .message_stats.publish_details.rate, consume : .message_stats.deliver_get_details.rate}'
	sleep 1
done
```

### Deployment without Git Action
```shell
scp -i ~/vockey.pem out/artifacts/MyApp/MyApp.war tomcat@18.235.121.1:/usr/share/apache-tomcat-9.0.39/webapps  
```

### Tmux cheatsheet
control+b "
resize-pane -D 4


#
# TABLE STRUCTURE FOR: LiftRides
#

CREATE TABLE `LiftRides` (
`skierId` int(11) NOT NULL,
`resortId` int(11)  NOT NULL,
`seasonId` varchar(255) NOT NULL,
`dayId` varchar(255) NOT NULL,
`time` int(11) NOT NULL,
`liftId` int(11) NOT NULL,
`waitTime` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Install Mysql Client on Amazon OS
sudo yum install mariadb -y

# Install Mysql Client on Ubuntu OS
sudo apt install mysql-client-core-8.0

# Connect DB
mysql -h neu-database.c93msbykaob9.us-east-1.rds.amazonaws.com -u admin -pXXXXXXX neu_6650 