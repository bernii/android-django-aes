=========================================================
Java/Android + Python/Django AES encypted communication
=========================================================

This repository hosts project that allows communication between Django/Python and Android/Java application with AES encrypted data (probably through HTTP).

When to use it?
================

When you have need/desire to send AES encrypted data between web app and android device. Fell free to come up with other applications.

Testing:
============

All project modules were roughly tested with unit test and cross-tested with simple bash script that generates files and compares them. To start test simply run test.sh and you should hopefuly see something like that:

>>> ./test.sh
Testing compatibility of created decryption/encryption algorithms between Java and Python
[ OK ] Python encode/decode script
[ OK ] Java encode/decode script
[ OK ] Java and Python compatibility


How to use:
============

Use python lib for your Django project and java lib for Android and send encoded strings through HTTP. Remember to change the secret key from default one!  