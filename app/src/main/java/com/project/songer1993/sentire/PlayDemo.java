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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout;

public class PlayDemo extends AppCompatActivity {

    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;

    private CircleLayout circleMenu;
    private TextView tvDemoMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_demo);
        setTitle("Demo");


        //ConnectBT.bt.send("standard", true);

        // Action Bar
        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);


        tvDemoMsg = (TextView)findViewById(R.id.tvDemoMsg);

        circleMenu = (CircleLayout)findViewById(R.id.cmEmotion);
        circleMenu.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()){
                    case R.id.btnHappy:
                        ConnectBT.bt.send("0", true);
                        tvDemoMsg.setText("'happy' sent");
                        break;
                    case R.id.btnFearful:
                        ConnectBT.bt.send("1", true);
                        tvDemoMsg.setText("'fearful' sent");
                        break;
                    case R.id.btnSurprised:
                        ConnectBT.bt.send("2", true);
                        tvDemoMsg.setText("'surprised' sent");
                        break;
                    case R.id.btnSad:
                        ConnectBT.bt.send("3", true);
                        tvDemoMsg.setText("'sad' sent");
                        break;
                    case R.id.btnDisgusted:
                        ConnectBT.bt.send("4", true);
                        tvDemoMsg.setText("'disgusted' sent");
                        break;
                    case R.id.btnAngry:
                        ConnectBT.bt.send("5", true);
                        tvDemoMsg.setText("'angry' sent");
                        break;

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
