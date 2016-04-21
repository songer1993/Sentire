package com.project.songer1993.sentire;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        getSupportActionBar().setElevation(0);

        mContext = this;
        setTitle("Dashboard");

        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rv.setLayoutManager(llm);

        initializeData();

        MyRVAdapter adapter = new MyRVAdapter(features);
        rv.setAdapter(adapter);

        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class Feature {
        int iconID;
        String name;
        String description;
        int color;

        Feature(int iconID , String name, String description, int color) {
            this.name = name;
            this.iconID = iconID;
            this.description = description;
            this.color = color;
        }
    }

    private List<Feature> features;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData(){

        int[] card_drawablesResources = new int[]{
                R.drawable.icon_library_vibration_patterns,
                R.drawable.icon_realtime_vibration_patterns,
                R.drawable.icon_light_patterns,
                R.drawable.icon_saved_patterns,
                R.drawable.icon_demo
        };

        String [] card_names = new String[]{"Library Patterns",
                "Real-Time Patterns",
                "Light Patterns",
                "Saved Patterns",
                "Demo"};

        String [] card_descriptions = new String[]{"Vibration pattern design based on DRV2605L built-in library patterns",
                "Vibration pattern design based on real-time waveforms",
                "Light pattern design based on colour wheel",
                "Saved pattern list and statistics",
                "Test device in standard mode",
                "Sync top rated patterns with device"};

        int [] card_colors = new int[]{
                mContext.getResources().getColor(R.color.card_color1),
                mContext.getResources().getColor(R.color.card_color2),
                mContext.getResources().getColor(R.color.card_color3),
                mContext.getResources().getColor(R.color.card_color4),
                mContext.getResources().getColor(R.color.card_color5)
        };

        features = new ArrayList<>();
        for(int i = 0; i < card_names.length; i++){
            features.add(new Feature(card_drawablesResources[i], card_names[i], card_descriptions[i], card_colors[i]));
        }
    }

}
