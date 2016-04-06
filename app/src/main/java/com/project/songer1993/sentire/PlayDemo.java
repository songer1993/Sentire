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
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout;

public class PlayDemo extends AppCompatActivity {

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;

    private CircleLayout circleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_demo);

        ConnectBT.bt.send("standard", true);

        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.my_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Demo");

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        circleMenu = (CircleLayout)findViewById(R.id.cmEmotion);
        circleMenu.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()){
                    case R.id.btnHappy:
                        ConnectBT.bt.send("happy", true);
                        System.out.println("1");
                        break;
                    case R.id.btnFearful:
                        ConnectBT.bt.send("fearful", true);
                        break;
                    case R.id.btnSurprised:
                        ConnectBT.bt.send("surprised", true);
                        break;
                    case R.id.btnSad:
                        ConnectBT.bt.send("sad", true);
                        break;
                    case R.id.btnDisgusted:
                        ConnectBT.bt.send("disgusted", true);
                        break;
                    case R.id.btnAngry:
                        ConnectBT.bt.send("angry", true);
                        break;

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
                R.drawable.icon_realtime_vibration_patterns,
                R.drawable.icon_light_patterns,
                R.drawable.icon_saved_patterns,
        };

        for (int i = 0; i < 4; i++)
            subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        final String[] subButtonTexts = new String[]{
                "Library Vibration Patterns",
                "Real-Time Vibration Patterns",
                "Light Patterns",
                "Saved Patterns",
        };

        int [] subButtonColorResources = new int[]{
                R.color.card_color1,
                R.color.card_color2,
                R.color.card_color3,
                R.color.card_color4,
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
                                intent = new Intent(mContext, DesignLightPatterns.class);
                                break;
                            case 3:
                                intent = new Intent(mContext, SeeSavedPatterns.class);
                                break;
                            default:
                                intent = new Intent(mContext, PlayDemo.class);
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
