#!/bin/bash
cd "$1"
pkill java
DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority java --module-path "./distribution/openjfx/lib" --add-modules javafx.controls -Dsun.java2d.opengl=True -XX:+UseZGC -Xmx1G -jar "$2".jar
exit 0
