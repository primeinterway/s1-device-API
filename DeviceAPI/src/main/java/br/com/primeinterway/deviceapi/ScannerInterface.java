package br.com.primeinterway.deviceapi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.primeinterway.deviceapi.driver.CdcAcmSerialDriver;
import br.com.primeinterway.deviceapi.driver.UsbSerialDriver;
import br.com.primeinterway.deviceapi.driver.UsbSerialPort;
import br.com.primeinterway.deviceapi.util.SerialInputOutputManager;

public class ScannerInterface {

    private static String INTENT_ACTION_GRANT_USB = "com.android.nlscanner.USB_PERMISSION";;

    public static final int Code128 = 0;
    public static final int EAN8 = 1;
    public static final int EAN13 = 2;
    public static final int UPCE = 3;
    public static final int UPCA = 4;
    public static final int Coupon = 5;
    public static final int Interleaved2of5 = 6;
    public static final int Febraban = 7;
    public static final int ITF14 = 8;
    public static final int ITF6 = 9;
    public static final int Matrix2of5 = 10;
    public static final int Code39 = 11;
    public static final int Codabar = 12;
    public static final int Code93 = 13;
    public static final int ChinaPost25 = 14;
    public static final int GS1_128 = 15;
    public static final int GS1_Databar = 16;
    public static final int GS1_Composite = 17;
    public static final int Code11 = 18;
    public static final int ISBN = 19;
    public static final int ISSN = 20;
    public static final int Industrial25 = 21;
    public static final int Standard25 = 22;
    public static final int Plessey = 23;
    public static final int MSI_Plessey = 24;
    public static final int AIM128 = 25;
    public static final int ISBT128 = 26;
    public static final int Code49 = 27;
    public static final int Code16K = 28;
    public static final int PDF417 = 29;
    public static final int MicroPDF417 = 30;
    public static final int QRCode = 31;
    public static final int MicroQRCode = 32;
    public static final int Aztec = 33;
    public static final int DataMatrix = 34;
    public static final int Maxicode = 35;
    public static final int ChineseSensibleCode = 36;
    public static final int GMCode = 37;
    public static final int CodeOne = 38;
    public static final int USPS_Postnet = 39;
    public static final int USPS_Intelligent_Mail = 40;
    public static final int RoyalMail = 41;
    public static final int USPS_Planet = 42;
    public static final int KIXPost = 43;
    public static final int AustralianPostal = 44;
    public static final int SpecificOCR_B = 45;
    public static final int Passport_OCR = 46;
    public static final int ALL1D = 47;
    public static final int ALL2D = 48;
    public static final int ALL = 49;

    protected UsbDevice usb;
    protected UsbManager usbManager;
    protected UsbSerialPort port;

    private Context context;

    protected boolean permissionGranted;
    protected boolean permissionFinished;

    public int timeout;

    protected BroadcastReceiver mUsbReceiver;

