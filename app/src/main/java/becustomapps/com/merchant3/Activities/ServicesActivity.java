package becustomapps.com.merchant3.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import becustomapps.com.merchant3.Adapters.PunchAdapter;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Transmittable;
import becustomapps.com.merchant3.Utilities.Utility;

public class ServicesActivity extends AppCompatActivity implements Transmittable, LogOutTimerUtil.LogOutListener{

    DataSource datasource;
    List<Punch> punches = new ArrayList<Punch>();
    PunchAdapter adapter;

    ProgressDialog loader;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        getSupportActionBar().setTitle("Services");

        Button exitButton = (Button)findViewById(R.id.backButton);
        exitButton.setText("Back");
        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        datasource = new DataSource(this);
        datasource.open();

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        loader = new ProgressDialog(this);
        loader.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loader.setCancelable(false);

        punches = datasource.getAllServicePunches();
        Collections.sort(punches);
        ListView lv = (ListView) findViewById(R.id.punch_list);
        adapter = new PunchAdapter(this, punches);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Punch selected = punches.get(i);
                if(selected.getCompleted() == 0){
                    if(selected.getType().equals("MORNING")){
                        Intent intent = new Intent(ServicesActivity.this, FormActivity.class);
                        intent.putExtra("punch_id", selected.getPunch_id());
                        startActivity(intent);
                        finish();
                    } else if(selected.getType().equals("PULL_UP")){
                        displayPullupOptions(selected);
                    }
                } else {
                    if(selected.getType().equals("MORNING")){
                        displayEditOptions(selected);
                    }
                }

            }
        });

    }

    public void displayEditOptions(final Punch thisPunch){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Edit Service")
                .setMessage("This service is complete.  Would you like to edit it?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                thisPunch.setCompleted(0);
                                thisPunch.setTransmitted(0);
                                Date d2 = new Date();
                                String todaysDate  = (String) DateFormat.format("MM-dd-yyyy", d2.getTime());
                                String todaysTime  = (String) DateFormat.format("kk:mm:ss", d2.getTime());
                                thisPunch.setClock_in_date(todaysDate);
                                thisPunch.setClock_in_time(todaysTime);
                                thisPunch.setClock_out_date("");
                                thisPunch.setClock_out_time("");
                                datasource.updatePunch(thisPunch);
                                Intent intent = new Intent(ServicesActivity.this, FormActivity.class);
                                intent.putExtra("punch_id", thisPunch.getPunch_id());
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void displayPullupOptions(final Punch thisPunch){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Pull-Up Options")
                .setPositiveButton("Clock Out",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Date d2 = new Date();
                                String todaysDate  = (String) DateFormat.format("MM-dd-yyyy", d2.getTime());
                                String todaysTime  = (String) DateFormat.format("kk:mm:ss", d2.getTime());
                                thisPunch.setCompleted(1);
                                thisPunch.setClock_out_date(todaysDate);
                                thisPunch.setClock_out_time(todaysTime);
                                datasource.updatePunch(thisPunch);

                                dialog.cancel();

                                Utility.transmit(ServicesActivity.this, ServicesActivity.this, thisPunch, datasource, prefs);

                                //ToDo change button text?
                                //ToDo notify adapter
                            }
                        })
                .setNeutralButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel Service",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Utility.cancelService(ServicesActivity.this, ServicesActivity.this, thisPunch, datasource);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void newService(View view){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alertDialog;
        alertDialogBuilder
                .setMessage("Choose service type")
                .setCancelable(false)
                .setPositiveButton("Pull-Up",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                                Intent intent = new Intent(ServicesActivity.this, SelectionActivity.class);
                                intent.putExtra("type", "PULL_UP");
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton("Morning",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                                Intent intent = new Intent(ServicesActivity.this, SelectionActivity.class);
                                intent.putExtra("type", "MORNING");
                                startActivity(intent);
                                finish();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    @Override
    public void onTransmitComplete(boolean leave) {
        Log.e("CANCEL", "got here");
        punches.clear();
        punches.addAll(datasource.getAllServicePunches());
        Log.e("PUNCHES", punches.size() + "");
        Collections.sort(punches);
        adapter.notifyDataSetChanged();
        //Just stay here
    }
}
