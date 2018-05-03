package becustomapps.com.merchant3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.gcacace.signaturepad.views.SignaturePad;

import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;
import becustomapps.com.merchant3.Utilities.LogOutTimerUtil;
import becustomapps.com.merchant3.Utilities.Transmittable;
import becustomapps.com.merchant3.Utilities.Utility;

public class SignatureActivity extends AppCompatActivity implements Transmittable, LogOutTimerUtil.LogOutListener{

    DataSource datasource;
    Punch thisPunch;
    SharedPreferences prefs;
    String punch_id;
    Button cancelButton, clearButton, saveButton;
    SignaturePad mSignaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        getSupportActionBar().setTitle("Signature");

        datasource = new DataSource(this);
        datasource.open();

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        punch_id = intent.getStringExtra("punch_id");

        thisPunch = datasource.getPunchById(punch_id);
        if(thisPunch == null){
            //ToDo error
        }

        cancelButton = (Button)findViewById(R.id.cancelButton);
        clearButton = (Button)findViewById(R.id.clearButton);
        saveButton = (Button)findViewById(R.id.saveButton);

        clearButton.setEnabled(false);
        saveButton.setEnabled(false);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                clearButton.setEnabled(true);
                saveButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                clearButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                finish();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo save here
                mSignaturePad.clear();
                Utility.transmit(SignatureActivity.this, SignatureActivity.this, thisPunch, datasource, prefs);
            }
        });
    }

    public void back(View view) {
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

    @Override
    public void onTransmitComplete(boolean leave) {
        if(thisPunch.getCompleted() == 1 || leave){
            Intent intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            finish();
        } else {
            back(null);
        }
    }
}
