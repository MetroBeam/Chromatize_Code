package com.widgetone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class FlowerMainActivity extends AppCompatActivity {

    String selectedColourString;
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "";
    private String fileData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flower_activity_main);

        FrameLayout cross = findViewById(R.id.cross_frame);

        final Context context = this;


        selectedColourString = Integer.toString(999999);

        final FileManager fileManager = new FileManager(this);


        ColorPickerView colorPickerView = findViewById(R.id.color_flower_view);
        LightnessSlider lightSlider = findViewById(R.id.lightness);

        colorPickerView.setLightnessSlider(lightSlider);

        colorPickerView.setInitialColor(-2, false);


        final FrameLayout confirmFrame = findViewById(R.id.confirm_frame);


        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {

            @Override
            public void onColorChanged(int selectedColor) {
                Log.d("color", Integer.toString(selectedColor));

                // if (selectedColor != -1) {
                selectedColourString = Integer.toString(selectedColor);
                confirmFrame.setBackgroundColor(Integer.parseInt(selectedColourString));
                //}
            }


        });


        confirmFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String timeString = Calendar.getInstance().getTime().toString();
                //String currentFileString = askPermissionAndReadExtermalFile();
                String currentFileString = fileManager.readInternalStringFile("recorded_colours_history");

                fileManager.createInternalStringFile(timeString + "~" + "Flower~" + selectedColourString + "~" + currentFileString, "recorded_colours_history");


                Intent nextActivity;

                nextActivity = new Intent(context, SetWallpaperActivity.class);
                nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextActivity);
                finish();


            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


    }


}
