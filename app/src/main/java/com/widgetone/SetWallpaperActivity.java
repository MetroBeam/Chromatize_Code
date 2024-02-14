package com.widgetone;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SetWallpaperActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "";
    private String fileData = "";

    private int loggednotificationTarget = 3;
    private int appChoiceTarget = 5;
    private int inactivityTarget = 4;
    private int compareTarget = 4;
    FileManager fileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_wallpaper);



        fileManager= new FileManager(this);

        final ImageView quarterOne = findViewById(R.id.first_quarter);
        final ImageView quarterTwo = findViewById(R.id.second_quarter);
        final ImageView quarterThree = findViewById(R.id.third_quarter);
        final ImageView quarterFour = findViewById(R.id.fourth_quarter);


        String notificationsSchedule = fileManager.readInternalStringFile("notification_schedule");
        final String [] notificationsArray = notificationsSchedule.split("~");



                String colourList [] = fileManager.readInternalStringFile("recorded_colours_history").split("~");

        int colorOne = Integer.parseInt(colourList[2]);




        if (colourList.length > 10) {


            if (!colourList[2].equals("999999")) {
                quarterOne.setBackgroundColor(Integer.parseInt(colourList[2]));
            }

            if (!colourList[5].equals("999999")) {
                quarterTwo.setBackgroundColor(Integer.parseInt(colourList[5]));
            }

            if (!colourList[8].equals("999999")) {
                quarterThree.setBackgroundColor(Integer.parseInt(colourList[8]));
            }

            if (!colourList[11].equals("999999")) {
                quarterFour.setBackgroundColor(Integer.parseInt(colourList[11]));
            }

            if (colourList[2].equals("999999")) {
            quarterOne.setBackgroundColor(Color.WHITE);
            }
            if (colourList[5].equals("999999")) {
                quarterTwo.setBackgroundColor(Color.WHITE);
            }
            if (colourList[8].equals("999999")) {
                quarterThree.setBackgroundColor(Color.WHITE);
            }
            if (colourList[11].equals("999999")) {
                quarterFour.setBackgroundColor(Color.WHITE);
            }

        }

        final WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
    // I could probably remove this completely

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                RelativeLayout view = findViewById(R.id.wallpaper);

                int width = view.getWidth();

                Bitmap b = setViewToBitmapImage(view);

                try {

                    myWallpaperManager.setBitmap(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(context, MainActivity.class);


                if (Integer.parseInt(notificationsArray[6]) > 22) {

                    if (Integer.parseInt(notificationsArray[4]) > loggednotificationTarget - 1) {
                        notificationsArray[4] = "0";
                        intent.putExtra("logged_note", "logged_note");
                    } else {


                        if (Integer.parseInt(notificationsArray[5]) > appChoiceTarget - 1) {
                            notificationsArray[5] = "0";
                            sendNotification("widget_choice");
                            intent.putExtra("widget_choice", "widget_choice");

                        }
                    }

                }

                else if  ((Integer.parseInt(notificationsArray[6]) == 4)||(Integer.parseInt(notificationsArray[6]) == 5)|| (Integer.parseInt(notificationsArray[6]) == 11)||(Integer.parseInt(notificationsArray[6]) == 12)) {
                     if (Integer.parseInt(notificationsArray[0]) > compareTarget - 1) {
                    // need to re-add this
                    notificationsArray[0] = "0";
                    //sendNotification("colour_logged");
                    intent.putExtra("comparing_widgets", "comparing_widgets");
                     }

                }

                else  {
                    Log.d("other cases", "onFinish: ");
                    if (Integer.parseInt(notificationsArray[1])> inactivityTarget-1) {
                        intent.putExtra("inactivity", "inactivity");
                        notificationsArray[4] = "2";


                    }
                    else {
                        if (Integer.parseInt(notificationsArray[4]) > loggednotificationTarget - 1) {
                            notificationsArray[4] = "0";
                            intent.putExtra("logged_note", "logged_note");
                        }

                    }
                }

                 String scheduleReplacement ="";

                notificationsArray[1] = "0";
                for (int x = 0; x < notificationsArray.length; x++) {
                    scheduleReplacement = scheduleReplacement + notificationsArray[x] + "~";
                }
                fileManager.createInternalStringFile(scheduleReplacement, "notification_schedule");




                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        }.start();



    }


    public static Bitmap setViewToBitmapImage(View view) {
//Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    public static void setBackgroundColorAndRetainShape(final int color, final Drawable background) {

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background.mutate()).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background.mutate()).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background.mutate()).setColor(color);
        } else {
        }

    }




    public void sendNotification (String notificationType) {
        Intent notificationIntent = new Intent(this, DiaryActivity.class);

        notificationIntent.putExtra(notificationType, "notification");


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
        Log.d("notification", "sendNotification: ");

    }


}