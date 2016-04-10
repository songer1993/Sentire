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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class DesignRealtimeVibrationPatterns extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable mTable;
    private VibrationPattern mVibrationVibrationPattern;
    private String mName;
    private String mType = "Realtime Vibration";
    private String mEmotion;
    private String mValue;
    private int mScore;

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;


    private List<Integer> amplitudes = new ArrayList<Integer>();
    private String msg = "";
    private Button btnPlay, btnClear;
    boolean played = false;
    private MyPaintView myPaintView;
    private ValueLineChart mCubicValueLineChart;

    RadioButton rbHappy, rbFearful, rbSurprised, rbSad, rbDisgusted, rbAngry;
    private RadioGroup rgEmotions;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_realtime_vibration_patterns);

        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.my_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Real-Time Vibration Patterns");
        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ConnectBT.bt.send("design2", true);
        ConnectBT.bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
                int newRate = Integer.valueOf(message);
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

        // Azure
        try {
            mClient = new MobileServiceClient(
                    "https://songerarduinotest.azure-mobile.net/",
                    "IBmOdZkslBSsjrCkJeQNvpjHOpTQYr42",
                    this
            );
            mTable = mClient.getTable(VibrationPattern.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        myPaintView = (MyPaintView) findViewById(R.id.paintView);
        mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.setUseDynamicScaling(false);
        mCubicValueLineChart.setMaxZoomY(255);
        mCubicValueLineChart.setIndicatorTextColor(getResources().getColor(R.color.colorPrimaryDark));

        btnPlay = (Button) findViewById(R.id.btnPlay2);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
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
                    msg = "";
                    mCubicValueLineChart.clearChart();
                    amplitudes = myPaintView.getAmplitudes(50);
                    amplitudes.add(0, 0);
                    amplitudes.add(0);

                    ValueLineSeries series = new ValueLineSeries();
                    series.setColor(getResources().getColor(R.color.colorAccent));

                    for(int i=0; i < amplitudes.size(); i++) {
                        msg += amplitudes.get(i).toString()+", ";
                        series.addPoint(new ValueLinePoint(((new Integer(i*40)).toString()+"ms"), amplitudes.get(i)));
                    }
                    mCubicValueLineChart.addSeries(series);
                    mCubicValueLineChart.startAnimation();
                    mValue = "2: "+msg;
                    ConnectBT.bt.send(mValue, true);
                    System.out.println(mValue);
                    played = true;
                    btnPlay.setText("Save?");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.colorAccent));
                }
            }
        });


        btnClear = (Button) findViewById(R.id.btnClear2);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPaintView.clearPoints();
                myPaintView.invalidate();
                mCubicValueLineChart.clearChart();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Use a param to record whether the boom button has been initialized
        // Because we don't need to init it again when onResume()
        if (init) return;
        init = true;

        Drawable[] subButtonDrawables = new Drawable[4];
        int[] drawablesResource = new int[]{
                R.drawable.icon_library_vibration_patterns,
                R.drawable.icon_light_patterns,
                R.drawable.icon_saved_patterns,
                R.drawable.icon_demo
        };

        for (int i = 0; i < 4; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        final String[] subButtonTexts = new String[]{
                "Library Vibration Patterns",
                "Light Patterns",
                "Saved Patterns",
                "Demo"
        };

        int[] subButtonColorResources = new int[]{
                R.color.card_color1,
                R.color.card_color3,
                R.color.card_color4,
                R.color.card_color5
        };

        int[][] subButtonColors = new int[4][2];
        for (int i = 0; i < 4; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, subButtonColorResources[i]);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }

        boomMenuButtonInActionBar.init(
                subButtonDrawables, // The drawables of images of sub buttons. Can not be null.
                subButtonTexts,     // The texts of sub buttons, ok to be null.
                subButtonColors,    // The colors of sub buttons, including pressed-state and normal-state.
                ButtonType.HAM,     // The button type.
                BoomType.PARABOLA,  // The boom type.
                PlaceType.HAM_4_1,  // The place type.
                null,               // Ease type to move the sub buttons when showing.
                null,               // Ease type to scale the sub buttons when showing.
                null,               // Ease type to rotate the sub buttons when showing.
                null,               // Ease type to move the sub buttons when dismissing.
                null,               // Ease type to scale the sub buttons when dismissing.
                null,               // Ease type to rotate the sub buttons when dismissing.
                null                // Rotation degree.
        );

        boomMenuButtonInActionBar.setOnSubButtonClickListener(
                new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        Toast.makeText(
                                mContext,
                                "On click " + subButtonTexts[buttonIndex],
                                Toast.LENGTH_SHORT).show();
                        Intent intent;
                        switch (buttonIndex) {
                            case 0:
                                intent = new Intent(mContext, DesignLibraryVibrationPatterns.class);
                                break;
                            case 1:
                                intent = new Intent(mContext, DesignLightPatterns.class);
                                break;
                            case 2:
                                intent = new Intent(mContext, SeeSavedPatterns.class);
                                break;
                            case 3:
                                intent = new Intent(mContext, PlayDemo.class);
                                break;
                            default:
                                intent = new Intent(mContext, DesignRealtimeVibrationPatterns.class);
                                break;

                        }
                        startActivity(intent);
                    }
                });
    }

}
