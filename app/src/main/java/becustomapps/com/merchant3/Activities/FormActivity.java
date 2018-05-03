package becustomapps.com.merchant3.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import becustomapps.com.merchant3.Adapters.AuthcodeAdapter;
import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.Objects.Shipment;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Transmittable;
import becustomapps.com.merchant3.Utilities.Utility;

public class FormActivity extends AppCompatActivity implements Transmittable, LogOutTimerUtil.LogOutListener{

    DataSource datasource;
    SharedPreferences prefs;

    Customer thisCustomer;
    Punch thisPunch;
    String punch_id;
    List<Authcode> authcodes;
    HashMap<String, Shipment> shipmentMap = new HashMap<String, Shipment>();
    int index = 0;

    ProgressDialog loader;

    private RecyclerView recyclerView;
    private AuthcodeAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.SmoothScroller smoothScroller;

    GregorianCalendar gc;
    boolean needsSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Button completeButton = (Button)findViewById(R.id.middleButton);
        completeButton.setText("Menu");
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        datasource = new DataSource(this);
        datasource.open();

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        needsSignature = true;
        //ToDo fix this

        loader = new ProgressDialog(this);
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loader.setCancelable(false);

        Intent intent = getIntent();
        punch_id = intent.getStringExtra("punch_id");

