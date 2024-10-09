
import matplotlib.pyplot as plt
import csv
  
x = []
y = []
  
with open('Pro1.csv','r') as csvfile:
    lines = csv.reader(csvfile, delimiter=',')
    for row in lines:
        x.append(row[0])
        y.append(row[1])
print(x)
print(y)
plt.scatter(x = x, y = y, c = 'r', marker = 'x')
plt.xlabel('Run')
plt.ylabel('Time')
plt.title('Execution time for each run')
  
plt.show()