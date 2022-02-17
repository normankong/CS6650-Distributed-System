import matplotlib.pyplot as plt

x = ["32", "64", "128", "256", "512", "1024"]    # Thread Number

# Input the finding in order to plot the graph
y1 = [1018.3697102864584,1025.806884765625, 1301.5977376302083,2553.0930989583335,5118.595784505208,10303.908854166666] # Mean Response Time
y2 = [31.388817,62.207642,96.230804,96.624283,96.069050, 93.357544]  # TPS

label1 = 'Mean Response'
label2 = 'TPS'

ylabel1 = 'Mean Response(ms)'
ylabel2 = 'TPS'

title1 = 'Mean Response vs Threads'
title2 = 'TPS vs Threads'

# Switch the information here
y = y2
title = title2
label = label2
ylabel = ylabel2

plt.style.use('seaborn-whitegrid')
plt.figure(figsize=(15,8))

plt.plot(x, y, label=label);
plt.legend()
plt.title(title)

plt.xlabel('Thread')
plt.ylabel(ylabel1)

plt.show()