        thisPunch = datasource.getPunchById(punch_id);
        if(thisPunch == null){
            //ToDo error
        }
        thisCustomer = datasource.getCustomerById(thisPunch.getCust_no());
        if(thisCustomer == null){
            //ToDo error
        }

        ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(this);
        textview.setText(thisPunch.getCust_name());
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        textview.setGravity(Gravity.CENTER);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        if(thisPunch.getDate_of_service().equals("")){
            getDateOfService();
        } else {
            LoadTask lt = new LoadTask(punch_id);
            lt.execute();
        }

    }

    public void getDateOfService(){
        gc = new GregorianCalendar();
        final String todaysDate = (String) DateFormat.format("MMddyyyy", gc.getTime().getTime());
        gc.add(Calendar.DATE, 1);
        final String tomorrowsDate = (String) DateFormat.format("MMddyyyy", gc.getTime().getTime());
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Date")
                .setMessage("Please select date of service")
                .setNegativeButton("Today",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Log.e("DATE", "today");
                                thisPunch.setDate_of_service(todaysDate);
                                datasource.updatePunch(thisPunch);
                                LoadTask lt = new LoadTask(punch_id);
                                lt.execute();
                            }
                        })
                .setPositiveButton("Tomorrow",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Log.e("DATE", "tomorrow");
                                thisPunch.setDate_of_service(tomorrowsDate);
                                datasource.updatePunch(thisPunch);
                                LoadTask lt = new LoadTask(punch_id);
                                lt.execute();
                            }
                        })
                .setNeutralButton("Cancel Service",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Log.e("DATE", "cancel");
                                //ToDo cancel service
                            }
                        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showMenu(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Menu")
                .setPositiveButton("Complete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                completeService();
                            }
                        })
                .setNeutralButton("Transmit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if(needsSignature){
                                    Intent intent = new Intent(FormActivity.this, SignatureActivity.class);
                                    intent.putExtra("punch_id", thisPunch.getPunch_id());
                                    startActivity(intent);
                                } else {
                                    Utility.transmit(FormActivity.this, FormActivity.this, thisPunch, datasource, prefs);
                                }
                            }
                        })
                .setNegativeButton("Cancel Service",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Utility.cancelService(FormActivity.this, FormActivity.this, thisPunch, datasource);
                            }
                        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void completeService(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);

        alertDialogBuilder
                .setMessage("Are you sure you want to complete this service?")
                .setCancelable(false)
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                dialog.cancel();

                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                dialog.cancel();

                                Date d2 = new Date();
                                String todaysDate  = (String) DateFormat.format("MM-dd-yyyy", d2.getTime());
                                String todaysTime  = (String) DateFormat.format("kk:mm:ss", d2.getTime());
                                thisPunch.setCompleted(1);
                                thisPunch.setClock_out_date(todaysDate);
                                thisPunch.setClock_out_time(todaysTime);
                                datasource.updatePunch(thisPunch);
                                if(needsSignature){
                                    Intent intent = new Intent(FormActivity.this, SignatureActivity.class);
                                    intent.putExtra("punch_id", thisPunch.getPunch_id());
                                    startActivity(intent);
                                } else {
                                    Utility.transmit(FormActivity.this, FormActivity.this, thisPunch, datasource, prefs);
                                }


                            }
                        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onTransmitComplete(boolean leave) {
        if(thisPunch.getCompleted() == 1 || leave){
            Intent intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private class LoadTask extends AsyncTask<Void, Void, Integer> {

        String pid = "";

        @Override
        protected void onPreExecute(){
            loader.setMessage("Loading...");
            loader.show();
        }

        public LoadTask(String pid){
            this.pid = pid;
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            int toReturn = 0;

            try{
                Thread.sleep(500);
            } catch (InterruptedException e){

            }

            List<Authcode> templates =
                    datasource.getAllTemplates(thisCustomer.getCust_no().substring(0,7));
            Log.e("TEMPLATES", templates.size() + "");
            for(Authcode a: templates){
                if(!datasource.authcodeExists(punch_id, a.getCust_no(), a.getProd_no())){
                    a.setPunch_id(punch_id);
                    datasource.createAuthcode(a);
                    Log.e("REAL", a.getPunch_id() + ", " + a.getProd_name());
                }
            }

            List<Shipment> currentShipments =
                    datasource.getShipmentsByDate(thisPunch.getCust_no(), thisPunch.getDate_of_service());
            for(Shipment s: currentShipments){
                shipmentMap.put(s.getProd_no(), s);
            }
            Log.e("SHIP", currentShipments.size() + "");



            return toReturn;
        }

        @Override
        protected void onPostExecute(Integer result) {
            completeSetup(result);
        }
    }

    public void completeSetup(int result){
        index = thisPunch.getLeft_off();
        authcodes = datasource.getAuthcodesWithPID(punch_id);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        mAdapter = new AuthcodeAdapter(authcodes, shipmentMap, this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });
        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.e("FOCUS", "focus shifted to recycler");
                    EditText oh =(EditText) (recyclerView.findViewWithTag("onhand"));
                    oh.requestFocus();
                }
            }
        });


        Log.e("LEFT_OFF", index + "");
        scroll(index);
        //ToDo left off is being set on every text change; not scrolling

        if(loader.isShowing()){
            loader.dismiss();
        }
    }

    public void scroll(int position){
        smoothScroller.setTargetPosition(position);
        mLayoutManager.startSmoothScroll(smoothScroller);
        Log.e("LEFT_OFF", "scrolled to " + position);

    }

    public void updateRow(int pos, String oh, String ns, String md, String mdr){
        Authcode ac = authcodes.get(pos);
        ac.setOnhand(oh);
        ac.setNotsold(ns);
        ac.setMarkdown(md);
        ac.setMdretail(mdr);
        ac.setCompleted(1);
        datasource.updateAuthcode(ac);
        thisPunch.setLeft_off(pos);
        datasource.updatePunch(thisPunch);
    }

    public void updateAdjust(int pos, String ch, String sh, String dm, String cr, String tr, String re){
        Authcode ac = authcodes.get(pos);
        ac.setCharge(ch);
        ac.setShort_(sh);
        ac.setDamaged(dm);
        ac.setCripple(cr);
        ac.setTransfer(tr);
        ac.setRecall(re);
        ac.setCompleted(1);
        datasource.updateAuthcode(ac);
        thisPunch.setLeft_off(pos);
        datasource.updatePunch(thisPunch);
    }


    public void back(View view) {
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
        finish();
    }

    public void help(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TIMER", "OnStart () &&& Starting timer");
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("TIMER", "User interacting with screen");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loader.isShowing()){
            loader.dismiss();
        }
        Log.e("TIMER", "Being Destroyed");
    }

    @Override
    public void doLogout(boolean foreGround) {
        if(foreGround){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("force_logout", true);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
