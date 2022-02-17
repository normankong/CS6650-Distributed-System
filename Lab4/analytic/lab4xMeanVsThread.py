import numpy as np
import statistics
import scipy.stats
import sys

filename = str(sys.argv[1])

if __name__ == '__main__':
    x = np.genfromtxt(filename, delimiter=",", dtype=None, encoding=None, usecols=(2))
    median = np.median(x)
    mode = statistics.mode(x)
    mean = np.nanmean(x)
    variance = statistics.variance(x, mean)
    percentile = np.percentile(x, 99)
    range = np.ptp(x)
    std = scipy.stats.tstd(x)
    result = scipy.stats.describe(x, ddof=1, bias=False)

    print('mean        : ' + str(mean))
    print('median      : ' + str(median))
    print('99percentile: ' + str(percentile))
    print('mode        : ' + str(mode))
    print('std         : ' + str(std))
    print('variance    : ' + str(variance))
