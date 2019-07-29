package com.widgetone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private FileManager fileManager;



    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "";
    private String fileData = "";


    private String selectedColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        ImageView tick = findViewById(R.id.tick);
        ImageView cross = findViewById(R.id.cross);

        fileManager = new FileManager(this);



        TextView selectedColourView = findViewById(R.id.selected_colour);

        Intent intent = getIntent();

        selectedColour = "-1";


        if( getIntent().hasExtra("Exit me")){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }


        if (getIntent().hasExtra("button")) {


            final String buttonIndex = intent.getStringExtra("button");


            String colourSet[] = fileManager.readInternalStringFile("current_preset_colours").split(",");


            switch (buttonIndex) {
                case "0":
                    selectedColour = colourSet[0];
                    break;
                case "1":
                    selectedColour = colourSet[1];
                    break;
                case "2":
                    selectedColour = colourSet[2];
                    break;
                case "3":
                    selectedColour = colourSet[3];
                    break;
                case "4":
                    selectedColour = colourSet[4];
                    break;


            }

            selectedColourView.setBackgroundColor(Integer.parseInt(selectedColour));
        }





            tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String timeString;

                    fileName = "recorded_colours_history";

                    timeString = Calendar.getInstance().getTime().toString();
                    String currentFileString = askPermissionAndReadExtermalFile();


                    fileData = timeString + "," + selectedColour + "," + currentFileString;


                    askPermissionAndWriteExternalFile();


                    Intent nextActivity = new Intent(context, SetWallpaperActivity.class);
                    startActivity(nextActivity);


                }
            });


            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    return;

                }
            });


        } /*else {
            tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    return;


                }
            });
            */

    private void askPermissionAndWriteExternalFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.writeFile();
        }
    }

    private String askPermissionAndReadExtermalFile() {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //
        String fileData = "";
        if (canRead) {
            fileData = this.readFile();
        }
        return fileData;
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_READ_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        readFile();
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        writeFile();
                    }
                }
            }
        } else {
        }
    }


    private void writeFile() {

        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;

        String data = fileData;

        try {
            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile() {

        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;

        String s = "";
        String fileContent = "";
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((s = myReader.readLine()) != null) {
                fileContent += s + "\n";
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    private void listExternalStorages() {
        StringBuilder sb = new StringBuilder();

        sb.append("Data Directory: ").append("\n - ")
                .append(Environment.getDataDirectory().toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("External Storage State: ").append("\n - ")
                .append(Environment.getExternalStorageState().toString()).append("\n");

        sb.append("External Storage Directory: ").append("\n - ")
                .append(Environment.getExternalStorageDirectory().toString()).append("\n");

        sb.append("Is External Storage Emulated?: ").append("\n - ")
                .append(Environment.isExternalStorageEmulated()).append("\n");

        sb.append("Is External Storage Removable?: ").append("\n - ")
                .append(Environment.isExternalStorageRemovable()).append("\n");

        sb.append("External Storage Public Directory (Music): ").append("\n - ")
                .append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("Root Directory: ").append("\n - ")
                .append(Environment.getRootDirectory().toString()).append("\n");

    }


        }










