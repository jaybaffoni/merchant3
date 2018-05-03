package becustomapps.com.merchant3.Utilities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Punch;

/**
 * Created by Jay on 4/4/2018.
 */

public class Utility {

    private Utility() {
        super();
    }

    public static void transmit(Context ctx, Transmittable trans, Punch punch, DataSource datasource, SharedPreferences prefs){
        TransmitTask tt = new TransmitTask(ctx, trans, punch, datasource, prefs);
        tt.execute();
    }

    static class TransmitTask extends AsyncTask<Void, Void, Integer> {

        Context ctx;
        Punch punch;
        DataSource datasource;
        SharedPreferences prefs;
        ProgressDialog loader;
        Transmittable trans;

        public TransmitTask(Context ctx, Transmittable trans, Punch punch, DataSource datasource, SharedPreferences prefs){
            this.ctx = ctx;
            this.punch = punch;
            this.datasource = datasource;
            this.prefs = prefs;
            this.trans = trans;
            loader = new ProgressDialog(ctx);
            loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        @Override
        protected void onPreExecute(){
            loader.setMessage("Uploading...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            //ToDo get shift punch

            String emp_id = prefs.getString("emp_id", "");
            String filename = "upld." + emp_id + "-" + (String) DateFormat.format("yyyyMMddkkmmss", new Date().getTime()) + ".dat";

            File upld = new File(ctx.getFilesDir(), filename);

            String transmission = "";
            transmission = transmission + emp_id + "," + "DayRcd," + punch.toString() + "\r\n";
            transmission = transmission + emp_id + ",MODLRC," + getDeviceInfo(ctx) + "\r\n";
            transmission = transmission + emp_id + ",CustSv," + punch.toStringForTransmit() + prefs.getString("sales_manager", "") + "\r\n";

            if(punch.getType().equals("MORNIGN")){
                List<Authcode> authcodes = datasource.getAuthcodesWithPID(punch.getPunch_id());
                for(Authcode ac: authcodes){
                    transmission = transmission + emp_id + ",ProdRc," + ac.toString() + "\r\n";
                }
            }

            transmission = transmission + emp_id + "," + "EndRcd" + "\r\n";

            writeToFile(ctx, upld, transmission);

            int toReturn = 0;
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();

            try {
                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                session.connect(10000);

                channel = session.openChannel("sftp");
                channel.connect(10000);
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                try {
                    sftpChannel.put(new FileInputStream(upld), upld.getName());
                } catch (SftpException e) {
                    e.printStackTrace();
                    toReturn = 1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    toReturn = 2;
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                e.printStackTrace();
                Log.e("TAG", "didn't even connect");
                toReturn = 3;
            }

            return toReturn;

            //ToDo verify file
            //ToDo check for updates
        }

        @Override
        protected void onPostExecute(Integer result) {
            loader.dismiss();
            Log.e("UPLD", "complete");
            Log.e("UPLD", result + "");
            trans.onTransmitComplete(false);
        }
    }

    public static void cancelService(Context ctx, Transmittable trans, Punch thisPunch, DataSource datasource){
        //ToDo Start loader
        List<Authcode> acs = datasource.getAuthcodesWithPID(thisPunch.getPunch_id());
        for(Authcode ac: acs){
            datasource.deleteAuthcode(ac);
        }
        datasource.deletePunch(thisPunch);
        //ToDo stop loader
        trans.onTransmitComplete(true);
    }

    public static void writeToFile(Context ctx, File file, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput(file.getName(), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String getDeviceInfo(Context ctx){
        return "\"" + getModel() + "\"" + "," + getSerial() + "," + getIMEI(ctx) + "," + getMDN(ctx) + "," + getVersion(ctx) + "," + Build.VERSION.RELEASE;
    }

    public static String getIMEI(Context ctx){
        TelephonyManager tManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tManager.getDeviceId();

        if(IMEI == null){
            IMEI = "0";
        }

        return IMEI;
    }

    public static String getMDN(Context ctx){
        TelephonyManager tManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String MDN = tManager.getLine1Number();

        if(MDN.equals("")){
            MDN = "0";
        }

        return MDN;
    }

    public static String getSerial(){
        String Serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            Serial = (String) get.invoke(c, "ril.serialnumber", "unknown");
        } catch (Exception ignored) {}

        if(Serial == null || Serial.equals("")){
            Serial = "0";
        }

        return Serial;
    }

    public static String getModel(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getVersion(Context ctx){
        String v = "";
        try {
            v = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //Log.e("tag", e.getMessage());
        }
        return v;
    }

    public static void alert(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }
}
