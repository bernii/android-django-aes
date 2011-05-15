#!/bin/bash
echo Testing compatibility of created decryption/encryption algorithms between Java and Python

if [ -f tmp_decoded.txt ]
then
rm tmp_decoded.txt
fi

if [ -f tmp_encrypted_python.txt ]
then
rm tmp_encrypted_python.txt
fi

if [ -f tmp_decrypted_python.txt ]
then
rm tmp_decrypted_python.txt
fi

if [ -f tmp_encrypted_java.txt ]
then
rm tmp_encrypted_java.txt
fi

if [ -f tmp_decrypted_java.txt ]
then
rm tmp_decrypted_java.txt
fi

# Create file with all unicode characters in system
python django-python/testhelper.py > tmp_decoded.txt

# Encode and decode with python lib
python django-python/crypt.py -e tmp_decoded.txt > tmp_encrypted_python.txt

# Encode and decode with java lib
javac android-java/crypt/*.java
cd android-java
java crypt.Cipher -e ../tmp_decoded.txt > ../tmp_encrypted_java.txt
java crypt.Cipher -d ../tmp_encrypted_python.txt > ../tmp_decrypted_java.txt
cd ../
python django-python/crypt.py -d tmp_encrypted_java.txt > tmp_decrypted_python.txt

# Compare Python decoded and encoded 
if diff tmp_decoded.txt tmp_decrypted_python.txt >/dev/null ; then
  echo "[ OK ] Python encode/decode script"
else
  echo "! ERROR ! Python encode/decode is wrong :("
fi

# Compare Java encoded and decoded
if diff tmp_decoded.txt tmp_decrypted_java.txt >/dev/null ; then
  echo "[ OK ] Java encode/decode script"
else
  echo "! ERROR ! Java encode/decode is wrong :("
fi

# Compare Java encoded with Python encoded
if diff tmp_encrypted_python.txt tmp_encrypted_java.txt >/dev/null ; then
  echo "[ OK ] Java and Python compatibility"
else
  echo "! ERROR ! Java and Python compatibility is wrong :("
fi

# Cleanup
rm tmp_decoded.txt
rm tmp_encrypted_python.txt
rm tmp_decrypted_python.txt
rm tmp_encrypted_java.txt
rm tmp_decrypted_java.txt
rm android-java/crypt/*.class