package br.com.primeinterway.deviceapi;

import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.com.primeinterway.deviceapi.driver.CdcAcmSerialDriver;
import br.com.primeinterway.deviceapi.driver.UsbSerialDriver;
import br.com.primeinterway.deviceapi.driver.UsbSerialPort;

public class ScannerInterface {

    private static String INTENT_ACTION_GRANT_USB;

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

    private UsbDevice usb;
    private UsbManager usbManager;
    private UsbSerialPort port;

    private AppCompatActivity activity;

    public ScannerInterface(AppCompatActivity activity, UsbManager manager) {
        this.activity = activity;
        this.usbManager = manager;
    }

    public String knowSymbology(int i) {
        switch(i) {
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

    public UsbSerialPort getUsbPort() {
        if(port != null) {
            return port;
        } else {
            return null;
        }
    }

    public Boolean isReturnConfig(byte[] data) {
        String string = new String(data);
        if(string.contains("ALGUM CARACTER DE RETORNO DE CONFIG")) {
            return true;
        }
        return false;
    }

    // 4 = Falha de permissão do dispositivo
    // 3 = Manager não configurado
    // 2 = Scanner não encontrado
    // -1 = Falha de comunicação USB
    // 0 = OK
    public int init(UsbDevice usb) throws IOException {
        if(this.activity == null || this.usbManager == null) {
            return 3;
        }
        if(usb.getManufacturerName() != null && usb.getManufacturerName().contains("Newland")) {
            INTENT_ACTION_GRANT_USB = activity.getApplicationContext().getPackageName() + ".GRANT_USB";
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
            usbManager.requestPermission(usb, usbPermissionIntent);
            if(usbManager.hasPermission(usb)) {
                this.usb = usb;
            } else {
                return 4;
            }
        } else {
            return 2;
        }
        UsbSerialDriver driver = new CdcAcmSerialDriver(usb);
        UsbDeviceConnection connection = usbManager.openDevice(this.usb);
        if(connection == null) {
            return -1;
        }
        port = driver.getPorts().get(0);
        port.open(connection);
        port.setParameters(19200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

        if(port.isOpen()) {
            return 0;
        } else {
            return -1;
        }
    }

    public void setSymbology(Boolean status, int symbology) throws RuntimeException {
        if(port == null) {
            throw new RuntimeException("A conexão é nula");
        }
        try {

            byte[] stt;
            if(status) {
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

            port.write(data, 2000);

        } catch (Exception e) {
            throw new RuntimeException("Erro de configuração");
        }
    }

}
