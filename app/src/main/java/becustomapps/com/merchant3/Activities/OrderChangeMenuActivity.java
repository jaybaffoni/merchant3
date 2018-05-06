package becustomapps.com.merchant3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Transmittable;

public class OrderChangeMenuActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener{

    SharedPreferences prefs;
    DataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_change_menu);

        getSupportActionBar().setTitle("Order Change Customers");

        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        datasource = new DataSource(this);
        datasource.open();

        //ToDo get all order customers
        //ToDo narrow to same 7s
        //ToDo get all regular customers
        //ToDo create map
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
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


}
