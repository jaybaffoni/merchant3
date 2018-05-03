package becustomapps.com.merchant3.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
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

import becustomapps.com.merchant3.Adapters.CustomerAdapter;
import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Utility;

public class SelectionActivity extends AppCompatActivity implements LogOutTimerUtil.LogOutListener{

    DataSource datasource;
    List<Customer> customers;

    CustomerAdapter adapter;

    String type = "";
    boolean isMorning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        isMorning = type.equals("MORNING");

        if(isMorning){
            getSupportActionBar().setTitle("Morning Service");
        } else {
            getSupportActionBar().setTitle("Pull-up Service");
        }

        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        datasource = new DataSource(this);
        datasource.open();

        List<Customer> allCustomers = datasource.getAllCustomers();
        customers = new ArrayList<Customer>();

        Collections.sort(allCustomers);
        String first7 = "";
        for(Customer c: allCustomers){
            if (!c.getCust_no().substring(0,7).equals(first7)){
                customers.add(c);
                first7 = c.getCust_no().substring(0,7);
            }
        }

        adapter = new CustomerAdapter(this, customers, type);
        ListView lv = (ListView)findViewById(R.id.customer_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createService(customers.get(i));
            }
        });
    }

    public void createService(Customer customer){
        Date d2 = new Date();
        String todaysDate  = (String) DateFormat.format("MM-dd-yyyy", d2.getTime());
        String todaysTime  = (String) DateFormat.format("kk:mm:ss", d2.getTime());
        String timestamp = (String)DateFormat.format("yyyyMMddkkmmss", d2.getTime());
        String punch_id = customer.getCust_no() + "-" + timestamp;
        Punch newPunch = new Punch(punch_id, "", todaysTime, todaysDate, "","", type, "", customer.getCust_name(), customer.getCust_no(), 0,0, 0, "0");
        datasource.createPunch(newPunch);
        if(isMorning){
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra("punch_id", punch_id);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            finish();
        }

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
