package com.widgetone;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.widgetone.MultipleConfigActivity.KEY_COLOR_FIVE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_FOUR;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_ONE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_THREE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_TWO;

import static com.widgetone.MultipleConfigActivity.VALUES_SHARED_PREFS;

public class MultipleMainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_multiple_main);

        final Context context = this;


        fileManager = new FileManager(this);
        TextView selectedColourView = findViewById(R.id.selected_colour);
        Intent intent = getIntent();


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        if (getIntent().hasExtra("multiple")) {


            final String fullExtra = intent.getStringExtra("multiple");

            final String buttonIndex = fullExtra.substring(0, fullExtra.indexOf(","));
            final String widgetId = fullExtra.substring(fullExtra.indexOf(",") + 1, fullExtra.length());

            Log.d("widgetId", widgetId);



            SharedPreferences valuePreferences = context.getSharedPreferences(VALUES_SHARED_PREFS + String.valueOf(widgetId), Context.MODE_PRIVATE);
            String colorOneInt = valuePreferences.getString(KEY_COLOR_ONE + String.valueOf(widgetId), "0");
            String colorTwoInt = valuePreferences.getString(KEY_COLOR_TWO + String.valueOf(widgetId), "0");
            String colorThreeInt = valuePreferences.getString(KEY_COLOR_THREE + String.valueOf(widgetId), "0");
            String colorFourInt = valuePreferences.getString(KEY_COLOR_FOUR + String.valueOf(widgetId), "0");
            String colorFiveInt = valuePreferences.getString(KEY_COLOR_FIVE + String.valueOf(widgetId), "0");

            switch (buttonIndex) {
                case "0":
                    selectedColour = colorOneInt;
                    break;
                case "1":
                    selectedColour = colorTwoInt;
                    break;
                case "2":
                    selectedColour = colorThreeInt;
                    break;
                case "3":
                    selectedColour = colorFourInt;
                    break;
                case "4":
                    selectedColour = colorFiveInt;
                    break;

            }


            //  selectedColourView.setBackgroundColor(Integer.parseInt(selectedColour));
        }

           /* tick.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        */
        String timeString;

        //fileName = "recorded_colours_history";


        timeString = Calendar.getInstance().getTime().toString();
        //String currentFileString = askPermissionAndReadExtermalFile();
        String currentFileString = fileManager.readInternalStringFile("recorded_colours_history");

        //fileData = timeString + "," + selectedColour + "," + currentFileString;

        fileManager.createInternalStringFile(timeString + "~" + "Multiple~" + selectedColour + "~" + currentFileString, "recorded_colours_history");


        //askPermissionAndWriteExternalFile();

        Intent nextActivity;
        nextActivity = new Intent(context, SetWallpaperActivity.class);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nextActivity);
        finish();


        //  }
        // });


       /*     cross.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
    });
    */


    }
}







