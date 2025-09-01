package com.pi4j.catalog.components.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Consumer;

import com.pi4j.boardinfo.util.BoardInfoHelper;

import com.fazecast.jSerialComm.SerialPort;

public class SerialSensor extends Component {
    private final SerialPort port;
    private boolean continueReading = false;

    public SerialSensor(int baudRate) {
        port = createPort(baudRate);
    }

    public void startReading(Consumer<String> onNewLine){
        Objects.requireNonNull(onNewLine);
        continueReading = false; //stop the current reading, if any

        readFromPort(port, onNewLine);
    }

    public void stopReading(){
        continueReading = false;
    }

    public void shutdown() {
        super.shutdown();
        if (BoardInfoHelper.runningOnRaspberryPi()) {
            stopReading();
            port.closePort();
        }
    }

    private SerialPort createPort(int baudRate) {
        if (BoardInfoHelper.runningOnRaspberryPi()) {
            SerialPort port = SerialPort.getCommPort(runningOnPi5() ? "/dev/ttyAMA0" : "/dev/ttyS0");;
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); //no read timeout
            port.setComPortParameters(baudRate, 8, 1, SerialPort.NO_PARITY);     // Set baud rate, data bits, stop bits, and parity

            return port;
        } else {
            return null;
        }
    }

    private void readFromPort(SerialPort port, Consumer<String> onNewLine) {
        if (BoardInfoHelper.runningOnRaspberryPi()) {
            new Thread(() -> {
                continueReading = true;
                port.openPort();
                // Set up an input stream to read from the serial port
                try (BufferedReader input = new BufferedReader(new InputStreamReader(port.getInputStream()))) {
                    String line;
                    // Continuously read until the port is closed
                    while (continueReading && (line = input.readLine()) != null) {
                        logDebug(line);
                        onNewLine.accept(line);
                    }
                } catch (Exception e) {
                    logException("Reading from Serial Port throws ", e);
                } finally {
                    // Always ensure the port is closed after use
                    port.closePort();
                }
            }).start();
        }
    }

}
