import matplotlib.pyplot as plt

x = ["32", "64", "128", "256", "512"]    # Thread Number

# Input the finding in order to plot the graph
y1 = [98.75907258064517,94.4263888888889, 95.98102678571429,99.00748697916667,121.90966796875] # Mean Response Time
y2 = [31.388817,62.207642,96.230804,96.624283,96.069050]  # TPS

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
