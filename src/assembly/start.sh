#!/bin/bash
cd "$1"
pkill java
DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority /home/pi/.sdkman/candidates/java/current/bin/java --add-modules javafx.controls --enable-native-access=javafx.controls,javafx.graphics,ALL-UNNAMED -Dsun.java2d.opengl=True -XX:+UseZGC -Xmx1G -jar "$2".jar
exit 0
