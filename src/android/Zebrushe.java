package imb.ridiqirici.plugin.cordova.zebra;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;


public class Zebrushe extends CordovaPlugin {
    
    public static final String PRINT_TEXT = "printText";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (PRINT_TEXT.equals(action)) {
            String macaddress = args.getString(0);
            String label = args.getString(1);
            this.printText(macaddress, label, callbackContext);
            return true;
        }
        return false;
    }

    private void printText(String macaddress, String label, CallbackContext callbackContext) {
        Connection connection =  new BluetoothConnection(macaddress);

        try {
                connection.open();
                com.zebra.sdk.printer.ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);             
                printer.printStoredFormat(label, new HashMap<Integer, String>(), "utf8");
                connection.close();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection.isConnected()){
                    connection.close();
                }   
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }
}