# coding: utf-8
'''
Created on 2011-05-15

@author: berni
'''

import unittest
from crypt import Cipher

class KnownValues(unittest.TestCase):                                   
    knownValues = ( 
                    (
                     "123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|} ", 
                     "MSGi2oLpyuzaLQSA+bxQtsmW6q7OFwYeOBKTBBwoM9QKCHfry9Y3Sw7i9yDxrm5RYe3fPUaW5j59Yj2clkLgUALllj1V2LuTV3L0FctLtDigkmHwlKjRvAKw/ISr5++R"
                     ),    
                  )                               

    def testKnownValues(self):                                   
        """encrypt should give known result with known input"""
        for clear_text, encrypted_text in self.knownValues:              
            result = Cipher.encrypt(clear_text)          
            self.assertEqual(encrypted_text, result)        
            
class SanityCheck(unittest.TestCase):        
    def testSanity(self):                    
        """decrypt(encrypt(str))==str for all unicode chars"""
        import sys
        chars = []
        for i in xrange(0,sys.maxint):
            try:
                chars.append(unichr(i))
            except ValueError:
#                print i," not in range"
                break 
        str = "".join(chars).encode('utf8')
        self.assertEqual(str, Cipher.decrypt(Cipher.encrypt(str)))
            
if __name__ == "__main__":
    unittest.main()