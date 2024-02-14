package com.widgetone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private FileManager fileManager;


    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "";
    private String fileData = "";


    AlarmManager alarmManager;

    private String selectedColour = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fileManager = new FileManager(this);
        String notificationSchedule = "";

        notificationSchedule = fileManager.readInternalStringFile("notification_schedule");
        String[] scheduleArray = notificationSchedule.split("~");
        String scheduleReplacement = "";
        if (notificationSchedule.length() == 0) {
            String firstSchedule = "0~" + "0~" + "0~" + "0~" + "2~" + "0~" + "0~"; //comparison, inactivity, topic, colours, logged, choice, overall
            fileManager.createInternalStringFile(firstSchedule, "notification_schedule");
           // startAlarms();
            Log.d("alarms_started", "onCreate: ");
        }

       int lastDate = 0;
        if (fileManager.readInternalStringFile("last_date").length()>0){
            lastDate = Integer.parseInt(fileManager.readInternalStringFile("last_date"));
            int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (lastDate!=date) {


                if (date<lastDate) {
                    Log.d("date", Integer.toString(Calendar.getInstance().get(Calendar.MONTH)));

                    if (Calendar.getInstance().get(Calendar.MONTH) == 10) {
                        date = date + 31;
                    }
                    if (Calendar.getInstance().get(Calendar.MONTH) == 11) {
                        date = date + 30;
                    }
                }

                int differential = date - lastDate;

                for (int x = 0; x < scheduleArray.length; x++) {
                    Log.d("logged", "logged");
                    int replacementInt = Integer.parseInt(scheduleArray[x]);
                    replacementInt = replacementInt + differential;
                    scheduleReplacement = scheduleReplacement + replacementInt + "~";
                }

                fileManager.createInternalStringFile(scheduleReplacement,"notification_schedule");
                String newDate = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                fileManager.createInternalStringFile(newDate, "last_date");

            }


        }
        else {
            String date = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            fileManager.createInternalStringFile(date,"last_date");
        }

        startAlarms();
        Intent intent = getIntent();

        String extra = "";


        if (getIntent().hasExtra("multiple")) {
            extra = "multiple";

            final String fullExtra = intent.getStringExtra("multiple");
            Log.d("fullExtra", fullExtra);

            Intent multipleActivityIntent = new Intent(MainActivity.this, MultipleMainActivity.class);

            multipleActivityIntent.putExtra("multiple", fullExtra);
            startActivity(multipleActivityIntent);
        }

        if (getIntent().hasExtra("flower")) {
            extra = "flower";
            
            Intent flowerActivityIntent = new Intent(MainActivity.this, FlowerMainActivity.class);
            startActivity(flowerActivityIntent);
        }


        if (getIntent().hasExtra("capture")) {
            extra = "capture";

            Intent flowerActivityIntent = new Intent(MainActivity.this, CaptureMainActivity.class);
            startActivity(flowerActivityIntent);
        }


        if (getIntent().hasExtra("logged_note")) {


            Intent loggedIntent = new Intent(MainActivity.this, DiaryActivity.class);
            loggedIntent.putExtra("colour_logged", "notification");
            startActivity(loggedIntent);
        }

        if (getIntent().hasExtra("comparing_widgets")) {


            Intent compareIntent = new Intent(MainActivity.this, DiaryActivity.class);
            compareIntent.putExtra("comparing_widgets", "comparing_widgets");
            startActivity(compareIntent);
        }

        if (getIntent().hasExtra("inactivity")) {

            Intent inactivityIntent = new Intent(MainActivity.this, DiaryActivity.class);
            inactivityIntent.putExtra("inactivity", "inactivity");
            startActivity(inactivityIntent);
        }
        Log.d("extra", extra);

        finish();
        return; // add this to prevent from doing unnecessary stuffs



    }

    public void workerRequest() {

        WorkManager mWorkManager = WorkManager.getInstance();

       //OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
    }


    public void startAlarms() {

        Log.d("loghouse", "startAlarms: ");
        Intent alarmIntent = new Intent("android.media.action.DISPLAY_REGULAR_NOTIFICATION");
        alarmIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent regularPendingIntent = PendingIntent.getBroadcast(this,200, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final int random = new Random().nextInt((16 - 11) + 1) + 11;

        Log.d("alarmTime", Integer.toString(random));

        PendingIntent sender = PendingIntent.getBroadcast(this,200,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(sender);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, random); // change to 19
        calendar.set(Calendar.MINUTE, 00);


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*10 , regularPendingIntent);

    }


}










