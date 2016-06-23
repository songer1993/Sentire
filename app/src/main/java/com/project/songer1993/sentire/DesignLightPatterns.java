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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.net.MalformedURLException;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class DesignLightPatterns extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable mTable;
    private LightPattern mLightPattern;
    private String mType = "Light";
    private String mName;
    private String mEmotion;
    private String mValue = "";
    private int mScore;

    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;


    private Button btnPlay, btnAdd, btnClear;
    boolean played = false;

    private PieChart mPieChart;
    private int mColor = 0xffffffff;
    private int mNumber = 0;
    private int mDuration = 0;

    private SeekBar sbDuration;
    private TextView tvDuration;


    RadioButton rbHappy, rbFearful, rbSurprised, rbSad, rbDisgusted, rbAngry;
    private RadioGroup rgEmotions;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_light_patterns);
        setTitle("Light Pattern");


        //ConnectBT.bt.send("design3", true);
        ConnectBT.bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
                int newRate;
                if(message == "1")
                    newRate = 1;
                else
                    newRate = -1;
                mScore = mLightPattern.getScore() + newRate;
                mLightPattern.setScore(mScore);

                mTable.update(mLightPattern, new TableOperationCallback<LightPattern>() {
                    public void onCompleted(LightPattern entity, Exception exception, ServiceFilterResponse response) {
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
            mTable = mClient.getTable(LightPattern.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        btnAdd = (Button)findViewById(R.id.btnAdd3);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(mContext)
                        .setTitle("Choose color")
                        .initialColor(mColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                mColor = selectedColor;

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                LayoutInflater inflater = getLayoutInflater();
                                View v = inflater.inflate(R.layout.my_duration_dialog, null);
                                builder.setView(v)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int r = (mColor >> 16) & 0xFF;
                                                int g = (mColor >> 8) & 0xFF;
                                                int b = (mColor >> 0) & 0xFF;
                                                mValue += (new Integer(r)).toString() + ", ";
                                                mValue += (new Integer(g)).toString() + ", ";
                                                mValue += (new Integer(b)).toString() + ", ";
                                                mValue += (new Integer(mDuration).toString()) + ", ";
                                                mNumber += 1;
                                                mPieChart.addPieSlice(new PieModel((new Integer(mNumber)).toString(), mDuration, mColor));
                                                mDuration = 0;
                                            }
                                        })
                                .setTitle("Duration");

                                tvDuration = (TextView)v.findViewById(R.id.tvDuration);

                                sbDuration = (SeekBar)v.findViewById(R.id.sbDuration);
                                sbDuration.setMax(3000);
                                sbDuration.setProgress(0);
                                sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                                  boolean fromUser) {
                                        // TODO Auto-generated method stub
                                        mDuration = progress;
                                        tvDuration.setText((new Integer(mDuration)).toString()+"ms");
                                    }
                                });

                                builder.create();
                                builder.show();

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(false)
                        .build()
                        .show();

            }
        });

        btnPlay = (Button)findViewById(R.id.btnPlay3);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(played){

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    v = inflater.inflate(R.layout.my_name_dialog, null);
                    builder.setView(v)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mName = etName.getText().toString();
                                    mLightPattern = new LightPattern();
                                    mLightPattern.setName(mName);
                                    mLightPattern.setEmotion(mEmotion);
                                    mLightPattern.setType(mType);
                                    mLightPattern.setValue(mValue);
                                    mLightPattern.setScore(0);
                                    mLightPattern.setScoreHappy(0);
                                    mLightPattern.setScoreSad(0);
                                    mLightPattern.setScoreFearful(0);
                                    mLightPattern.setScoreAngry(0);
                                    mLightPattern.setScoreNeutral(0);

                                    mTable.insert(mLightPattern, new TableOperationCallback<LightPattern>() {
                                        public void onCompleted(LightPattern entity, Exception exception, ServiceFilterResponse response) {
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
                    mValue = "3: "+mValue;
                    System.out.println(mValue);
                    ConnectBT.bt.send(mValue, true);
                    played = true;
                    btnPlay.setText("Save?");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                }
            }
        });

        btnClear = (Button)findViewById(R.id.btnClear3);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumber = 0;
                mValue = "";
                mPieChart.clearChart();
                mPieChart.startAnimation();
                btnPlay.setText("Play");
                btnPlay.setTextColor(getApplication().getResources().getColor(R.color.secondaryText));
                played = false;
            }
        });

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.setInnerValueUnit("ms");


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
                if (checkedId == R.id.rbHappy) {
                    rbHappy.setButtonDrawable(R.drawable.icon_happy);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Happy",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Happy";
                } else if (checkedId == R.id.rbFearful) {
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Fearful",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Fearful";
                } else if (checkedId == R.id.rbSurprised) {
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Surprised",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Surprised";
                } else if (checkedId == R.id.rbSad) {
                    rbSad.setButtonDrawable(R.drawable.icon_sad);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Sad",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Sad";
                } else if (checkedId == R.id.rbDisgusted) {
                    rbDisgusted.setButtonDrawable(R.drawable.icon_disgusted);
                    rbHappy.setButtonDrawable(R.drawable.icon_happy0);
                    rbFearful.setButtonDrawable(R.drawable.icon_fearful0);
                    rbSurprised.setButtonDrawable(R.drawable.icon_surprised0);
                    rbSad.setButtonDrawable(R.drawable.icon_sad0);
                    rbAngry.setButtonDrawable(R.drawable.icon_angry0);
                    Toast.makeText(getApplicationContext(), "type selected: Disgusted",
                            Toast.LENGTH_SHORT).show();
                    mEmotion = "Disgusted";
                } else {
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
