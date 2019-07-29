package com.widgetone;




import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;





    public class FileManager {

        private static final int REQUEST_ID_READ_PERMISSION = 100;
        private static final int REQUEST_ID_WRITE_PERMISSION = 200;
        private final String fileName = "note.txt";
        Context context;

        public FileManager(Context context) {
            this.context = context;
        }

        public void createInternalStringFile(String fileData, String fileName) {


            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
                outputStreamWriter.write(fileData);
                outputStreamWriter.close();
            } catch (IOException e) {

            }
        }

        public String readInternalStringFile(String fileName) {

            String ret = "";

            try {
                InputStream inputStream = context.openFileInput(fileName);
                String name = context.getFilesDir().getAbsolutePath();

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }

            return ret;
        }






    }