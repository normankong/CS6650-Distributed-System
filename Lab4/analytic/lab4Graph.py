import sys
import statistics
import scipy.stats
import numpy as np
import matplotlib.pyplot as plt

folder = str(sys.argv[1])
title = f"Phase 2 with {folder} threads"
filename = folder + "/Phase2.csv"

w = 1024
h = 768

with open(filename, 'r') as f:
    lines = f.readlines()

hash = {}
for line in lines:
    temp = line.split(',')
    time = temp[7].strip()[2:4] + ':' + temp[7].strip()[4:5] + '0'
    # time = temp[7].strip()[0:2] + ':' +  temp[7].strip()[2:4] + ':' + temp[7].strip()[4:5] + '0'
    # time = temp[7].strip()[0:2] + ':' +  temp[7].strip()[2:4] + ':' + temp[7].strip()[4:6]

    latency = float(temp[2])
    if hash.get(time) is None:
        lst = [latency]
        hash[time] = lst
        # print("Creating list " + time)
    else:
        lst = hash.get(time)
        lst.append(latency)

mean_list = []
median_list = []
ninetynine_list = []
for time in hash:
    lst = hash.get(time)
    median = int(np.median(lst))
    mode = int(statistics.mode(lst))
    mean = int(np.nanmean(lst))
    variance = int(statistics.variance(lst, mean))
    percentile = int(np.percentile(lst, 99))
    range = np.ptp(lst)
    std = int(scipy.stats.tstd(lst))
    result = scipy.stats.describe(lst, ddof=1, bias=False)

    mean_list.append(mean)
    median_list.append(median)
    ninetynine_list.append(percentile)

    # print('time        : ' + time)
    # print('count       : ' + str(len(lst)))
    # print('mean        : ' + str(mean))
    # print('median      : ' + str(median))
    # print('99percentile: ' + str(percentile))
    # print('mode        : ' + str(mode))
    # print('std         : ' + str(std))
    # print('variance    : ' + str(variance))
    # print('-------------------------------')

    print(f'{time},{len(lst)},{mean},{median},{percentile},{mode},{std},{variance}')

x = hash.keys()
plt.style.use('seaborn-whitegrid')
plt.figure(figsize=(15,8))
plt.plot(x, mean_list,  label="Mean");
plt.plot(x, median_list, label="Median",  linestyle="--", color="red");
plt.plot(x, ninetynine_list, label="99th", color="green");
plt.legend()
# plt.scatter(x, mean_list, marker='o');

plt.title(title)
plt.xlabel('Time (mm:ss)')
plt.ylabel('Latency (ms)')

# plt.savefig(filename + '.png')
plt.show()