    public ScannerInterface(Context context) {
        this.timeout = 50000;
        this.mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(Context rcontext, Intent intent) {
                String action = intent.getAction();
                if ("com.android.nlscanner.USB_PERMISSION".equals(action)) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    if(intent.getBooleanExtra("permission", false)) {
                        if(device != null) {
                            permissionGranted = true;
                        }
                    } else {
                        permissionGranted = false;
                    }
                    permissionFinished = true;
                    rcontext.unregisterReceiver(this);
                }
            }
        };
        this.context = context;
        this.usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
    }

    private String knowSymbology(int i) {
        switch (i) {
            case Code128:
                return "128ENA";
            case EAN8:
                return "EA8ENA";
            case EAN13:
                return "E13ENA";
            case UPCE:
                return "UPEEN0";
            case UPCA:
                return "UPAENA";
            case Coupon:
                return "CPNENA";
            case Interleaved2of5:
                return "I25ENA";
            case Febraban:
                return "I25FBB";
            case ITF14:
                return "I14ENA";
            case ITF6:
                return "IT6ENA";
            case Matrix2of5:
                return "M25ENA";
            case Code39:
                return "C39ENA";
            case Codabar:
                return "CBAENA";
            case Code93:
                return "C93ENA";
            case ChinaPost25:
                return "CHPENA";
            case GS1_128:
                return "GS1ENA";
            case GS1_Databar:
                return "RSSENA";
            case GS1_Composite:
                return "CPTENA";
            case Code11:
                return "C11ENA";
            case ISBN:
                return "ISBENA";
            case ISSN:
                return "ISSENA";
            case Industrial25:
                return "L25ENA";
            case Standard25:
                return "S25ENA";
            case Plessey:
                return "PLYENA";
            case MSI_Plessey:
                return "MSIENA";
            case AIM128:
                return "AIMENA";
            case ISBT128:
                return "IBTENA";
            case Code49:
                return "C49ENA";
            case Code16K:
                return "16KENA";
            case PDF417:
                return "PDFENA";
            case MicroPDF417:
                return "MPDENA";
            case QRCode:
                return "QRCENA";
            case MicroQRCode:
                return "MQRENA";
            case Aztec:
                return "AZTENA";
            case DataMatrix:
                return "DMCENA";
            case Maxicode:
                return "MXCENA";
            case ChineseSensibleCode:
                return "CSCENA";
            case GMCode:
                return "GMCENA";
            case CodeOne:
                return "ONEENA";
            case USPS_Postnet:
                return "PNTENA";
            case USPS_Intelligent_Mail:
                return "ILGENA";
            case RoyalMail:
                return "ROYENA";
            case USPS_Planet:
                return "PLAENA";
            case KIXPost:
                return "KIXENA";
            case AustralianPostal:
                return "APLENA";
            case SpecificOCR_B:
                return "SOBENA";
            case Passport_OCR:
                return "PASENA";
            case ALL1D:
                return "ALL1DC";
            case ALL2D:
                return "ALL2DC";
            case ALL:
                return "ALLENA";
        }
        return null;
    }

    public UsbDevice findScanner(HashMap<String, UsbDevice> usbDevices) {
        Collection<UsbDevice> ite = usbDevices.values();
        UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});

        for (UsbDevice usb : usbs) {
            if (usb.getProductId() == 10758 && usb.getVendorId() == 7851) {
                return usb;
            }
        }
        return null;
    }

    public int init(UsbDevice usb) throws IOException {
        UsbDeviceConnection connection = null;
        if (usb == null) {
            return -5;
        }
        if (this.context == null || this.usbManager == null) {
            return -3;
        }
        if (usb.getProductId() == 10758 && usb.getVendorId() == 7851) {
            UsbSerialDriver driver = new CdcAcmSerialDriver(usb);
            if(driver.getPorts().size() <= 0) {
                return -7;
            }
            this.port = driver.getPorts().get(0);
            requestPermission(usb);
            if(!usbManager.hasPermission(usb)) {
                return -4;
            } else {
                connection = this.usbManager.openDevice(usb);
                this.usb = usb;
            }
        }
        if(connection == null) {
            return -6;
        }
        if(this.port != null) {
            this.port.open(connection);
            this.port.setParameters(19200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            return 0;
        }
        return -1;
    }

    public int close() throws IOException {
        if(this.port == null) {
            return -2;
        }
        if(this.port.isOpen()) {
            this.port.close();
            this.usb = null;
            this.usbManager = null;
            this.context.unregisterReceiver(this.mUsbReceiver);
            this.mUsbReceiver = null;
            this.context = null;
            return 0;
        } else {
            return -1;
        }
    }

    public int getStatus() {
        if(this.port == null) {
            return -2;
        } else {
            return this.port.isOpen() ? 0 : -1;
        }
    }

    public void setSymbology(Boolean status, int symbology) throws RuntimeException {
        if (this.port == null) {
            throw new RuntimeException("A conexão é nula");
        }
        try {

            byte[] stt;
            if (status) {
                stt = "1".getBytes();
            } else {
                stt = "0".getBytes();
            }

            byte[] header = {0x7E, 0x01};
            byte[] body1 = "0000@".getBytes();
            byte[] body2 = knowSymbology(symbology).getBytes();
            byte[] body3 = stt;
            byte[] body4 = ";".getBytes();
            byte[] footer = {0x03};

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(header);
            outputStream.write(body1);
            outputStream.write(body2);
            outputStream.write(body3);
            outputStream.write(body4);
            outputStream.write(footer);

            byte[] data = outputStream.toByteArray();

            this.port.write(data, 2000);

        } catch (Exception e) {
            throw new RuntimeException("Erro de configuração");
        }
    }

    public Boolean isReturnConfig(byte[] data) {
        String string = new String(data);
        for (int i = 0; i < 49; i++) {
            if (string.contains(knowSymbology(i))) {
                return true;
            }
        }
        return false;
    }

    public Boolean getConfigReturn(byte[] data) {
        String string = new String(data);
        for (int i = 0; i < 49; i++) {
            if (string.contains(knowSymbology(i))) {
                byte rcontent = data[data.length - 3];
                if (rcontent != 6) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public int joinListener(SerialInputOutputManager.Listener activity) {
        if(this.port == null) {
            return -2;
        }
        if (activity != null) {
            if (this.port.isOpen()) {
                SerialInputOutputManager usbIoManager = new SerialInputOutputManager(port, activity);
                Executors.newSingleThreadExecutor().submit(usbIoManager);
                return 0;
            } else {
                return -1;
            }
        }
        return -3;
    }

    private boolean requestPermission(UsbDevice usb) {
        if(this.usbManager.hasPermission(usb)) {
            return true;
        }
        this.permissionFinished = false;
        this.permissionGranted = false;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
        IntentFilter intentFilter = new IntentFilter(INTENT_ACTION_GRANT_USB);
        this.context.registerReceiver(mUsbReceiver, intentFilter);
        this.usbManager.requestPermission(usb, pendingIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.add(14, this.timeout);

        while (!this.permissionFinished && !this.usbManager.hasPermission(usb)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                Log.d("Scanner-AAR", "Check the firmware");;
            }
            Calendar calendar1 = Calendar.getInstance();
            if (calendar.before(calendar1))
                break;
        }
        if(this.permissionGranted) {
            while(!this.usbManager.hasPermission(usb)) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    Log.d("Scanner-AAR", "Check the firmware");;
                }
            }
        }
        return this.usbManager.hasPermission(usb);
    }

}
