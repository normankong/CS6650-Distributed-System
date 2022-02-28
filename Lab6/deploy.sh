#!/bin/bash

execute () {
  echo Deploying to $1 tomcat server

  scp -i ~/vockey.pem out/artifacts/MyApp/MyApp.war tomcat@$1:/usr/share/apache-tomcat-9.0.39/webapps
}

execute 54.90.10.84
execute 54.234.19.159

