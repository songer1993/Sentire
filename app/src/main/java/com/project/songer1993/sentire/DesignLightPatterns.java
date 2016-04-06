package com.project.songer1993.sentire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.channguyen.rsv.RangeSliderView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

public class DesignLightPatterns extends AppCompatActivity {

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;


    private Button btnPlay, btnAdd, btnClear;
    boolean played = false;

    private PieChart mPieChart;
    private int mColor = 0xffffffff;
    private int mNumber = 0;
    private float mDuration = 0;

    private SeekBar sbDuration;
    private float range = 3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_light_patterns);

        ConnectBT.bt.send("design3", true);

        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        final View mCustomView = mInflater.inflate(R.layout.my_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Light Patterns");
        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


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
                                                mNumber += 1;
                                                mPieChart.addPieSlice(new PieModel((new Integer(mNumber)).toString(), mDuration, mColor));
                                                mDuration = 0;
                                            }
                                        })
                                .setTitle("Duration");

                                SeekBar sbDuration = (SeekBar)v.findViewById(R.id.sbDuration);
                                sbDuration.setMax(100);
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
                                        mDuration = progress * range / 100;
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
                    played = false;
                    btnPlay.setText("Play");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.secondaryText));
                }
                else {
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
                btnPlay.setText("Play");
                btnPlay.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                played = false;
            }
        });

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.setInnerValueUnit("ms");

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
                R.drawable.icon_realtime_vibration_patterns,
                R.drawable.icon_saved_patterns,
                R.drawable.icon_demo
        };

        for (int i = 0; i < 4; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        final String[] subButtonTexts = new String[]{
                "Library Vibration Patterns",
                "Real-Time Vibration Patterns",
                "Saved Patterns",
                "Demo"
        };

        int [] subButtonColorResources = new int[]{
                R.color.card_color1,
                R.color.card_color2,
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
                                intent = new Intent(mContext, DesignRealtimeVibrationPatterns.class);
                                break;
                            case 2:
                                intent = new Intent(mContext, SeeSavedPatterns.class);
                                break;
                            case 3:
                                intent = new Intent(mContext, Demo.class);
                                break;
                            default:
                                intent = new Intent(mContext, DesignLightPatterns.class);
                                break;

                        }
                        startActivity(intent);
                    }
                });
    }

    public void onDestroy() {
        super.onDestroy();
        startActivity(new Intent(mContext, MainActivity.class));
    }
}
