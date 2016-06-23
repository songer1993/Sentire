package com.project.songer1993.sentire;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class BrowsePatterns extends AppCompatActivity {


    private String mEmotion;
    private BarChart mBarChartVibrationPattern, mBarChartLightPattern;
    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<VibrationPattern> mVibrationPatternTable;
    private MobileServiceTable<LightPattern> mLightPatternTable;

    //Offline Sync
    /**
     * Mobile Service Table used to access and Sync data
     */
    //private MobileServiceSyncTable<ToDoItem> mToDoTable;

    /**
     * Adapter to sync the items list with the view
     */
    private VibrationPatternAdapter mVibrationPatternAdapter;
    private LightPatternAdapter mLightPatternAdapter;


    /**
     * EditText containing the "New To Do" text
     */
    private EditText mTextNewToDo;

    /**
     * Progress spinner to use for table operations
     */
    private ProgressBar mProgressBar;

    /**
     * Initializes the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_patterns);


        mEmotion = getIntent().getExtras().getString("emotion");
        setTitle((mEmotion+" Pattern List"));

        mBarChartVibrationPattern = (BarChart) findViewById(R.id.barchartVibrationPattern);
        mBarChartLightPattern = (BarChart) findViewById(R.id.barchartLightPattern);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        // Initialize the progress bar
        mProgressBar.setVisibility(ProgressBar.GONE);

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient =
                    new MobileServiceClient(
                            Constants.MOBILE_SERVICE_URL,
                            Constants.APPLICATION_KEY,
                            this).withFilter(new ProgressFilter());

            // Get the Mobile Service Table instance to use
            mVibrationPatternTable = mClient.getTable(VibrationPattern.class);
            mLightPatternTable = mClient.getTable(LightPattern.class);

            //Init local storage
            initLocalStore().get();

            // Create an adapter to bind the items with the view
            mVibrationPatternAdapter = new VibrationPatternAdapter(this, R.layout.pattern_list_row);
            mLightPatternAdapter = new LightPatternAdapter(this, R.layout.pattern_list_row);

            ListView listViewVibrationPattern = (ListView) findViewById(R.id.listViewVibrationPattern);
            listViewVibrationPattern.setAdapter(mVibrationPatternAdapter);


            ListView listViewLightPattern = (ListView) findViewById(R.id.listViewLightPattern);
            listViewLightPattern.setAdapter(mLightPatternAdapter);


            // Load the items from the Mobile Service
            refreshPatternsFromTable();

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
    }

    /**
     * Initializes the activity menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    /**
     * Select an option from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshPatternsFromTable();
        }

        return true;
    }

    private void refreshPatternsFromTable() {
        refreshVibrationPatternsFromTable();
        refreshLightPatternsFromTable();
    }

    /**
     * Mark an item as completed
     *
     * @param vibrationPattern
     *            The item to mark
     */
    public void updateVibrationPattern(final VibrationPattern vibrationPattern) {
        if (mClient == null) {
            return;
        }

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    updateVibrationPatternInTable(vibrationPattern);
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }

    /**
     * Mark an item as completed in the Mobile Service Table
     *
     * @param vibrationPattern
     *            The item to mark
     */
    public void updateVibrationPatternInTable(VibrationPattern vibrationPattern) throws ExecutionException, InterruptedException {
            mVibrationPatternTable.update(vibrationPattern).get();
    }

    /**
     * Mark an item as completed
     *
     * @param lightPattern
     *            The item to mark
     */
    public void updateLightPattern(final LightPattern lightPattern) {
        if (mClient == null) {
            return;
        }

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    updateLightPatternInTable(lightPattern);
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }

    /**
     * Mark an item as completed in the Mobile Service Table
     *
     * @param lightPattern
     *            The item to mark
     */
    public void updateLightPatternInTable(LightPattern lightPattern) throws ExecutionException, InterruptedException {
        mLightPatternTable.update(lightPattern).get();
    }

    /**
     * Refresh the list with the items in the Table
     */
    private void refreshVibrationPatternsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<VibrationPattern> vibrationPatterns = refreshVibrationPatternsFromMobileServiceTable();
                    System.out.println(vibrationPatterns.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mVibrationPatternAdapter.clear();
                            mBarChartVibrationPattern.clearChart();

                            for (VibrationPattern vibrationPattern : vibrationPatterns) {
                                mVibrationPatternAdapter.add(vibrationPattern);
                                int barColor = determineBarColor(vibrationPattern.getScore());
                                mBarChartVibrationPattern.addBar(new BarModel(vibrationPattern.getScore(), barColor));
                            }

                            mBarChartVibrationPattern.startAnimation();
                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }
    /**
     * Refresh the list with the items in the Table
     */
    private void refreshLightPatternsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<LightPattern> lightPatterns = refreshLightPatternsFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLightPatternAdapter.clear();
                            mBarChartLightPattern.clearChart();

                            for (LightPattern lightPattern : lightPatterns) {
                                mLightPatternAdapter.add(lightPattern);
                                int barColor = determineBarColor(lightPattern.getScore());
                                mBarChartLightPattern.addBar(new BarModel(lightPattern.getScore(), barColor));
                            }
                            mBarChartLightPattern.startAnimation();

                        }
                    });
                } catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<VibrationPattern> refreshVibrationPatternsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mVibrationPatternTable.where().startsWith("emotion", mEmotion).execute().get();
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private List<LightPattern> refreshLightPatternsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mLightPatternTable.where().startsWith("emotion", mEmotion).execute().get();
    }

    /**
     * Initialize local storage
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("emotion", ColumnDataType.String);
                    tableDefinition.put("type", ColumnDataType.String);
                    tableDefinition.put("value", ColumnDataType.String);
                    tableDefinition.put("score", ColumnDataType.Integer);
                    tableDefinition.put("score_happy", ColumnDataType.Integer);
                    tableDefinition.put("score_sad", ColumnDataType.Integer);
                    tableDefinition.put("score_fearful", ColumnDataType.Integer);
                    tableDefinition.put("score_angry", ColumnDataType.Integer);
                    tableDefinition.put("score_neutral", ColumnDataType.Integer);



                    localStore.defineTable("Patterns", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
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

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }

    private int determineBarColor(int score){
        int barColor;

        if(score<0)
            barColor = 0xFF123456;
        else if (score < 5)
            barColor = 0xFF343456;
        else if (score < 10)
            barColor = 0xFF563456;
        else if (score < 20)
            barColor = 0xFF873F56;
        else if (score < 30)
            barColor = 0xFF56B7F1;
        else if (score < 40)
            barColor = 0xFF343456;
        else if (score < 50)
            barColor = 0xFF1FF4AC;
        else
            barColor = 0xFF1BA4E6;

        return barColor;
    }
}