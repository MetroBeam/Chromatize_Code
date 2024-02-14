package com.widgetone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SampleBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Context c = context;
            //StartAlarms(c);

        }
    }

        public void StartAlarms (Context context){

            Log.d("loghouse", "startAlarms: ");
            Toast.makeText(context, "alrmsSressafa", Toast.LENGTH_LONG).show();
            Intent alarmIntent = new Intent("android.media.action.DISPLAY_REGULAR_NOTIFICATION");
            alarmIntent.addCategory("android.intent.category.DEFAULT");
            PendingIntent regularPendingIntent = PendingIntent.getBroadcast(context, 200, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            // Set the alarm to start at 8:30 a.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 16); // change to 19
            calendar.set(Calendar.MINUTE, 00);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60,  regularPendingIntent);

            Toast.makeText(context, "this", Toast.LENGTH_SHORT).show();


        }
    }


