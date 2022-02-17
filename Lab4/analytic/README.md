# Lab4 to Generate Graph
python3 lab4Graph.py 32  
python3 lab4Graph.py 64  
python3 lab4Graph.py 128  
python3 lab4Graph.py 256  
python3 lab4Graph.py 512  
python3 lab4Graph.py 1024  

# Calculate Statistic per bucket
python3 lab4xMeanVsThread.py 32/Phase2.csv
python3 lab4xMeanVsThread.py 64/Phase2.csv
python3 lab4xMeanVsThread.py 128/Phase2.csv
python3 lab4xMeanVsThread.py 256/Phase2.csv
python3 lab4xMeanVsThread.py 512/Phase2.csv
python3 lab4xMeanVsThread.py 1024/Phase2.csv

# Generate TPS/Mean vs Thread# Graph (Manual update)
python3 lab4xThread.py