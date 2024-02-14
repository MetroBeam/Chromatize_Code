package com.widgetone;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class CaptureConfigActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "";
    private String fileData = "";

    public static final String VALUES_SHARED_PREFS = "values_prefs";
    public static final String CAPTURE_KEY_TEXT = "capture_key_text";

    final ArrayList<Item> backgroundColorList = new ArrayList<>();

    private RecyclerView recyclerView;

    //Context context;


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editText;
    private int lastClicked;
    private ImageView confirmText;
    private int clicked = 0;
    FileManager fileManager;
    private ImageView tapView;
    private int nTaps;

    private boolean editTextSelected = false;
    private int topicChangeTarget = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_config_layout);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();


        if (extras != null) {
            Log.d("captureID", Integer.toString(appWidgetId));
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d("captureID", Integer.toString(appWidgetId));

        }

        fileManager = new FileManager(this);



        /*
        "to xomplete the activity we need this resultvalue stuff " I think it;s the instructions to do something. Canceled is usually the default mode
        but some phones don't have it. So by making ti explicit we set it outselves.
        */

        editText = findViewById(R.id.edit_text);



        String  topicsHistoryString = "";
        topicsHistoryString = fileManager.readInternalStringFile("topics_history");
        int titleHistoryLength = topicsHistoryString.length();

        if (topicsHistoryString.length()>0) {
            String[] flowerTopicArray = topicsHistoryString.split("~");
            String lastTitle = flowerTopicArray[2];
            if (!lastTitle.equals("no topic")) {
                editText.setText(lastTitle);
            }
            else {
                editText.setText("");
            }
        }

        else {
            editText.setHint("set your topic...");
        }




        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }




        editText.requestFocus();



        final ImageView confirmbutton = findViewById(R.id.capture_config_confirm_button);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if(editText.getText().toString().equals("set your topic...")) {
                    editText.setText("");
                }
                return false;
            }


        });



        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmbutton.setVisibility(View.VISIBLE);
                confirmConfiguration();
            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextSelected = true;

                String enteredText = editText.getText().toString();

                if (enteredText.length()>0) {

                    if (enteredText.charAt(enteredText.length() - 1) == '~') {
                        enteredText = enteredText.replace(enteredText.substring(enteredText.length() - 1), "");
                        editText.setText(enteredText);
                        Toast.makeText(CaptureConfigActivity.this, "Sorry, you cannot use the ~ symbol", Toast.LENGTH_SHORT).show();
                        editText.setSelection(editText.getText().length());

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    public void confirmConfiguration() {
        String notificationsSchedule = fileManager.readInternalStringFile("notification_schedule");
        String[] notificationsArray = notificationsSchedule.split("~");


        String timeString = Calendar.getInstance().getTime().toString();


        String fileText ="";
        String displayedText = "";

        fileText= editText.getText().toString();

        if (fileText.length()<1) {
            Log.d("aha", "confirmConfiguration: ");
            displayedText= " ";
            fileText = "no topic";

        }

        else {
            displayedText = fileText;
        }


        String  topicsHistoryString = "";
        topicsHistoryString= fileManager.readInternalStringFile("topics_history");

        fileManager.createInternalStringFile( Integer.toString(appWidgetId) + "~" + timeString + "~" + fileText + "~" + topicsHistoryString , "topics_history");


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);


        Intent selectIntent = new Intent(this, MainActivity.class);
        selectIntent.putExtra("capture", "capture");
        PendingIntent pendingIntentSelect = PendingIntent.getActivity(this,
                appWidgetId, selectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent configIntent = new Intent(this, CaptureConfigActivity.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(this,0,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent penIntent = new Intent(this, DiaryActivity.class);
        PendingIntent penPendingIntent = PendingIntent.getActivity(this,0,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.capture_app_widget);
        views.setOnClickPendingIntent(R.id.pen,penPendingIntent);
        views.setOnClickPendingIntent(R.id.capture_icon, pendingIntentSelect);
        views.setOnClickPendingIntent(R.id.settings, configPendingIntent);
        views.setCharSequence(R.id.topic_text, "setText", displayedText);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        SharedPreferences sharedPreferences = getSharedPreferences(VALUES_SHARED_PREFS + String.valueOf(appWidgetId), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CAPTURE_KEY_TEXT+ String.valueOf(appWidgetId), displayedText);
        // this could be putInt, which would remove thge need to change it to a string etc. need to verify
        editor.apply();


        appWidgetManager = AppWidgetManager
                .getInstance(CaptureConfigActivity.this);
        ComponentName thisAppWidget = new ComponentName(
                CaptureConfigActivity.this, CaptureWidget.class);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(thisAppWidget);
        Intent startBroadcast = new Intent(CaptureConfigActivity.this,
                CaptureWidget.class);
        //startBroadcast.putParcelableArrayListExtra("list", newsList);
        startBroadcast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                appWidgetIds);
        startBroadcast.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(startBroadcast);



        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);


        if (editTextSelected) {

            Log.d("indapocket", "confirmConfiguration: ");
            // if (Integer.parseInt(notificationsArray[6]) > 22) {
            if (Integer.parseInt(notificationsArray[2]) > topicChangeTarget - 1) {
                String scheduleReplacement = "";
                notificationsArray[2] = "0";

                // sendTopicSetChangedNotification();
                for (int x = 0; x < notificationsArray.length; x++) {
                    scheduleReplacement = scheduleReplacement + notificationsArray[x] + "~";
                }
                fileManager.createInternalStringFile(scheduleReplacement, "notification_schedule");
                Intent diaryIntent = new Intent(CaptureConfigActivity.this, DiaryActivity.class);
                diaryIntent.putExtra("topic_changed", "topic_note");
                startActivity(diaryIntent);
            }
            //}
        }
        finish();



    }

    public void sendTopicSetChangedNotification () {
        Intent notificationIntent = new Intent(this, DiaryActivity.class);
        notificationIntent.putExtra("topic_changed", "topic_changed_note");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this); // is this the best way?
        stackBuilder.addParentStack(DiaryActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = builder.setContentTitle("Question from Matt")
                .setContentText("Questions from Matt")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher) // set image of Me as image.
                .setContentIntent(pendingIntent).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
