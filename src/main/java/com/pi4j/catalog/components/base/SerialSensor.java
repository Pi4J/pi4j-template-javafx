package com.pi4j.catalog.components.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Consumer;

import com.pi4j.boardinfo.util.BoardInfoHelper;

import com.fazecast.jSerialComm.SerialPort;

/**
 * The SerialSensor class represents a hardware component capable of reading data
 * from a serial port. It allows continuous reading of data lines and provides callback
 * functionality to handle incoming data. This class is especially designed for environments
 * running on Raspberry Pi.
 *
 * You can check the status of your Raspberry Pi's serial port with
 * 'jbang https://github.com/pi4j/pi4j-os/blob/main/iochecks/IOChecker.java serial'
 *
 * Extends the Component class to inherit logging and resource cleanup capabilities.
 */
public class SerialSensor extends Component {
    private final SerialPort port;
    private boolean continueReading = false;

    /**
     *
     * @param baudRate the baud rate that's needed by the serial device
     * @param portDescriptor something like "/dev/ttyAMA0" or "/dev/ttyS0"
     */
    public SerialSensor(int baudRate, String portDescriptor) {
        port = createPort(baudRate, portDescriptor);
        logDebug("Created new SerialSensor component on port %s with baud rate %d", portDescriptor, baudRate);
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
        if (BoardInfoHelper.runningOnRaspberryPi()) {
            stopReading();
            port.closePort();
        }
        super.shutdown();
    }

    private SerialPort createPort(int baudRate, String portDescriptor) {
        if (BoardInfoHelper.runningOnRaspberryPi()) {
            SerialPort port = SerialPort.getCommPort(portDescriptor);
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); //no read timeout
            port.setComPortParameters(baudRate, 8, 1, SerialPort.NO_PARITY);     // Set baud rate, data bits, stop bits, and parity

            return port;
        } else {
            return null;
        }
    }

    /**
     * Continuously reads data from a given serial port and processes each line using the provided callback function.
     * This method starts a new thread for reading data from the serial port.
     *
     * @param port the serial port to read data from
     * @param onNewLine a callback function to process each line of data read from the port
     */
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
