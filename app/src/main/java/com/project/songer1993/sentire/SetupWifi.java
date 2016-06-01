package com.project.songer1993.sentire;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetupWifi extends AppCompatActivity {

    private ActionBar mActionBar;
    private Context mContext;

    private EditText etSSID;
    private EditText etPASS;
    private Button btnWifi;

    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_wifi);
        setTitle("Set up Wi-Fi");

        etSSID = (EditText)findViewById(R.id.etSSID);
        etPASS = (EditText)findViewById(R.id.etPASS);
        btnWifi = (Button)findViewById(R.id.btnWifi);


        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "4:";
                msg += etSSID.getText().toString() + "," + etPASS.getText().toString();
                System.out.println(msg);
                ConnectBT.bt.send(msg, true);
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
