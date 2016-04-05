package com.project.songer1993.sentire;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

public class DesignRealtimeVibrationPatterns extends AppCompatActivity implements View.OnClickListener {

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;


    private List<Integer> amplitudes = new ArrayList<Integer>();
    private String msg = "";
    private Button btnSaveCurve, btnClearCurve;
    private MyPaintView myPaintView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_realtime_vibration_patterns);

        ConnectBT.bt.send("design2", true);

        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.my_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Real-Time Vibration Patterns");

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);
        }


        myPaintView = (MyPaintView) findViewById(R.id.paintView);

        btnSaveCurve = (Button) findViewById(R.id.btnSaveCurve);
        btnSaveCurve.setOnClickListener(this);

        btnClearCurve = (Button) findViewById(R.id.btnClearCurve);
        btnClearCurve.setOnClickListener(this);
    }


    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.btnClearCurve:
                myPaintView.clearPoints();
                break;
            case R.id.btnSaveCurve:
                msg = "";
                amplitudes = myPaintView.getAmplitudes(50);
                for(Integer amplitude: amplitudes){
                    msg += amplitude.toString()+",";
                }
                System.out.println(("2:"+msg));
                ConnectBT.bt.send(("2:"+msg), true);
                break;
        }

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
                                intent = new Intent(mContext, Demo.class);
                                break;
                            default:
                                intent = new Intent(mContext, DesignRealtimeVibrationPatterns.class);
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
