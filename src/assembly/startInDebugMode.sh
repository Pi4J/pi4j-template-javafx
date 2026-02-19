#!/bin/bash
cd "$1"
pkill java
DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority /home/pi/.sdkman/candidates/java/current/bin/java -XX:+UseZGC -Xmx1G -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar "$2".jar
exit 0
