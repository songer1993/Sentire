package com.project.songer1993.sentire;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

public class LibraryVibrationPatterns extends AppCompatActivity {

    private BoomMenuButton boomMenuButtonInActionBar;
    private ActionBar mActionBar;
    private boolean init = false;
    private Context mContext;


    Button btnPlay;
    Button btnClear;
    EditText etEffectSequence;
    GridView gridView;
    GridViewCustomAdapter grisViewCustomeAdapter;
    String effectSequence = "";
    boolean played = false;
    private final static int DIALOG=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_vibration_patterns);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);
        }

        etEffectSequence = (EditText)findViewById(R.id.etEffectSequence);

        final Dialog dialog = onCreateDialog(DIALOG);

        gridView=(GridView)findViewById(R.id.gridViewCustom);
        // Create the Custom Adapter Object
        grisViewCustomeAdapter = new GridViewCustomAdapter(mContext);
        // Set the Adapter to GridView
        gridView.setAdapter(grisViewCustomeAdapter);

        // Handling touch/click Event on GridView Item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (played){
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
                    dialog.show();
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
    }
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) {
            case DIALOG:
                AlertDialog.Builder builder=new android.app.AlertDialog.Builder(mContext);
                //builder.setIcon(R.drawable.header);
                builder.setTitle("Save as");

                builder.setSingleChoiceItems(R.array.emotion, 0, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        String emotion = getApplication().getResources().getStringArray(R.array.emotion)[which];
                        switch (emotion){
                            case "Happy":
                                break;
                            case "Sad":
                                break;
                            case "Surprised":
                                break;
                            case "Afraid":
                                break;
                            case "Angry":
                                break;
                            case "Disgusted":
                                break;
                        }
                    }
                });

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog=builder.create();
                break;
        }
        return dialog;
    }

    public class GridViewCustomAdapter extends ArrayAdapter
    {
        Context context;



        public GridViewCustomAdapter(Context context)
        {
            super(context, 0);
            this.context=context;

        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row;

            //if (row == null)
            //{
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.my_grid_row, parent, false);


            TextView textViewTitle = (TextView) row.findViewById(R.id.textView);
            ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView);


            textViewTitle.setText(mEffectDescriptions[position]);
            imageViewIte.setImageResource(mThumbIds[position]);

            //}



            return row;

        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.click0, R.drawable.click0, R.drawable.click0,
                R.drawable.click0, R.drawable.click0, R.drawable.click0,
                R.drawable.bump, R.drawable.bump, R.drawable.bump,
                R.drawable.click1, R.drawable.click1, R.drawable.click2,
                R.drawable.fuzz, R.drawable.buzz0,
                R.drawable.alert, R.drawable.alert,
                R.drawable.click0, R.drawable.click0, R.drawable.click0, R.drawable.click0,
                R.drawable.click0, R.drawable.click0, R.drawable.click0,
                R.drawable.tick, R.drawable.tick, R.drawable.tick,
                R.drawable.click1, R.drawable.click1, R.drawable.click1, R.drawable.click1,
                R.drawable.click1, R.drawable.click1, R.drawable.click1,
                R.drawable.tick, R.drawable.tick, R.drawable.tick,
                R.drawable.click1, R.drawable.click1, R.drawable.click1, R.drawable.click1,
                R.drawable.click1, R.drawable.click1, R.drawable.click1,
                R.drawable.tick, R.drawable.tick, R.drawable.tick,
                R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0,
                R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse,
                R.drawable.click3, R.drawable.click3, R.drawable.click3,
                R.drawable.click3, R.drawable.click3, R.drawable.click3,
                R.drawable.hum, R.drawable.hum, R.drawable.hum,
                R.drawable.hum, R.drawable.hum, R.drawable.hum,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_down_full, R.drawable.ramp_down_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_up_full, R.drawable.ramp_up_full,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_down_half, R.drawable.ramp_down_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.ramp_up_half, R.drawable.ramp_up_half,
                R.drawable.buzz0,
                R.drawable.hum,
                R.drawable.hum,
                R.drawable.hum,
                R.drawable.hum,
                R.drawable.hum
        };

        private String[] mEffectDescriptions = {
                "Strong, 100%", "Strong, 60%", "Strong, 30%",
                "Sharp, 100%", "Sharp, 60%", "Sharp, 30%",
                "Soft, 100%", "Soft, 60%", "Soft, 30%",
                "Double, 100%", "Double, 60%", "Triple, 100%",
                "Soft, 60%", "Strong, 100%",
                "750ms, 100%", "1000ms, 100%",
                "Strong, 100%", "Strong, 80%", "Strong, 60%", "Strong, 30%",
                "Medium, 100%", "Medium, 80%", "Medium, 60%",
                "Sharp, 100%", "Sharp, 80%", "Sharp, 60%",
                "Short Double Strong, 100%", "Short Double Strong, 80%",
                "Short Double Strong, 60%", "Short Double Strong, 30%",
                "Short Double Medium, 100%", "Short Double Medium, 80%", "Short Double Medium, 60%",
                "Short Double Sharp, 100%", "Short Double Sharp, 80%", "Short Double Sharp, 60%",
                "Long Double Strong1 100%", "Long Double Strong, 80%",
                "Long Double Strong3 60%", "Long Double Strong, 30%",
                "Long Double Medium, 100%", "Long Double Medium, 80%", "Long Double Medium, 60%",
                "Long Double Sharp, 100%", "Long Double Sharp, 80%", "Long Double Sharp, 60%",
                "100%", "80%", "60%", "40%", "20%",
                "Strong 1, 100%", "Strong 2, 60%",
                "Medium 1, 100%", "Medium 2, 60%",
                "Sharp 1, 100%", "Sharp 2, 60%",
                "Transition 1, 100%", "Transition 2, 80%", "Transition 3, 60%",
                "Transition 4, 40%", "Transition 5, 20%", "Transition 1, 10%",
                "100%", "80%", "60%",
                "40%", "20%", "10%",
                "Long & Smooth 1", "Long & Smooth 2",
                "Medium & Smooth 1", "Medium & Smooth 2",
                "Short & Smooth 1", "Short & Smooth 2",
                "Long & Sharp 1", "Long & Sharp 2",
                "Sharp 1", "Medium & Sharp 2",
                "Short & Sharp 1", "Short & Sharp 2",
                "Long & Smooth 1", "Long & Smooth 2",
                "Medium & Smooth 1", "Medium & Smooth 2",
                "Short & Smooth 1", "Short & Smooth 2",
                "Long & Sharp 1", "Long & Sharp 2",
                "Medium & Sharp1", "Medium & Sharp 2",
                "Short & Sharp 1", "Short & Sharp 2",
                "Long & Smooth 1", "Long & Smooth 2",
                "Medium & Smooth 1", "Medium & Smooth 2",
                "Short & Smooth 1%", "Short & Smooth 2",
                "Long & Sharp 1", "Long & Sharp 2",
                "Sharp 1", "Medium & Sharp 2",
                "Short & Sharp 1", "Short & Sharp 2",
                "Long & Smooth 1", "Long & Smooth 2",
                "Medium & Smooth 1", "Medium & Smooth 2",
                "Short & Smooth 1", "Short & Smooth 2",
                "Long & Sharp 1", "Long & Sharp 2",
                "Medium & Sharp1", "Medium & Sharp 2",
                "Short & Sharp 1", "Short & Sharp 2",
                "Long, 100%",
                "Smooth, 50%",
                "Smooth, 40%",
                "Smooth, 30%",
                "Smooth, 20%",
                "Smooth, 10%",
        };




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
                                intent = new Intent(mContext, RealtimeVibrationPatterns.class);
                                break;
                            case 1:
                                intent = new Intent(mContext, LightPatterns.class);
                                break;
                            case 2:
                                intent = new Intent(mContext, SavedPatterns.class);
                                break;
                            case 3:
                                intent = new Intent(mContext, Demo.class);
                                break;
                            default:
                                intent = new Intent(mContext, LibraryVibrationPatterns.class);
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
