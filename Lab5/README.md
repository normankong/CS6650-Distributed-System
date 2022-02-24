# Lab5 Assignment

This lab is to compare the socket performance by different set of Client/Server thread

For this lab, be ready to show how you used RabbitMQ and a thread pool and tested it out!  You can run all of this locally, but you might want to experiment with your cloud set up as well, to make sure it works.  The best way to do this will be able to collect metrics, and show your results.  Ideas of metrics to collect include:
Create a new server that utilizes a FIXED SIZE thread pool. Test out a couple of pool sizes (e.g. 20?)
In the client, use two timestamps, one before any threads run and one after all threads complete. Print out the wall time (test duration) in milliseconds before exiting
Experiment with different numbers of clients and thread pool sizes. Do you see much variation in the wall time for a test?
Compare the performance of the system without a thread pool, is there a difference in performance?


while (true)
do
echo Grepping Result
grep tps 32_result.txt
sleep 3
done