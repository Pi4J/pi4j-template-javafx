#!/bin/bash
cd "$1"
pkill java
DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority java --add-modules javafx.controls --enable-native-access=javafx.controls,javafx.graphics,ALL-UNNAMED -Dsun.java2d.opengl=True -XX:+UseZGC -Xmx1G -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar "$2".jar
exit 0
