package com.widgetone;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RegularAlarmReceiver extends BroadcastReceiver {

    private String fileName = "";
    private String fileData = "";
    String notificationType = "";
    boolean indexRegistered = false;
    FileManager fileManager;
    private int inactivityTarget = 3;
    private int generalTarget = 3;
    Random r = new Random();


    @Override
    public void onReceive(Context context, Intent intent) {

        final int random = r.nextInt((4 - 1) + 1) + 1;

        Log.d("random", Integer.toString(random));
        if (random == 1) {


            Intent notificationIntent = new Intent(context, DiaryActivity.class);
            notificationIntent.putExtra("general_experience", "general_note");

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context); // is this the best way?
            stackBuilder.addParentStack(DiaryActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle("Question from Matt")
                    .setContentText("Questions from Matt")
                    .setTicker("New Message Alert!")
                    .setSmallIcon(R.mipmap.ic_launcher) // set image of Me as image.
                    .setContentIntent(pendingIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            notificationManager.notify(0, notification);
        }
    }


    }





