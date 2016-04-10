package com.project.songer1993.sentire;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class ConnectBT extends AppCompatActivity {

    public static BluetoothSPP bt;

    private Button btnConnect;
    private Button btnBypassBT;
    private ImageView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bt);

        getSupportActionBar().setElevation(0);

        bt = new BluetoothSPP(this);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        final Handler handler=new Handler();

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(ConnectBT.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                rippleBackground.stopRippleAnimation();
                startMainAcitivity();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                rippleBackground.stopRippleAnimation();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
                rippleBackground.stopRippleAnimation();
            }
        });

        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MyDeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                    rippleBackground.startRippleAnimation();
                }
            }
        });

        phone=(ImageView)findViewById(R.id.centerImage);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MyDeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                    rippleBackground.startRippleAnimation();
                }
            }
        });

        btnBypassBT = (Button)findViewById(R.id.btnBypassBT);
        btnBypassBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainAcitivity();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void startMainAcitivity(){
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
    }
}
