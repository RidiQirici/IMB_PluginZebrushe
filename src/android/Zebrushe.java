package imb.ridiqirici.plugin.cordova.zebra;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import android.graphics.BitmapFactory;
import android.os.Environment;


public class Zebrushe extends CordovaPlugin {
    
    public static final String PRINT_TEXT = "printText";
    public static final String PRINT_IMAGE = "printImage";
    public static final String GET_LOCATION = "getLocation";
    private static File file = null;
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (PRINT_TEXT.equals(action)) {
            String macaddress = args.getString(0);
            String label = args.getString(1);
            this.printText(macaddress, label, callbackContext);
            return true;
        }
        else if (PRINT_IMAGE.equals(action)) {
            String macaddress = args.getString(0);
            String pathi = args.getString(1);
            int x = args.getInt(2);
            int y = args.getInt(3);
            int w = args.getInt(4);
            int h = args.getInt(5);
            this.printImage(macaddress, pathi, x, y, w, h, callbackContext);
            return true;
        }
        else if (GET_LOCATION.equals(action)) {
            this.getLocation(callbackContext);
            return true;
        }
        return false;
    }

    private void printText(String macaddress, String label, CallbackContext callbackContext) {
        Connection connection =  new BluetoothConnection(macaddress);

        try {
                connection.open();
                //com.zebra.sdk.printer.ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);   
                //printer.sendCommand("! U1 setvar 'device.languages' 'line_print'");
                connection.write(label.getBytes());
                //printer.printStoredFormat(label, new HashMap<Integer, String>(), "utf8");
                connection.close();
               
        } catch (ConnectionException e) {
            e.printStackTrace();
            callbackContext.error(e.toString());
        } finally {
            try {
                if(connection.isConnected()){
                    connection.close();
                }   
                callbackContext.success("Printimi i tekstit u krye me sukses! " + Environment.getExternalStorageDirectory() + " " + Environment.getRootDirectory());
            } catch (ConnectionException e) {
                e.printStackTrace();
                callbackContext.error(e.toString());
            }
        }
    }
    
    private void printImage(String macaddress, String pathi, int x, int y, int w, int h, CallbackContext callbackContext) {
        Connection connection =  new BluetoothConnection(macaddress);

        try {
                connection.open();
                com.zebra.sdk.printer.ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection); 
                file = new File(Environment.getExternalStorageDirectory(), pathi);
                printer.sendCommand("~jc^xa^jus^xz\r\n");
                printer.printImage(new ZebraImageAndroid(BitmapFactory.decodeFile(file.getAbsolutePath())), x, y, w, h, false);
                connection.close();

        } catch (ConnectionException e) {
            e.printStackTrace();
            callbackContext.error(e.toString());
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
            callbackContext.error(e.toString());
       } finally {
            try {
                if(connection.isConnected()){
                    connection.close();
                }   
                callbackContext.success("Printimi i tekstit u krye me sukses!");
            } catch (ConnectionException e) {
                e.printStackTrace();
                callbackContext.error(e.toString());
            }
        }
    }
    
    private void getLocation(CallbackContext callbackContext){
    	try {
    		callbackContext.success(Zebrushe.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			callbackContext.error(e.toString());
		}
    	    	
    }
}