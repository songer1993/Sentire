package com.project.songer1993.sentire;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by songer1993 on 06/04/2016.
 */
public class MyPatternCard extends CardWithList {

    private String mEmotion;
    private BarChart mBarChart;
    private MobileServiceClient mClient;
    private MobileServiceTable<VibrationPattern> mVibrationPatternTable;
    private MobileServiceTable<LightPattern> mLightPatternTable;
    private List<VibrationPattern> mVibrationPatterns;
    private List<LightPattern> mLightPatterns;

    @Override
    public void init() {
        try {
            mClient = new MobileServiceClient(
                    "https://songerarduinotest.azure-mobile.net/",
                    "IBmOdZkslBSsjrCkJeQNvpjHOpTQYr42",
                    getContext()
            );

            mVibrationPatternTable = mClient.getTable(VibrationPattern.class);
            mLightPatternTable = mClient.getTable(LightPattern.class);
            //refreshFromTable();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        super.init();
    }

    /**
     * Refresh the list with the items in the Table
     */
    private void refreshFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    mVibrationPatterns = refreshFromVibrationPatternTable();
                    mLightPatterns = refreshFromLightPatternTable();
                    for(VibrationPattern mVibrationPattern: mVibrationPatterns){
                    PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
                    patternListObject.id = mVibrationPattern.getId();
                    patternListObject.emotion = mVibrationPattern.getEmotion();
                    patternListObject.type = mVibrationPattern.getType();
                    patternListObject.name = mVibrationPattern.getName();
                    patternListObject.value = mVibrationPattern.getValue();
                    patternListObject.score = mVibrationPattern.getScore();
                    mLinearListAdapter.add(patternListObject);
                    }


                    for(LightPattern mLightPattern: mLightPatterns){
                    PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
                    patternListObject.emotion = mLightPattern.getEmotion();
                    patternListObject.type = mLightPattern.getType();
                    patternListObject.name = mLightPattern.getName();
                    patternListObject.value = mLightPattern.getValue();
                    patternListObject.score = mLightPattern.getScore();
                    mLinearListAdapter.add(patternListObject);
                    }

                } catch (final Exception e){
                    System.out.println(e);
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    /**
     * Run an ASync task on the corresponding executor
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public MyPatternCard(Context context, String emotion) {
        super(context);
        this.mEmotion = emotion;
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext(), R.layout.pattern_card_header){

            @Override
            public void setupInnerViewElements(ViewGroup parent, View view) {
                super.setupInnerViewElements(parent, view);
                mBarChart = (BarChart) view.findViewById(R.id.barchart);
                mBarChart.setOnBarClickedListener(new IOnBarClickedListener() {
                    @Override
                    public void onBarClicked(int _Position) {
                        Toast.makeText(getContext(), ("Pattern " + new Integer(_Position+1).toString()), Toast.LENGTH_SHORT).show();
                    }
                });
                mBarChart.startAnimation();
            }
        };

        //Add a popup menu. This method set OverFlow button to visible
        header.setPopupMenu(R.menu.popup_menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_refresh:
                        //Add an object to the list
//                        PatternListObject v1= new PatternListObject(MyPatternCard.this);
//                        v1.name ="Pattern1";
//                        v1.type = "library";
//                        v1.value = "2: 12, 32, 44, 53, ";
//                        v1.score = 12;
//                        v1.setObjectId(v1.name); //It can be important to set ad id
//                        mLinearListAdapter.add(v1);
//                        mBarChart.addBar(new BarModel(v1.score, 0xFF123456));
                        refreshFromTable();
                        try {
                            // Create the Mobile Service Client instance, using the provided

                            // Mobile Service URL and key

                            // Get the Mobile Service Table instance to use


                            // Load the items from the Mobile Service

//                            PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
//                            patternListObject.id = mVibrationPatterns.get(0).getId();
//                            patternListObject.emotion = mVibrationPatterns.get(0).getEmotion();
//                            patternListObject.type = mVibrationPatterns.get(0).getType();
//                            patternListObject.name = mVibrationPatterns.get(0).getName();
//                            patternListObject.value = mVibrationPatterns.get(0).getValue();
//                            patternListObject.score = mVibrationPatterns.get(0).getScore();

//                            for(VibrationPattern mVibrationPattern: mVibrationPatterns){
//                                PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
//                                patternListObject.id = mVibrationPattern.getId();
//                                patternListObject.emotion = mVibrationPattern.getEmotion();
//                                patternListObject.type = mVibrationPattern.getType();
//                                patternListObject.name = mVibrationPattern.getName();
//                                patternListObject.value = mVibrationPattern.getValue();
//                                patternListObject.score = mVibrationPattern.getScore();
//                                mLinearListAdapter.add(patternListObject);
//                            }
//
//
//                            for(LightPattern mLightPattern: mLightPatterns){
//                                PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
//                                patternListObject.id = mLightPattern.getId();
//                                patternListObject.emotion = mLightPattern.getEmotion();
//                                patternListObject.type = mLightPattern.getType();
//                                patternListObject.name = mLightPattern.getName();
//                                patternListObject.value = mLightPattern.getValue();
//                                patternListObject.score = mLightPattern.getScore();
//                                mLinearListAdapter.add(patternListObject);
//                            }

                        } catch (Exception e){
                        }
                        Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        header.setTitle(mEmotion); //should use R.string.

        return header;
    }

    @Override
    protected void initCard() {

        //Set the whole card as swipeable
        setSwipeable(true);
        setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected List<ListObject> initChildren() {

        //Init the list
        List<ListObject> mObjects = new ArrayList<ListObject>();

//        for(VibrationPattern mVibrationPattern: mVibrationPatterns){
//            PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
//            patternListObject.id = mVibrationPattern.getId();
//            patternListObject.emotion = mVibrationPattern.getEmotion();
//            patternListObject.type = mVibrationPattern.getType();
//            patternListObject.name = mVibrationPattern.getName();
//            patternListObject.value = mVibrationPattern.getValue();
//            patternListObject.score = mVibrationPattern.getScore();
//            mObjects.add(patternListObject);
//        }
//
//
//        for(LightPattern mLightPattern: mLightPatterns){
//            PatternListObject patternListObject = new PatternListObject(MyPatternCard.this);
//            patternListObject.id = mLightPattern.getId();
//            patternListObject.emotion = mLightPattern.getEmotion();
//            patternListObject.type = mLightPattern.getType();
//            patternListObject.name = mLightPattern.getName();
//            patternListObject.value = mLightPattern.getValue();
//            patternListObject.score = mLightPattern.getScore();
//            mObjects.add(patternListObject);
//        }

        if(!mObjects.isEmpty())
            return mObjects;
        else
            return null;
    }


    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<LightPattern> refreshFromLightPatternTable() throws ExecutionException, InterruptedException {
        return mLightPatternTable.where().startsWith("emotion", mEmotion).execute().get();
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<VibrationPattern> refreshFromVibrationPatternTable() throws ExecutionException, InterruptedException {
        return mVibrationPatternTable.where().startsWith("emotion", mEmotion).execute().get();

    }



    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView tvName = (TextView) convertView.findViewById(R.id.tvPatternName);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvPatternValue);
        final TextView tvScore = (TextView) convertView.findViewById(R.id.tvPatternScore);
        Button btnPlus = (Button) convertView.findViewById(R.id.btnPlus);
        Button btnMinus = (Button) convertView.findViewById(R.id.btnMinus);

        //Retrieve the values from the object
        final PatternListObject patternListObject = (PatternListObject)object;
        tvName.setText(patternListObject.name);
        tvValue.setText(patternListObject.value);
        tvScore.setText(new Integer(patternListObject.score).toString() + "pt");
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternListObject.score += 1;
                tvScore.setText(new Integer(patternListObject.score).toString() + "pt");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternListObject.score -= 1;
                tvScore.setText(new Integer(patternListObject.score).toString() + "pt");
            }
        });
        return  convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.pattern_card_inner_main;
    }


    public void updateItems(ArrayList myList) {

        //Update the array inside the card
        ArrayList<PatternListObject> objs = new ArrayList<PatternListObject>();
        getLinearListAdapter().addAll(objs);

        //use this line if your are using the progress bar
        //updateProgressBar(true,true);
    }

    public class PatternListObject extends DefaultListObject{

        public String id;
        public String name;
        public String emotion;
        public String type;
        public String value;
        public int score;

        public PatternListObject(Card parentCard){
            super(parentCard);
            init();
        }

        private void init(){
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipe(ListObject object, boolean dismissRight) {
                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
