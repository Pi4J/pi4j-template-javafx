package com.pi4j.catalog.components.base;

import java.io.IOException;
import java.util.Objects;

import com.pi4j.boardinfo.util.BoardInfoHelper;

import com.fazecast.jSerialComm.SerialPort;

public abstract class SerialActuator extends Component {
    private static final int QUEUE_SIZE = 8192;

    private final SerialPort port;

    protected SerialActuator(int baudRate){
        port = createPort(baudRate);
        openPort(port);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        closePort();
    }

    protected void write(byte[] data) {
        Objects.requireNonNull(data);

        if(BoardInfoHelper.runningOnRaspberryPi()){
            int lastErrorCode = port.getLastErrorCode() ;
            int lastErrorLocation = port.getLastErrorLocation();
            boolean isOpen = port.isOpen();
            if (!isOpen || lastErrorCode != 0) {
                logError("Port is open:" + isOpen + ", last error:" + lastErrorCode + " " + lastErrorLocation);
                openPort(port);
            }
            port.writeBytes(data, data.length);
        }
    }

    private SerialPort createPort(int baudRate){
        if(BoardInfoHelper.runningOnRaspberryPi()){
            SerialPort port = SerialPort.getCommPort(runningOnPi5() ? "/dev/ttyAMA0" : "/dev/ttyS0");
            port.setBaudRate(baudRate);
            port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

            return port;
        }
        else {
            return null;
        }
    }

    private void openPort(SerialPort port){
        if(BoardInfoHelper.runningOnRaspberryPi()){
            port.openPort(0, QUEUE_SIZE, QUEUE_SIZE);
            logInfo("Opening " + port.getDescriptivePortName());
        }
    }

    private void closePort() {
        if(BoardInfoHelper.runningOnRaspberryPi()){
            logDebug("Closing " + port.getDescriptivePortName());
            try {
                // Make sure all pending commands are sent before closing the port
                port.getOutputStream().flush();
            } catch (IOException e) {
                logError("Error while flushing the data: " + e.getMessage());
            }
            port.closePort();
        }
    }

}
