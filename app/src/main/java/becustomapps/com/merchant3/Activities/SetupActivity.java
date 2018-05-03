package becustomapps.com.merchant3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.Utility;

public class SetupActivity extends AppCompatActivity {

    SharedPreferences prefs;

    EditText username, address, port, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        getSupportActionBar().setTitle("Network Setup");

        Button exitButton = (Button)findViewById(R.id.backButton);
        exitButton.setText("Back");
        Button blankButton = (Button)findViewById(R.id.middleButton);
        blankButton.setEnabled(false);

        username = (EditText)findViewById(R.id.username);
        address = (EditText)findViewById(R.id.address);
        port = (EditText)findViewById(R.id.port);
        password = (EditText)findViewById(R.id.password);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        username.setText(prefs.getString("server_username", ""));
        address.setText(prefs.getString("server_address", ""));
        if(prefs.getInt("server_port", 0) == 0){
            port.setText("");
        } else {
            port.setText(prefs.getInt("server_port", 0) + "");
        }
        password.setText(prefs.getString("server_password", ""));
    }

    public void saveSettings(View view){
        String us = username.getText().toString();
        String ad = address.getText().toString();
        String po = port.getText().toString();
        String pw = password.getText().toString();

        if(us.equals("") || ad.equals("") || po.equals("") || pw.equals("")){
            Utility.alert(this, "All forms not completed");
        } else {
            prefs.edit().putString("server_username", us).apply();
            prefs.edit().putString("server_address", ad).apply();
            prefs.edit().putInt("server_port", Integer.parseInt(po)).apply();
            prefs.edit().putString("server_password", pw).apply();
            back(null);
        }


    }

    public void back(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void help(View view) {

    }
}
