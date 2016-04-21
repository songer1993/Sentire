package com.project.songer1993.sentire;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardViewNative;

public class SeeSavedPatterns extends AppCompatActivity implements View.OnClickListener{

    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;
    private Button btnHappyList, btnFearfulList, btnSurprisedList, btnSadList, btnDisgustedList, btnAngryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_saved_patterns);
        setTitle("Saved Patterns");

        //ConnectBT.bt.send("design4", true);

        // Action Bar
        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        btnHappyList = (Button)findViewById(R.id.btnHappyList);
        btnFearfulList = (Button)findViewById(R.id.btnFearfulList);
        btnSurprisedList = (Button)findViewById(R.id.btnSurprisedList);
        btnSadList = (Button)findViewById(R.id.btnSadList);
        btnDisgustedList = (Button)findViewById(R.id.btnDisgustedList);
        btnAngryList = (Button)findViewById(R.id.btnAngryList);

        btnHappyList.setOnClickListener(this);
        btnFearfulList.setOnClickListener(this);
        btnSurprisedList.setOnClickListener(this);
        btnSadList.setOnClickListener(this);
        btnDisgustedList.setOnClickListener(this);
        btnAngryList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent(this, BrowsePatterns.class);
        switch (v.getId()){
            case R.id.btnHappyList:
                mIntent.putExtra("emotion", "Happy");
                break;
            case R.id.btnFearfulList:
                mIntent.putExtra("emotion", "Fearful");
                break;
            case R.id.btnSurprisedList:
                mIntent.putExtra("emotion", "Surprised");
                break;
            case R.id.btnSadList:
                mIntent.putExtra("emotion", "Sad");
                break;
            case R.id.btnDisgustedList:
                mIntent.putExtra("emotion", "Disgusted");
                break;
            case R.id.btnAngryList:
                mIntent.putExtra("emotion", "Angry");
                break;
        }
        startActivity(mIntent);
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
