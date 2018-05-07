package becustomapps.com.merchant3.Activities;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.OrderCustomer;
import becustomapps.com.merchant3.Objects.OrderProduct;
import becustomapps.com.merchant3.Objects.Product;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Utility;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";
    SharedPreferences prefs;

    DataSource datasource;

    ProgressDialog loader;

    File downFile, midwFile, updtFile, shipFile, orddFile;
    File fileToProcess;

    String loginState = "down";

    ArrayList<Customer> customersToAdd = new ArrayList<Customer>();
    ArrayList<Product> productsToAdd = new ArrayList<Product>();
    ArrayList<Authcode> authcodesToAdd = new ArrayList<Authcode>();
    ArrayList<OrderCustomer> orderCustomersToAdd = new ArrayList<OrderCustomer>();
    ArrayList<OrderProduct> orderProductsToAdd = new ArrayList<OrderProduct>();

    String orddFileName;
    ArrayList<String> shipFileNames = new ArrayList<String>();

    boolean renameMidw, renameUpdt, renameShip, renameOrdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            boolean force_logout = false;
            force_logout = extras.getBoolean("force_logout");

            if(force_logout){
                Utility.alert(this, "Logged out due to inactivity");
            }
        }

        getSupportActionBar().setTitle("Log In");

        Button exitButton = (Button)findViewById(R.id.backButton);
        exitButton.setText("Exit");
        Button setupButton = (Button)findViewById(R.id.middleButton);
        setupButton.setText("Setup");
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        datasource = new DataSource(this);
        datasource.open();

        Boolean firstRun = prefs.getBoolean("firstRun", true);
        if(firstRun){
            executeSetup();
        }

        loader = new ProgressDialog(this);
        loader.setCancelable(false);
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        downFile = new File(getFilesDir(), "downfile.txt");
        midwFile = new File(getFilesDir(), "midwfile.txt");
        updtFile = new File(getFilesDir(), "updtfile.txt");
        shipFile = new File(getFilesDir(), "shipfile.txt");
        orddFile = new File(getFilesDir(), "orddfile.txt");

        loadWelcomeText();

        checkUser();

        renameMidw = renameUpdt = renameShip = renameOrdd = false;

    }

    public void loginPressed(View view){
        Log.e(TAG, "Login button pressed");
        checkCompletion(null);

    }

    public void checkCompletion(View view){
        if(prefs.getString("emp_id", "").equals("")){
            checkUser();
        } else {
            establishConnection();
        }
    }

    public void establishConnection(){
        Log.e(TAG, "Establishing connection");
        ScooterTask scootertask = new ScooterTask();
        scootertask.execute();
    }

    private class ScooterTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute(){
            loader.setMessage("Connecting...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            int toReturn = 0;
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();
            try {

                Log.e("SERVER", prefs.getString("server_username", ""));
                Log.e("SERVER", prefs.getString("server_address", ""));
                Log.e("SERVER", prefs.getInt("server_port", 0) + "");
                Log.e("SERVER", prefs.getString("server_password", ""));

                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                Log.e("CONNECT", "session connecting...");
                session.connect(10000);
                Log.e("CONNECT", "session connected");
                channel = session.openChannel("sftp");
                Log.e("CONNECT", "channel connecting...");
                channel.connect(10000);
                Log.e("CONNECT", "channel connected");
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                try {
                    sftpChannel.get("scooter.dat");
                    toReturn = 1;

                } catch (SftpException e) {
                    toReturn = 2;
                    return toReturn;
                    //e.printStackTrace();
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                toReturn = 0;
                e.printStackTrace();
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //loader.dismiss();
            finishScooter(result);
        }
    }

    public void finishScooter(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                //jschException
                Log.e(TAG, "Server error looking for scooter file");
                if(canLoginOffline()){
                    alertDialogBuilder
                            .setMessage("Communication Error: No Connection. Continue trying to login as " + prefs.getString("emp_id", "") + "?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            checkForDownFile();
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            if(loader.isShowing()){
                                                loader.dismiss();
                                            }
                                        }
                                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    alertDialogBuilder
                            .setMessage("Communication Error: No Connection")
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            if(loader.isShowing()){
                                                loader.dismiss();
                                            }
                                        }
                                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case 1:
                //worked
                Log.e(TAG, "Scooter File found");
                checkForDownFile();
                break;
            case 2:
                //scooter file not found
                Log.e(TAG, "Scooter file not found");
                alertDialogBuilder
                        .setMessage("Communication Error:" + "\n" + "Control file not found")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loader.isShowing()){
                                            loader.dismiss();
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    public void checkForDownFile(){
        if(downFileNeeded()){
            prefs.edit().putInt("downfile_loaded", 0).apply();
            Log.e(TAG, "Checking for down file");
            loginState = "down";
            DownloadTask downloadTask = new DownloadTask("down");
            downloadTask.execute();
        }
        else{
            checkForMidWeekFile();
        }
    }

    public void checkForMidWeekFile(){
        prefs.edit().putInt("downfile_loaded", 1).apply();
        Log.e(TAG, "Checking for midw file");
        loginState = "midw";
        DownloadTask downloadTask = new DownloadTask("midw");
        downloadTask.execute();
    }

    public void checkForUpdateFile(){
        Log.e(TAG, "Checking for updt file");
        loginState = "updt";
        DownloadTask downloadTask = new DownloadTask("updt");
        downloadTask.execute();
    }

    public void checkForShipmentFiles(){
        Log.e(TAG, "Checking for ship files");
        loginState = "ship";
        ShipmentTask shipmentTask = new ShipmentTask();
        shipmentTask.execute();
    }

    class ShipmentTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute(){
            loader.setMessage("Updating Shipping Data...");
            loader.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String toReturn = "failed";
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();

            String id = prefs.getString("emp_id", "");
            String fileStart = "ship." + id;


            try {
                shipFileNames.clear();
                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                session.connect(10000);

                channel = session.openChannel("sftp");
                channel.connect(10000);
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                toReturn = "nouser";
                try {
                    OutputStream output = new FileOutputStream(shipFile);
                    Vector<ChannelSftp.LsEntry> entries = sftpChannel.ls("*.*");
                    for (ChannelSftp.LsEntry entry : entries) {
                        if(entry.getFilename().toLowerCase().startsWith(fileStart.toLowerCase()) && entry.getFilename().length() == 22)
                        {
                            //if(checkShipDate(entry.getFilename().substring(12,18))){
                            shipFileNames.add(entry.getFilename());
                            //}

                        }
                    }
                    for(String s: shipFileNames){
                        sftpChannel.get(s, output);

                    }
                    if(shipFileNames.size() > 0){
                        renameShip = true;
                        toReturn = "worked";
                    }


                } catch (SftpException e) {
                    toReturn = "nouser";
                    return toReturn;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return toReturn;
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                toReturn = "failed";
                e.printStackTrace();
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(String result) {
            //loader.dismiss();
            finishShipments(result);
        }
    }

    public void finishShipments(String result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case "worked":
                ProcessShippingTask pst = new ProcessShippingTask();
                pst.execute();
                break;
            case "nouser":
                checkForOrderFiles();
                break;
            case "failed":
                alertDialogBuilder
                        .setMessage("Communication Error: Could not check for shipping data")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        checkForOrderFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    public void checkForOrderFiles(){
        Log.e(TAG, "Checking for ordd files");
        loginState = "ordd";
        OrderTask ot = new OrderTask();
        ot.execute();

    }

    private class OrderTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute(){
            loader.setMessage("Downloading...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            int toReturn = 0;
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();

            Date date = new Date();
            String today = (String)DateFormat.format("yyMMdd", date.getTime());
            String id = prefs.getString("emp_id", "");
            orddFileName = "ordd." + id + "." + today + ".dat";

            try {
                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                session.connect(10000);

                channel = session.openChannel("sftp");
                channel.connect(10000);
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                try {
                    OutputStream downFileOutput = new FileOutputStream(orddFile);
                    sftpChannel.get(orddFileName, downFileOutput);
                    toReturn = 2;
                    renameOrdd = true;

                } catch (SftpException e) {
                    toReturn = 1;
                    return toReturn;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return 0;
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                e.printStackTrace();
                return 0;
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //loader.dismiss();
            finishOrderTask(result);
        }
    }

    public void finishOrderTask(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                alertDialogBuilder
                        .setMessage("Communication Error: Could not check for order changes")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        renameFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 1:
                renameFiles();
                break;
            case 2:
                ProcessOrderTask pot = new ProcessOrderTask();
                pot.execute();
                break;
        }
    }

    public class ProcessOrderTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setMessage("Processing...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0){

            int verificationCode = verifyOrderFile();
            return verificationCode;

        }

        @Override
        protected void onPostExecute(Integer str){
            //loader.dismiss();
            finishOrderProcessing(str);
        }
    }

    public void finishOrderProcessing(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                alertDialogBuilder
                        .setMessage("The " + loginState + " file size is Corrupt.  Contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        renameFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 1:
                //date mismatch
                alertDialogBuilder
                        .setMessage("Dates in Order file do not match Tablet. Contact support.")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        renameFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 2:
                //user mismatch
                alertDialogBuilder
                        .setMessage("Mismatch of merchandiser id on ordd file name to internal id. Contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        renameFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 3:
                //chrctc mismatch
                alertDialogBuilder
                        .setMessage("Error processing order change information")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        renameFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 4:
                renameFiles();
                break;
        }
    }

    public int verifyOrderFile(){
        ArrayList<String> linesToProcess = new ArrayList<String>();
        //Checks for inconsistencies with down, updt, or midw files
        if(orddFile.length() == 0){
            Log.e(TAG, "File size is zero");
            return 0;
        }
        linesToProcess.clear();
        orderCustomersToAdd.clear();
        orderProductsToAdd.clear();
        try {
            String line = "";
            InputStream is = new FileInputStream(orddFile.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while(!(line = reader.readLine()).toLowerCase().startsWith("endrcd")){
                linesToProcess.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        int checkRecordCount = 0;
        int actualRecordCount = 1;

        if(linesToProcess.size() == 0){
            return 0;
        }
        for(String line: linesToProcess){
            if(line.length() > 5){
                if(line.startsWith("PARMDW")){
                    actualRecordCount++;

                    Date d1 = new Date();
                    String today = (String)DateFormat.format("MMddyyyy", d1.getTime());
                    //alert(today);
                    if(!line.substring(12,20).equals(today)){
                        return 1;
                    }
                    if(!line.substring(6,12).equalsIgnoreCase(prefs.getString("emp_id", ""))){
                        return 2;
                    }

                    String todaysdate = line.substring(12,20);
                    String orderdate = line.substring(25,33);
                    String cutoffTime = line.substring(21,25);
                    String days = line.substring(33, 54);
                    String dates = line.substring(54,96);
                    char timer = line.charAt(96);
                    char suggested = line.charAt(97);

                    prefs.edit().putString("order_date", orderdate).apply();
                    prefs.edit().putString("order_timeout", cutoffTime).apply();
                    prefs.edit().putString("order_days", days).apply();
                    prefs.edit().putString("order_dates", dates).apply();

                    String cutoff = todaysdate.substring(4) + todaysdate.substring(0,4) + cutoffTime;
                    prefs.edit().putString("order_cutoff", cutoff).apply();

                    if(timer == 'Y'){
                        prefs.edit().putBoolean("ignoretimer", true).apply();
                    } else {
                        prefs.edit().putBoolean("ignoretimer", false).apply();
                    }
                    if(suggested == 'N'){
                        prefs.edit().putBoolean("ignoresuggested", true).apply();
                    } else {
                        prefs.edit().putBoolean("ignoresuggested", false).apply();
                    }

                } else if (line.startsWith("CUSTDW")) {
                    actualRecordCount++;
                    String acct = line.substring(6, 14);
                    String edlp = line.substring(14);
                    String days = prefs.getString("order_days", "");
                    String dates = prefs.getString("order_dates", "");
                    String orderdate = prefs.getString("order_date", "");

                    if(!datasource.orderCustomerExists(acct)){
                        datasource.createOrderCustomer(acct, days, dates, edlp, orderdate);
                    }
                    //Log.e("CUSTOMER", temp.toString());
                } else if (line.startsWith("PRODDW")) {
                    actualRecordCount++;
                    String custno = line.substring(6, 14);
                    String prodno = line.substring(14, 20);
                    String onhand = line.substring(20, 24);
                    String gamble = line.substring(24, 27);
                    String gambletype = line.substring(27, 31);
                    String facing = line.substring(31, 35);
                    String prevorderadj = line.substring(35, 70);
                    String shipadj = line.substring(70, 105);
                    String loadadjtotal = line.substring(105, 110);
                    String avgsold = line.substring(110, 138);
                    String prevweeksold = line.substring(138, 166);
                    String currentorder = line.substring(166, 194);
                    String dailyproductavailable = line.substring(194, 201);
                    String availtoorder = line.substring(201, 208);
                    String locked = line.substring(208);
                    String suggested = "";
                    String finalorder = "";
                    int touched = 0;
                    int transmitted = 0;
                    if(!datasource.orderProductExists(custno, prodno)){
                        datasource.createOrderProduct(custno, prodno, onhand, gamble, gambletype, facing,
                                prevorderadj, shipadj, loadadjtotal, avgsold, prevweeksold, currentorder, dailyproductavailable,
                                availtoorder, locked, suggested, finalorder, "", "", "", "", "", "", touched, transmitted);
                    }
                    //Log.e("ORDER", "product");
                } else if(line.startsWith("CKRCTC")){
                    actualRecordCount++;
                    String cff = "";
                    cff += Character.toString(line.charAt(6));
                    cff += Character.toString(line.charAt(7));
                    cff += Character.toString(line.charAt(8));
                    cff += Character.toString(line.charAt(9));
                    cff += Character.toString(line.charAt(10));
                    checkRecordCount = Integer.parseInt(cff);

                }
            }

        }
        if(checkRecordCount != actualRecordCount){
            Log.e(TAG, "Ckrctc: " + checkRecordCount + " Actual: " + actualRecordCount);
            return 3;
        }
        return 4;
    }

    public void renameFiles(){
        RenameTask rt = new RenameTask();
        rt.execute();
    }

    class RenameTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
            loader.show();
            loader.setMessage("Finishing...");
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String toReturn = "failed";
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();

            String id = prefs.getString("emp_id", "");
            String midwFileName = "midw." + id + ".dat";
            String updtFileName = "updt." + id + ".dat";

            Date d3 = new Date();
            String s3  = (String)DateFormat.format("yyyyMMddkkmmss", d3.getTime());
            try {
                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                session.connect(10000);

                channel = session.openChannel("sftp");
                channel.connect(10000);
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                try {
                    if(renameMidw){
                        Log.e("RENAME", "MIDW");
                        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("midw.*");
                        for(ChannelSftp.LsEntry entry : list) {
                            Log.e("FILE", entry.getFilename());
                            if(entry.getFilename().equalsIgnoreCase(midwFileName)){
                                sftpChannel.rename(entry.getFilename(), midwFileName.substring(0,11) + "-" + s3 + ".dat");
                            }
                        }
                        renameMidw = false;

                    }
                    if(renameUpdt){
                        Log.e("RENAME", "UPDT");
                        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("updt.*");
                        for(ChannelSftp.LsEntry entry : list) {
                            Log.e("FILE", entry.getFilename());
                            if(entry.getFilename().equalsIgnoreCase(updtFileName)){
                                sftpChannel.rename(entry.getFilename(), updtFileName.substring(0,11) + "-" + s3 + ".dat");
                            }
                        }
                        renameUpdt = false;
                    }
                    if(renameShip){
                        Log.e("RENAME", "SHIP");
                        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("ship.*");
                        for(String s: shipFileNames){

                            Log.e("SHIP", s);
                            for(ChannelSftp.LsEntry entry: list){
                                Log.e("FILE", entry.getFilename());
                                if(entry.getFilename().equalsIgnoreCase(s)){
                                    Log.e("MATCH", "winner");
                                    String fn = entry.getFilename();
                                    sftpChannel.rename(fn, (s.substring(0,18) + "-" + s3 + ".dat"));
                                }
                            }

                        }
                        renameShip = false;

                    }
                    if(renameOrdd){
                        Log.e("RENAME", "ORDD");
                        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("ordd.*");
                        for(ChannelSftp.LsEntry entry : list) {
                            Log.e("FILE", entry.getFilename());
                            if(entry.getFilename().equalsIgnoreCase(orddFileName)){
                                sftpChannel.rename(entry.getFilename(), orddFileName.substring(0,11) + "-" + s3 + ".dat");
                            }
                        }
                        renameOrdd = false;
                    }

                    toReturn = "worked";

                } catch (SftpException e) {
                    e.printStackTrace();
                    toReturn = "nouser";
                    Log.e("RENAME", e.toString());
                    return toReturn;
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                toReturn = "failed";
                Log.e("RENAME", "FAILED");
                e.printStackTrace();
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(String result) {
            //loader.dismiss();
            Log.e("RENAME", result);
            finishAndLogin();
        }
    }

    public void finishAndLogin(){

        //ToDo check about new day
        goToMainMenu();
    }

    public void goToMainMenu(){

        if(loader.isShowing()){
            loader.dismiss();
        }

        Log.e(TAG, "Going to main menu");
        prefs.edit().putString("last_user", prefs.getString("emp_id", "")).apply();
        Date d2 = new Date();
        String todaysDate  = (String)DateFormat.format("yyyyMMdd", d2.getTime());
        prefs.edit().putString("last_login_date", todaysDate).apply();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    class DownloadTask extends AsyncTask<Void, Void, Integer> {

        String fileType = "down";

        public DownloadTask(String fileType){
            this.fileType = fileType;
        }

        @Override
        protected void onPreExecute(){
            loader.setMessage("Downloading...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            int toReturn = 0;
            Session session = null;
            Channel channel = null;
            JSch jsch = new JSch();

            String id = prefs.getString("emp_id", "");
            String fileName = fileType + "." + id + ".dat";

            File temp;
            if(loginState.equals("down")){
                temp = downFile;
            } else if(loginState.equals("midw")){
                temp = midwFile;
            } else {
                temp = updtFile;
            }

            try {
                session = jsch.getSession(prefs.getString("server_username", ""), prefs.getString("server_address", ""), prefs.getInt("server_port", 0));
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(prefs.getString("server_password", ""));
                session.connect(10000);

                channel = session.openChannel("sftp");
                channel.connect(10000);
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                try {
                    OutputStream downFileOutput = new FileOutputStream(temp);
                    sftpChannel.get(fileName, downFileOutput);
                    toReturn = 1;
                    if(loginState.equals("midw")){
                        renameMidw = true;
                    } else if(loginState.equals("updt")){
                        renameUpdt = true;
                    }

                } catch (SftpException e) {
                    toReturn = 2;
                    return toReturn;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return toReturn;
                }

                sftpChannel.exit();
                session.disconnect();

            }
            catch (JSchException e) {
                toReturn = 0;
                e.printStackTrace();
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //loader.dismiss();
            finishDownload(result);
        }
    }

    public void finishDownload(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                //jschException
                Log.e(TAG, "Server error looking for down file");
                if(canLoginOffline()){
                    alertDialogBuilder
                            .setMessage("Communication Error: No Connection")
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            if(loginState.equals("midw")){
                                                checkForUpdateFile();
                                            } else if(loginState.equals("updt")){
                                                checkForShipmentFiles();
                                            }
                                            dialog.cancel();
                                        }
                                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case 1:
                //worked
                Log.e(TAG, loginState + " file loaded");
                if(loginState.equals("down")){
                    fileToProcess = downFile;
                } else if(loginState.equals("midw")){
                    fileToProcess = midwFile;
                } else {
                    fileToProcess = updtFile;
                }

                ProcessTask pt = new ProcessTask();
                pt.execute();
                break;
            case 2:
                //no user
                Log.e(TAG, "No " + loginState + " file on server for user");
                if(loginState.equals("down")){
                    alertDialogBuilder
                            .setMessage("No file on server for user")
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                            if(loader.isShowing()){
                                                loader.dismiss();
                                            }
                                        }
                                    });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if(loginState.equals("midw")){
                    checkForUpdateFile();
                } else {
                    checkForShipmentFiles();
                }

                break;
        }

    }

    public class ProcessTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setMessage("Processing...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0){

            int verificationCode = verifyFile(fileToProcess);
            if(verificationCode == 5){
                return addItems();
            } else {
                return verificationCode;
            }

        }

        @Override
        protected void onPostExecute(Integer str){
            //loader.dismiss();
            finishProcessing(str);
        }
    }

    public void finishProcessing(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                alertDialogBuilder
                        .setMessage("The " + loginState + " file size is Corrupt.  Contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loginState.equals("midw")){
                                            checkForUpdateFile();
                                        } else if(loginState.equals("updt")){
                                            checkForShipmentFiles();
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 1:
                alertDialogBuilder
                        .setMessage("Dates in " + loginState + " file do not match current week. Contact support.")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loginState.equals("midw")){
                                            checkForUpdateFile();
                                        } else if(loginState.equals("updt")){
                                            checkForShipmentFiles();
                                        } else {
                                            if(loader.isShowing()){
                                                loader.dismiss();
                                            }
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 2:
                alertDialogBuilder
                        .setMessage("Mismatch of merchandiser id on file name to " + loginState + " internal id. Contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loginState.equals("midw")){
                                            checkForUpdateFile();
                                        } else if(loginState.equals("updt")){
                                            checkForShipmentFiles();
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 3:
                alertDialogBuilder
                        //.setMessage("Customer or Product data error in download. Please try logging in again. If the error persists, contact support.")
                        .setMessage("Customer or Product count data error with " + loginState + "file. If error persists, contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loginState.equals("midw")){
                                            checkForUpdateFile();
                                        } else if(loginState.equals("updt")){
                                            checkForShipmentFiles();
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 4:
                alertDialogBuilder
                        .setMessage("Customer or Product data error in " + loginState + "file. If the error persists, contact support. Continue logging in?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        if(loginState.equals("midw")){
                                            checkForUpdateFile();
                                        } else if(loginState.equals("updt")){
                                            checkForShipmentFiles();
                                        }
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 5:
                if(loginState.equals("down")){
                    checkForMidWeekFile();
                } else if(loginState.equals("midw")){
                    checkForUpdateFile();
                } else {
                    checkForShipmentFiles();
                }
                break;
        }
    }

    public int verifyFile(File fileToCheck){
        ArrayList<String> linesToProcess = new ArrayList<String>();
        //Checks for inconsistencies with down, updt, or midw files
        if(fileToCheck.length() == 0){
            Log.e(TAG, "File size is zero");
            return 0;
        }
        linesToProcess.clear();
        customersToAdd.clear();
        productsToAdd.clear();
        authcodesToAdd.clear();
        try {
            String line = "";
            InputStream is = new FileInputStream(fileToCheck.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while(!(line = reader.readLine()).toLowerCase().startsWith("endrcd")){
                linesToProcess.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        int checkRecordCount = 0;
        int actualRecordCount = 1;

        Map<String, String> productNumberAndName = new HashMap<String, String>();

        //get customers and products
        for(String line: linesToProcess){
            if(line.length() > 5){
                if(line.startsWith("PARMTC")){
                    actualRecordCount++;

                    Date d1 = new Date();
                    String today = (String)DateFormat.format("yyyyMMdd", d1.getTime());
                    if(!line.substring(54,110).contains(today)){
                        Log.e(TAG, "Date not contained in file");
                        return 1;
                    }
                    if(!line.substring(6,12).equalsIgnoreCase(prefs.getString("emp_id", ""))){
                        Log.e(TAG, "User mismatch in file");
                        return 2;
                    }
                    if(linesToProcess.size() == 0){
                        Log.e(TAG, "No lines to parse in file");
                        return 0;
                    }

                    String dates = line.substring(54,110);
                    String daysServiced = line.substring(110,117);
                    String mileage = line.substring(117,118);
                    String timeOut = line.substring(240, 242);
                    String acctManaged = Character.toString(line.charAt(242));

                    prefs.edit().putString("param_dates", dates).apply();
                    prefs.edit().putString("days_serviced", daysServiced).apply();
                    prefs.edit().putString("mileage", mileage).apply();
                    prefs.edit().putString("logout_timeout", timeOut).apply();
                    prefs.edit().putString("acct_managed", acctManaged).apply();

                } else if(line.startsWith("CUSTTC")){
                    actualRecordCount++;
                    String cust_number = line.substring(6,14);
                    String store_type1 = line.substring(20,21);
                    String cust_name = line.substring(24,54).trim();
                    String cust_address = line.substring(54,84);
                    String cust_state_zip = line.substring(84, 105);

                    customersToAdd.add(new Customer(cust_name, cust_address, cust_state_zip, cust_number, store_type1));


                } else if(line.startsWith("PRODTC")){
                    actualRecordCount++;
                    String product_code = line.substring(6,12);
                    String product_description = line.substring(12,42).trim();
                    String product_available = line.substring(42,43);
                    String when_available = line.substring(43,49);
                    String upc_prefix = line.substring(49,51);
                    String upc_10 = line.substring (51,61);
                    String upc_check = line.substring(61);

                    productsToAdd.add(new Product(product_description, product_code, product_available, when_available, upc_prefix, upc_10, upc_check, ""));
                    productNumberAndName.put(product_code, product_description);

                } else if(line.startsWith("CKRCTC")){
                    actualRecordCount++;
                    String cff = "";
                    cff += Character.toString(line.charAt(6));
                    cff += Character.toString(line.charAt(7));
                    cff += Character.toString(line.charAt(8));
                    cff += Character.toString(line.charAt(9));
                    cff += Character.toString(line.charAt(10));
                    checkRecordCount = Integer.parseInt(cff);

                }
            }
        }
        //assemble authcodes
        for(String line: linesToProcess){
            if(line.length() > 5){
                if(line.startsWith("AUTHTC")){
                    actualRecordCount++;
                    String customer_number = line.substring(6,14);
                    String product_number = line.substring(14,20);
                    String product_index = line.substring(20);

                    if(productNumberAndName.containsKey(product_number)){
                        String name = productNumberAndName.get(product_number);
                        authcodesToAdd.add(new Authcode("template", name, product_number, customer_number, "","","","","",0,0,"","","","","",""));
                    } else {
                        Log.e(TAG, "An authcode is missing a corresponding product");
                        return 4;
                    }
                }
            }
        }

        Log.e("CUSTOMERS", customersToAdd.size() + "");
        Log.e("PRODUCTS", productsToAdd.size() + "");
        Log.e("AUTHCODES", authcodesToAdd.size() + "");
        Log.e("CKRCTC", checkRecordCount + "");
        Log.e("ACTUAL", actualRecordCount + "");
        if(checkRecordCount != actualRecordCount){
            Log.e(TAG, "Ckrctc mismatch");
            return 3;
        }


        return 5;
    }

    public int addItems(){
        for(Customer c: customersToAdd){
            if(!datasource.customerExists(c.getCust_no())){
                datasource.createCustomer(c);
            }
        }
        for(Product p: productsToAdd){
            if(!datasource.productExists(p.getProd_no())){
                datasource.createProduct(p);
            }
        }
        for(Authcode a: authcodesToAdd){
            if(!datasource.authcodeExists("template", a.getCust_no(), a.getProd_no())){
                datasource.createAuthcode(a);
            }
        }
        Log.e("CUSTOMER_COUNT", datasource.getAllCustomers().size() + "");
        Log.e("PRODUCT_COUNT", datasource.getAllProducts().size() + "");
        Log.e("AUTHCODE_COUNT", datasource.getAllAuthcodes().size() + "");

        //ToDo check if all products have a customer and vice versa(4) from arraylist against db
        return 5;
    }

    public class ProcessShippingTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setMessage("Processing Shipments...");
            loader.show();
        }

        @Override
        protected Integer doInBackground(Void... arg0){

            int verificationCode = verifyShipFile();
            return verificationCode;

        }

        @Override
        protected void onPostExecute(Integer str){
            //loader.dismiss();
            finishShipProcessing(str);
        }
    }

    public void finishShipProcessing(int result){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        switch(result){
            case 0:
                alertDialogBuilder
                        .setMessage("Communication Error: Could not check for shipping data")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        checkForOrderFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 1:
                alertDialogBuilder
                        .setMessage("Data error with ship file. If error persists, contact support")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                        checkForOrderFiles();
                                    }
                                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 2:
                checkForOrderFiles();
                break;
        }
    }

    public int verifyShipFile(){
        ArrayList<String> linesToProcess = new ArrayList<String>();
        //Checks for inconsistencies with down, updt, or midw files
        if(shipFile.length() == 0){
            Log.e(TAG, "File size is zero");
            return 0;
        }
        linesToProcess.clear();
        try {
            String line = "";
            InputStream is = new FileInputStream(shipFile.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while(!(line = reader.readLine()).toLowerCase().equals("endsh")){
                linesToProcess.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        int checkRecordCount = 0;
        int actualRecordCount = 1;
        String shipdate = "";
        //get shipments
        for(String line: linesToProcess){
            if(line.toLowerCase().startsWith("shipdt")){
                shipdate = line.substring(6).replace("/", "");
                actualRecordCount++;
            } else{
                if(line.length() == 18){
                    actualRecordCount++;
                    String shipCust = line.substring(0,8);
                    String shipProd = line.substring(8,14);
                    int shipQty = Integer.parseInt(line.substring(14));
                    datasource.createShipment(shipCust, shipProd, shipQty, shipdate);
                } else if (line.toLowerCase().startsWith("ckrctc")){
                    actualRecordCount++;
                    String cff = "";
                    cff += Character.toString(line.charAt(6));
                    cff += Character.toString(line.charAt(7));
                    cff += Character.toString(line.charAt(8));
                    cff += Character.toString(line.charAt(9));
                    cff += Character.toString(line.charAt(10));
                    Log.e("RECORD", cff);
                    checkRecordCount = Integer.parseInt(cff);
                }
            }
        }
        Log.e("CKRCTC", checkRecordCount + "");
        Log.e("ACTUAL", actualRecordCount + "");
        if(checkRecordCount != actualRecordCount){
            Log.e(TAG, "Ckrctc mismatch");
            return 1;
        }


        return 2;
    }

    public boolean downFileNeeded(){
        boolean needed = false;
        String thisUser = prefs.getString("emp_id", "");
        String lastUser = prefs.getString("last_user", "");

        Date d2 = new Date();
        String todaysDate  = (String)DateFormat.format("yyyyMMdd", d2.getTime());
        if(!prefs.getString("param_dates", "theDates").contains(todaysDate)){
            needed = true;
        }

        if(!thisUser.equals(lastUser)){
            needed = true;
        }

        if(prefs.getInt("downfile_loaded", 0) == 0){
            needed = true;
        }

        //ToDo fix is down file needed
        return true;
    }

    public boolean canLoginOffline(){
        boolean canLogin = true;
        String thisUser = prefs.getString("emp_id", "");
        String lastUser = prefs.getString("last_user", "");

        Date d2 = new Date();
        String todaysDate  = (String)DateFormat.format("yyyyMMdd", d2.getTime());
        if(!prefs.getString("last_login_date", "").contains(todaysDate)){
            canLogin = false;
        }

        if(!thisUser.equals(lastUser)){
            canLogin = false;
        }

        if(prefs.getInt("downfile_loaded", 0) == 0){
            canLogin = false;
        }

        return canLogin;
    }

    public void executeSetup(){
        Log.e(TAG, "Setting up for first run");
        File dir = new File(Environment.getExternalStorageDirectory()+"/Transmit");

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("firstRun", false);

        editor.commit();
    }

    public void checkUser(){
        if(prefs.getString("emp_id", "").equals("")){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                    this);
            alertDialogBuilder
                    .setMessage("Please supply your Login Credentials in Setup")
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    switchUser(null);
                                }
                            });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void switchUser(View view){
        Log.e(TAG, "Switching User");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Please enter Employee ID and Password");
        alert.setTitle("Switch User");
        final EditText usernameEdit = new EditText(this);
        final EditText passwordEdit = new EditText(this);

        usernameEdit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_CLASS_TEXT);
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        usernameEdit.setHint("Employee ID");
        passwordEdit.setHint("Password");

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(usernameEdit);
        ll.addView(passwordEdit);
        alert.setView(ll);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String emp_id = usernameEdit.getText().toString();
                String pass = passwordEdit.getText().toString();

                prefs.edit().putString("emp_id", emp_id).apply();
                prefs.edit().putString("password", pass).apply();
                //ToDo get actual sales manager value
                prefs.edit().putString("sales_manager", "N").apply();
                loadWelcomeText();

                dialog.cancel();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });
        alert.show();
    }

    public void loadWelcomeText(){
        TextView welcomeText = (TextView)findViewById(R.id.welcomeText);
        TextView notText = (TextView)findViewById(R.id.notText);
        String emp_id = prefs.getString("emp_id", "");
        String pass = prefs.getString("password", "");

        if(emp_id.equals("")){
            welcomeText.setText("Welcome!");
            notText.setText("");
        } else {
            welcomeText.setText("Welcome, " + emp_id);
            notText.setText("Not " + emp_id + "?");
        }

    }

    public void back(View view) {
        finishAndRemoveTask();
    }

    public void help(View view) {
        //ToDo add resetting prefs
        datasource.clearAll();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogOutTimerUtil.stopLogoutTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loader.isShowing()){
            loader.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
