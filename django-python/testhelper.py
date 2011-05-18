'''
Created on 2011-05-15

@author: berni
'''
import sys 

CHAR_LIST = []
#for i in xrange(0,sys.maxint):
for i in xrange(0, 32048):
    try:
        CHAR_LIST.append(unichr(i))
    except ValueError:
#        print i," not in range"
        break 
sys.stdout.write("".join(CHAR_LIST).encode('utf8'))	