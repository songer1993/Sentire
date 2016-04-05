package com.project.songer1993.sentire;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

public class DesignLibraryVibrationPatterns extends AppCompatActivity {

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;

    RadioButton rbHappy, rbFearful, rbSurprised, rbSad, rbDisgusted, rbAngry;
    private RadioGroup rgEmotions;


    Button btnPlay;
    Button btnClear;
    EditText etEffectSequence;
    GridView gridView;
    MyGVAdapter myGVAdapter;
    String effectSequence = "";
    boolean played = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_library_vibration_patterns);

        ConnectBT.bt.send("design1", true);

        // Action Bar
        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.my_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Library Vibration Patterns");

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


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
                if (played){
                    played = false;
                    btnPlay.setText("Play");
                    btnPlay.setTextColor(getApplication().getResources().getColor(R.color.primaryText));
                }
                else {
                    if (etEffectSequence.getText().length() != 0) {
                        ConnectBT.bt.send(("1:"+etEffectSequence.getText().toString()), true);
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
                btnPlay.setText("Play");
                btnPlay.setTextColor(getApplication().getResources().getColor(R.color.colorPrimary));
                effectSequence = "";
                etEffectSequence.setText("");
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Use a param to record whether the boom button has been initialized
        // Because we don't need to init it again when onResume()
        if (init) return;
        init = true;

        Drawable[] subButtonDrawables = new Drawable[4];
        int[] drawablesResource = new int[]{
                R.drawable.icon_realtime_vibration_patterns,
                R.drawable.icon_light_patterns,
                R.drawable.icon_saved_patterns,
                R.drawable.icon_demo
        };

        for (int i = 0; i < 4; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        final String[] subButtonTexts = new String[]{
                "Real-Time Vibration Patterns",
                "Light Patterns",
                "Saved Patterns",
                "Demo"
        };

        int [] subButtonColorResources = new int[]{
                R.color.card_color2,
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
                                intent = new Intent(mContext, DesignRealtimeVibrationPatterns.class);
                                break;
                            case 1:
                                intent = new Intent(mContext, DesignLightPatterns.class);
                                break;
                            case 2:
                                intent = new Intent(mContext, SeeSavedPatterns.class);
                                break;
                            case 3:
                                intent = new Intent(mContext, Demo.class);
                                break;
                            default:
                                intent = new Intent(mContext, DesignLibraryVibrationPatterns.class);
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
