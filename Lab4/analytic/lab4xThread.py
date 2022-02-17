import matplotlib.pyplot as plt

x = ["32", "64", "128", "256", "512", "1024"]    # Thread Number

# Input the finding in order to plot the graph
y1 = [1061, 1286, 1330, 2552, 5158, 10397]       # Mean Response Time
y2 = [29.78, 48.11, 88.88, 93.61, 93.50, 91.08]  # TPS

label1 = 'Mean Response'
label2 = 'TPS'

ylabel1 = 'Mean Response(ms)'
ylabel2 = 'TPS'

title1 = 'Mean Response vs Threads'
title2 = 'TPS vs Threads'

# Switch the information here
y = y1
title = title1
label = label1
ylabel = ylabel1

plt.style.use('seaborn-whitegrid')
plt.figure(figsize=(15,8))

plt.plot(x, y, label=label);
plt.legend()
plt.title(title)

plt.xlabel('Thread')
plt.ylabel(ylabel1)

plt.show()
