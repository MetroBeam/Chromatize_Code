package com.widgetone;

import
        android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ColorOverView extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;


    private int[] colours;
    private ArrayList colorHistoryList;
    private String lastDate = "";
    private String lastTopic = "";
    int entryPos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_history_grid);

        FrameLayout cross = findViewById(R.id.cross_frame);


        FileManager fileManager = new FileManager(this);


        String [] fullRecord = fileManager.readInternalStringFile("recorded_colours_history").split("~");
        int arraySize = fullRecord.length; // I should really divide this by two and round to a whole
        colours = new int[arraySize];
        colorHistoryList = new ArrayList<GridItem>();

        String topicHistoryArray [] = fileManager.readInternalStringFile("topics_history").split("~");

        lastDate =  fullRecord[0].substring(0,3);

        for (int x = 0; x<fullRecord.length -3; x= x+3) {

            int color = Integer.parseInt(fullRecord[x+2]);
            String loggedDate = fullRecord[x].substring(0,3);
            String dateToSend ="";
            String widget = fullRecord[x+1];
            if (loggedDate.equals(lastDate)) {


                String topic = "";
               // int d = topicDates.length-2;

                String topicSetDate = "";
                String dateToDisplay ="";

                for (int d = 1; d<topicHistoryArray.length-1; d=d+3) {
                    topicSetDate= topicHistoryArray[d];
                    topic = topicHistoryArray[d+1];

                    int startingPos = 0;
                    Log.d("length", Integer.toString(colorHistoryList.size()));

               /*     if (colorHistoryList.size()%4 == 0) {
                        Log.d("Pos", "ladstPos");
                        entryPos = 4;
                    }

                    if ((colorHistoryList.size()-1)%4 == 0) {
                        Log.d("Pos", "firsttPos");
                        entryPos = 1;
                    }


                    if ((colorHistoryList.size()-2)%4 == 0) {
                        Log.d("Pos", "secondtPos");
                        entryPos = 2;
                    }


                    if ((colorHistoryList.size()-3)%4 == 0) {
                        Log.d("Pos", "thirdPos");
                        entryPos = 3;
                    }

*/


                    if (isTopicBefore(topicSetDate, fullRecord[x])) {
                       if (!topic.equals(lastTopic)) {

                           String topicSentence [] = topicHistoryArray[d+1].split(" ");


                           for (int b =0; b< topicSentence.length; b++) {
                               colorHistoryList.add(new GridItem(Color.WHITE, topicSentence[b], "topic"));

                           }


                       }

                       lastTopic = topic;

                        break;
                    }

                }

                dateToSend = "null";
                colorHistoryList.add(new GridItem(color, dateToSend, widget));

                Log.d("colourloggeddate", fullRecord[x]);

            }


            else {
                dateToSend = loggedDate;
                colorHistoryList.add(new GridItem(Color.WHITE, dateToSend, "Multiple"));
                colorHistoryList.add(new GridItem(color,"null", widget));
            }
            lastDate = loggedDate;
        }



       GridView simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView

        CustomAdapter customGridAdapter = new CustomAdapter(this, colorHistoryList);
        simpleGrid.setAdapter(customGridAdapter);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ColorOverView.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }


    public boolean isTopicBefore (String topicDate, String colourLoggedDate) {

        boolean before = false;


        if (topicDate.compareTo(colourLoggedDate) <= 0)
        {
            before = true;

        }
        else {
            before = false;
        }

        return before;
    }



}
