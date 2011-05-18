# coding: utf-8
'''
Created on 2011-05-15

@author: berni
'''

from Crypto.Cipher import AES
import base64

#Define exceptions
class NoPadInString(Exception):
    '''
    String doesn't have padding character 
    '''
    pass  

class Cipher(object):
    '''
    Cryptography class for Android/Java communication
    '''
    # the block size for the cipher object; must be 16, 24, or 32 for AES
    BLOCK_SIZE = 32
    
    # the character used for padding--with a block cipher such as AES, the value
    # you encrypt must be a multiple of BLOCK_SIZE in length.  This character is
    # used to ensure that your value is always a multiple of BLOCK_SIZE
    PADDING = '|'
    
    # secret key
    secret = "1234567890abcdef"
    
    # create a cipher object using the secret
    cipher = AES.new(secret)

    # pad the text to be encrypted
    @classmethod
    def pad(cls, unpadded_str):
        '''
        Add padding to unpadded string
        @param cls:
        @param unpadded_str:
        '''
        return unpadded_str + (cls.BLOCK_SIZE - len(unpadded_str) % cls.BLOCK_SIZE) * cls.PADDING
    
    @classmethod
    def encrypt(cls, decoded):
        '''
        Encrypt decoded with AES
        @param cls:
        @param decoded: stringto encrypt
        '''
        # add length before se we know which characters are padded only
        with_pad = "".join([str(len(decoded.decode("utf8"))), "|", decoded])
        return base64.b64encode(cls.cipher.encrypt(cls.pad(with_pad)))
    
    @classmethod
    def decrypt(cls, encoded):
        '''
        Decrypt AES coded string and strip padding characters
        @param cls:
        @param encoded:
        '''
        whole = cls.cipher.decrypt(base64.b64decode(encoded)).rstrip(cls.PADDING)
        pad = whole.find("|")
        if pad == -1:
            raise NoPadInString
        return whole[pad+1:]
    
"""Python AES cryptography for Java/Android communication.

-e    Encrypt input string   
-d    Decrypt input string.
"""
import sys
import getopt

def main():
    '''
    Command line for testing purposes
    '''
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hed", ["help"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    # process options
    action = "d" 
    in_file = ""
   
    for opt, a in opts:
        if opt in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif opt in ("-e", "--encrypt"):
            action = "e" 
            in_file = args[0]
        elif opt in ("-d", "--decrypt"):
            in_file = args[0]			
    file_string = open(in_file, 'r').read()
    if action == "e":
        sys.stdout.write(Cipher.encrypt(file_string))
    else:
        sys.stdout.write(Cipher.decrypt(file_string))
        
if __name__ == "__main__":
    main()