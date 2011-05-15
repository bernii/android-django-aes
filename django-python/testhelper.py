import sys 

chars = []
#for i in xrange(0,sys.maxint):
for i in xrange(0,32048):
    try:
        chars.append(unichr(i))
    except ValueError:
#        print i," not in range"
        break 
sys.stdout.write("".join(chars).encode('utf8'))	