package becustomapps.com.merchant3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.OrderCustomer;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;

public class OrderChangeActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener{

    SharedPreferences prefs;
    DataSource datasource;
    String cust_no;
    OrderCustomer orderCustomer;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_change);

        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        datasource = new DataSource(this);
        datasource.open();

        Intent intent = getIntent();
        cust_no = intent.getStringExtra("cust_no");

        orderCustomer = datasource.getOrderCustomerById(cust_no);
        customer = datasource.getCustomerById(cust_no);
        if(orderCustomer == null){
            //ToDo error
        }
        if(customer == null){
            //ToDo error
        }

        ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(this);
        textview.setText(customer.getCust_name());
        textview.setTextColor(Color.WHITE);
        textview.setTextSize(18);
        textview.setGravity(Gravity.CENTER);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
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
