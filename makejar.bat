@echo off
cd classes
/jdk1.5.0/bin/jar -cf tempest.jar tempest
copy tempest.jar ..