f = open("log.txt","r");

total_ts = 0;
total_tj = 0;
q_count = 0;
for line in f:
    
    t = line.split(" ")
    
    
    if(len(t)>=2):
        if(t[0]=="TS"):
            total_ts+=int(t[1])
            q_count+=1;
        else:
            total_tj+=int(t[1])
    



print "Avg ts",total_ts/q_count
print "Avg tj",total_tj/q_count
