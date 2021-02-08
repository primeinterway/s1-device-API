package br.com.primeinterway.deviceapi.driver;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import androidx.annotation.IntDef;

import java.io.Closeable;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.EnumSet;

public interface UsbSerialPort extends Closeable {

    int DATABITS_5 = 5;
    int DATABITS_6 = 6;
    int DATABITS_7 = 7;
    int DATABITS_8 = 8;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PARITY_NONE, PARITY_ODD, PARITY_EVEN, PARITY_MARK, PARITY_SPACE})
    @interface Parity {}
    int PARITY_NONE = 0;
    int PARITY_ODD = 1;
    int PARITY_EVEN = 2;
    int PARITY_MARK = 3;
    int PARITY_SPACE = 4;

    int STOPBITS_1 = 1;
    int STOPBITS_1_5 = 3;
    int STOPBITS_2 = 2;

    enum ControlLine { RTS, CTS,  DTR, DSR,  CD, RI }

    UsbSerialDriver getDriver();

    UsbDevice getDevice();

    int getPortNumber();

    String getSerial();

    void open(UsbDeviceConnection connection) throws IOException;

    void close() throws IOException;

    int read(final byte[] dest, final int timeout) throws IOException;

    int write(final byte[] src, final int timeout) throws IOException;

    void setParameters(int baudRate, int dataBits, int stopBits, @Parity int parity) throws IOException;

    boolean getCD() throws IOException;

    boolean getCTS() throws IOException;

    boolean getDSR() throws IOException;

    boolean getDTR() throws IOException;

    void setDTR(boolean value) throws IOException;

    boolean getRI() throws IOException;

    boolean getRTS() throws IOException;

    void setRTS(boolean value) throws IOException;

    EnumSet<ControlLine> getControlLines() throws IOException;

    EnumSet<ControlLine> getSupportedControlLines() throws IOException;

    void purgeHwBuffers(boolean purgeWriteBuffers, boolean purgeReadBuffers) throws IOException;

    void setBreak(boolean value) throws IOException;

    boolean isOpen();

}
