package becustomapps.com.merchant3.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.List;

import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Transmittable;
import becustomapps.com.merchant3.Utilities.Utility;

public class MainMenuActivity extends AppCompatActivity implements Transmittable, LogOutTimerUtil.LogOutListener{

    DataSource datasource;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getSupportActionBar().setTitle("Main Menu");

        Button exitButton = (Button)findViewById(R.id.backButton);
        exitButton.setText("Log Out");
        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        datasource = new DataSource(this);
        datasource.open();

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        for(Customer c: datasource.getAllCustomers()){
            Log.e("CUST_NO", c.getCust_no());
        }
    }

    public void services(View view){
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
        finish();
    }

    public void orderChange(View view){

    }

    public void transmit(View view){
        List<Punch> allPunches = datasource.getAllServicePunches();
        for(Punch p: allPunches){
            if(p.getTransmitted() == 0){
                Utility.transmit(this, this, p, datasource, prefs);
            }
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
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
        //LogOutTimerUtil.stopLogoutTimer();
        Log.e("TIMER", "Being Destroyed");
    }

    @Override
    public void onBackPressed() {
        return;
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
    public void onTransmitComplete(boolean leave) {
        //Just stay here
    }
}
