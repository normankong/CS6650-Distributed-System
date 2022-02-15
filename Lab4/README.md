### CS6650 Project Space - Lab 4

#### Copy file to analytic
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/32  
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/64  
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/128  
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/256  
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/512  
cp Phase*.csv ~/Desktop/NEU/CS6650/analystic/1024

java SkierClient 32 10000 40 20 http://localhost:8080  
java SkierClient 64 10000 40 20 http://localhost:8080  
java SkierClient 128 10000 40 20 http://localhost:8080  
java SkierClient 256 10000 40 20 http://localhost:8080  
java SkierClient 512 10000 40 20 http://localhost:8080  
java SkierClient 1024 10000 40 20 http://localhost:8080


#### Open Telemetry
-javaagent:opentelemetry-javaagent.jar -Dotel.service.name=Lab4-Client
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:49699
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:59361
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:55680