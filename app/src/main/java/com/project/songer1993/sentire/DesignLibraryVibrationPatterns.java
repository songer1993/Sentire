package com.project.songer1993.sentire;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class DesignLibraryVibrationPatterns extends AppCompatActivity {

    private ActionBar mActionBar;
    private Context mContext;

    RadioButton rbHappy, rbFearful, rbSurprised, rbSad, rbDisgusted, rbAngry;
    private RadioGroup rgEmotions;
    private EditText etName;

    private MobileServiceClient mClient;
    private MobileServiceTable mTable;
    private VibrationPattern mVibrationVibrationPattern;
    private String mName;
    private String mType = "Library Vibration";
    private String mEmotion;
    private String mValue;
    private int mScore;


    Button btnPlay, btnClear;
    EditText etEffectSequence;
    GridView gridView;
    MyGVAdapter myGVAdapter;
    String effectSequence = "";
    boolean played = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_library_vibration_patterns);
        setTitle("Library Vibration Pattern");

        ConnectBT.bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
                System.out.println(message);
                int newRate;
                if(message == "1")
                    newRate = 1;
                else
                    newRate = -1;
                mScore = mVibrationVibrationPattern.getScore() + newRate;
                mVibrationVibrationPattern.setScore(mScore);

                mTable.update(mVibrationVibrationPattern, new TableOperationCallback<VibrationPattern>() {
                    public void onCompleted(VibrationPattern entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            Toast.makeText(getApplicationContext(), "Score updated!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Save updating failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        // Action Bar
        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);


        // Azure
        try {
            mClient = new MobileServiceClient(
                    Constants.MOBILE_SERVICE_URL,
                    Constants.APPLICATION_KEY,
                    this
            );
            mTable = mClient.getTable(VibrationPattern.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // UI elements
        etEffectSequence = (EditText)findViewById(R.id.etEffectSequence);
        gridView=(GridView)findViewById(R.id.gridViewCustom);

        // Create the Custom Adapter Object
        myGVAdapter = new MyGVAdapter(mContext);
        // Set the Adapter to GridView
        gridView.setAdapter(myGVAdapter);

        // Handling touch/click Event on GridView Item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (played) {
                    played = false;
                    btnPlay.setText("Play");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.primaryText));
                    effectSequence = "";
                    etEffectSequence.setText("");
                }

                if (effectSequence.isEmpty()) {
                    effectSequence += String.valueOf(position);
                } else {
                    effectSequence += (", " + String.valueOf(position));
                }
                etEffectSequence.setText(effectSequence);

            }
        });

        gridView.setLongClickable(true);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                Toast.makeText(getApplicationContext(), mEffectNames[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        btnPlay = (Button)findViewById(R.id.btnPlay1);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (played) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    v = inflater.inflate(R.layout.my_name_dialog, null);
                    builder.setView(v)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mName = etName.getText().toString();
                                    mVibrationVibrationPattern = new VibrationPattern();
                                    mVibrationVibrationPattern.setName(mName);
                                    mVibrationVibrationPattern.setEmotion(mEmotion);
                                    mVibrationVibrationPattern.setType(mType);
                                    mVibrationVibrationPattern.setValue(mValue);
                                    mVibrationVibrationPattern.setScore(0);
                                    mVibrationVibrationPattern.setScoreHappy(0);
                                    mVibrationVibrationPattern.setScoreSad(0);
                                    mVibrationVibrationPattern.setScoreFearful(0);
                                    mVibrationVibrationPattern.setScoreAngry(0);
                                    mVibrationVibrationPattern.setScoreNeutral(0);
                                    mTable.insert(mVibrationVibrationPattern, new TableOperationCallback<VibrationPattern>() {
                                        public void onCompleted(VibrationPattern entity, Exception exception, ServiceFilterResponse response) {
                                            if (exception == null) {
                                                Toast.makeText(getApplicationContext(), "Saved!",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Save failed",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .setTitle("Pattern Name");

                    etName = (EditText)v.findViewById(R.id.etName);

                    builder.create();
                    builder.show();

                    played = false;
                    btnPlay.setText("Play");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.secondaryText));

                }
                else {
                    if (etEffectSequence.getText().length() != 0) {
                        mValue = "1: "+etEffectSequence.getText().toString();
                        ConnectBT.bt.send(mValue, true);
                        played = true;
                        btnPlay.setText("Save?");
                        btnPlay.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        });

        btnClear = (Button)findViewById(R.id.btnClear1);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectSequence = "";
                etEffectSequence.setText("");
                btnPlay.setText("Play");
                btnPlay.setTextColor(getApplication().getResources().getColor(R.color.secondaryText));
                played = false;
            }
        });


        rbHappy = (RadioButton) findViewById(R.id.rbHappy);
        rbFearful = (RadioButton) findViewById(R.id.rbFearful);
        rbSurprised = (RadioButton) findViewById(R.id.rbSurprised);
        rbSad = (RadioButton) findViewById(R.id.rbSad);
        rbDisgusted = (RadioButton) findViewById(R.id.rbDisgusted);
        rbAngry = (RadioButton) findViewById(R.id.rbAngry);


        rgEmotions = (RadioGroup)findViewById(R.id.rgEmotions);
        rgEmotions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rbHappy) {
                    rbHappy.setButtonDrawable(R.drawable.icon_happy);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Happy",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Happy";
                }
                else if(checkedId == R.id.rbFearful) {
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Fearful",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Fearful";
                }
                else if(checkedId == R.id.rbSurprised) {
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Surprised",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Surprised";
                }
                else if(checkedId == R.id.rbSad) {
                    rbSad.setButtonDrawable(R.drawable.icon_sad);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Sad",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Sad";
                }
                else if(checkedId == R.id.rbDisgusted) {
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Disgusted",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Disgusted";
                }
                else {
                    rbAngry.setButtonDrawable(R.drawable.icon_angry);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    Toast.makeText(getApplicationContext(), "type selected: Angry",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Angry";
                }
            }

        });


    }


    private String[] mEffectNames = {
            "Strong Click - 100%", "Strong Click - 60%", "Strong Click - 30%",
            "Sharp Click -100%", "Sharp Click - 60%", "Sharp Click - 30%",
            "Soft Bump - 100%", "Soft Bump - 60%", "Soft Bump - 30%",
            "Double Click - 100%", "Double Click - 60%", "Triple Click - 100%",
            "Soft Fuzz - 60%", "Strong Buzz - 100%",
            "750ms Alert - 100%", "1000ms Alert - 100%",
            "Strong Click 1 - 100%", "Strong Click 2 - 80%", "Strong Click 3 - 60%", "Strong Click 4 - 30%",
            "Medium Click 1 - 100%", "Medium Click 2 - 80%", "Medium Click 3 - 60%",
            "Sharp Tick 1 - 100%", "Sharp Tick 2 - 80%", "Sharp Tick 3 - 60%",
            "Short Double Click Strong 1 - 100%", "Short Double Click Strong 2 - 80%",
            "Short Double Click Strong 3 - 60%", "Short Double Click Strong 4 - 30%",
            "Short Double Click Medium 1 - 100%", "Short Double Click Medium 2 - 80%", "Short Double Click Medium3 60%",
            "Short Double Sharp Tick 1 - 100%", "Short Double Sharp Tick2 80%", "Short Double Sharp Tick3 60%",
            "Long Double Click Strong 1 - 100%", "Long Double Click Strong 2 - 80%",
            "Long Double Click Strong 3 - 60%", "Long Double Click Strong 4 - 30%",
            "Long Double Click Medium 1 - 100%", "Long Double Click Medium 2 - 80%", "Long Double Click Medium 3 - 60%",
            "Long Double Sharp Tick 1 - 100%", "Long Double Sharp Tick 2 - 80%", "Long Double Sharp Tick 3 - 60%",
            "Buzz 1 100%", "Buzz 2 - 80%", "Buzz 3 - 60%", "Buzz 4 - 40%", "Buzz 5 - 20%",
            "Pulsing Strong 1 - 100%", "Pulsing Strong 2 - 60%",
            "Pulsing Medium 1 - 100%", "Pulsing Medium 2 - 60%",
            "Pulsing Sharp 1 - 100%", "Pulsing Sharp 2 - 60%",
            "Transition Click 1 - 100%", "Transition Click2 80%", "Transition Click 3 -60%",
            "Transition Click 4 - 40%", "Transition Click5 20%", "Transition Click 6 - 10%",
            "Transition Hum 1 - 100%", "Transition Hum2 80%", "Transition Hum 3 - 60%",
            "Transition Hum 4 - 40%", "Transition Hum5 20%", "Transition Hum 1 - 10%",
            "Transition Ramp Down Long Smooth 1 - 100 to 0%", "Transition Ramp Down Long Smooth 2 - 100 to 0%",
            "Transition Ramp Down Medium Smooth 1 - 100 to 0%", "Transition Ramp Down Medium Smooth 2 -100 to 0%",
            "Transition Ramp Down Short Smooth 1 - 100 to 0%", "Transition Ramp Down Short Smooth 2 - 100 to 0%",
            "Transition Ramp Down Long Sharp 1 - 100 to 0%", "Transition Ramp Down Long Sharp 2 - 100 to 0%",
            "Transition Ramp Down Medium Sharp 1 - 100 to 0%", "Transition Ramp Down Medium Sharp 2 - 100 to 0%",
            "Transition Ramp Down Short Sharp 1 - 100 to 0%", "Transition Ramp Down Short Sharp 2 - 100 to 0%",
            "Transition Ramp Up Long Smooth 1 - 0 to 100%", "Transition Ramp Up Long Smooth 2 - 0 to 100%",
            "Transition Ramp Up Medium Smooth 1 - 0 to 100%", "Transition Ramp Up Medium Smooth 2 - 0 to 100%",
            "Transition Ramp Up Short Smooth 1 - 0 to 100%", "Transition Ramp Up Short Smooth 2 - 0 to 100%",
            "Transition Ramp Up Long Sharp 1 - 0 to 100%", "Transition Ramp Up Long Sharp 2 - 0 to 100%",
            "Transition Ramp Up Medium Sharp 1 - 0 to 100%", "Transition Ramp Up Medium Sharp 2 - 0 to 100%",
            "Transition Ramp Up Short Sharp 1 - 0 to 100%", "Transition Ramp Up Short Sharp 2 - 0 to 100%",
            "Transition Ramp Down Long Smooth 1 - 50 to 0%", "Transition Ramp Down Long Smooth 2 - 100 to 0%",
            "Transition Ramp Down Medium Smooth 1 - 50 to 0%", "Transition Ramp Down Medium Smooth 2 - 100 to 0%",
            "Transition Ramp Down Short Smooth 1 - 50 to 0%", "Transition Ramp Down Short Smooth 2 - 100 to 0%",
            "Transition Ramp Down Long Sharp 1 - 50 to 0%", "Transition Ramp Down Long Sharp 2 - 100 to 0%",
            "Transition Ramp Down Medium Sharp 1 - 50 to 0%", "Transition Ramp Down Medium Sharp 2 - 100 to 0%",
            "Transition Ramp Down Short Sharp 1 - 50 to 0%", "Transition Ramp Down Short Sharp 2 - 100 to 0%",
            "Transition Ramp Up Long Smooth 1 - 0 to 50%", "Transition Ramp Up Long Smooth 2 - 0 to 50%",
            "Transition Ramp Up Medium Smooth 1 - 0 to 50%", "Transition Ramp Up Medium Smooth 2 - 0 to 50%",
            "Transition Ramp Up Short Smooth 1 - 0 to 50%", "Transition Ramp Up Short Smooth 2 - 0 to 50%",
            "Transition Ramp Up Long Sharp 1 - 0 to 50%", "Transition Ramp Up Long Sharp 2 - 0 to 50%",
            "Transition Ramp Up Medium Sharp 1 - 0 to 50%", "Transition Ramp Up Medium Sharp 2 - 0 to 50%",
            "Transition Ramp Up Short Sharp 1 - 0 to 50%", "Transition Ramp Up Short Sharp 2 - 0 to 50%",
            "Long Buzz for Programmatic Stopping - 100%",
            "Smooth Hum 1 (No kick or brake pulse) - 50%",
            "Smooth Hum 2 (No kick or brake pulse) - 40%",
            "Smooth Hum 3 (No kick or brake pulse) - 30%",
            "Smooth Hum 4 (No kick or brake pulse) - 20%",
            "Smooth Hum 5 (No kick or brake pulse) - 10%",
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
