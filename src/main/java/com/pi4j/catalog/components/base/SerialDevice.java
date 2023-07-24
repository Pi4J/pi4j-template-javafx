package com.pi4j.catalog.components.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.function.Consumer;

import com.pi4j.context.Context;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.StopBits;

/**
 *
 */
public class SerialDevice extends Component {
    /**
     * The PI4J Serial
     */
    private final Serial serial;

    private final Consumer<String> onNewData;

    private boolean continueReading = false;

    private Thread serialReaderThread;

    public SerialDevice(Context pi4j, Consumer<String> onNewData){
        serial = pi4j.create(Serial.newConfigBuilder(pi4j)
                .use_9600_N81()
                .dataBits_8()
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE)
                .id("my-serial")
                .device("/dev/ttyS0")
                .build());
        this.onNewData = onNewData;
        //todo: Check if this is really necessary
        serial.open();
        // Wait till the serial port is open
        while (!serial.isOpen()) {
            delay(Duration.ofMillis(250));
        }
    }

    @Override
    public void reset() {
        stopReading();
        serial.close();

        super.reset();
    }

    public void stopReading() {
        continueReading = false;
        serialReaderThread = null;
    }

    public void startReading(){
        if(continueReading){
            return;
        }
        continueReading = true;
        serialReaderThread = new Thread(() -> listenToSerialPort(), "SerialReader");
        serialReaderThread.setDaemon(true);
        serialReaderThread.start();
    }

    private void listenToSerialPort() {
        // We use a buffered reader to handle the data received from the serial port
        BufferedReader br = new BufferedReader(new InputStreamReader(serial.getInputStream()));

        try {
            // Data from the GPS is received in lines
            StringBuilder line = new StringBuilder();

            // Read data until the flag is false
            while (continueReading) {
                // First we need to check if there is data available to read.
                // The read() command for pi-gpio-serial is a NON-BLOCKING call, in contrast to typical java input streams.
                var available = serial.available();
                if (available > 0) {
                    for (int i = 0; i < available; i++) {
                        byte b = (byte) br.read();
                        if (b < 32) {
                            // All non-string bytes are handled as line breaks
                            if (line.length() > 0) {
                                // Here we should add code to parse the data to a GPS data object
                                onNewData.accept(line.toString());
                                line = new StringBuilder();
                            }
                        } else {
                            line.append((char) b);
                        }
                    }
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (Exception e) {
            logException("Error reading data from serial: ", e);
            e.printStackTrace();
        }
    }
}
