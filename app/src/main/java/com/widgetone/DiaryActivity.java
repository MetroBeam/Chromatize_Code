package com.widgetone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;



import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DiaryActivity extends AppCompatActivity {


    FileManager fileManager;
    EditText editText;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    Button startbtn;
    Button stopbtn;
    Button playbtn;
    Button stopplay;
    LinearLayout audioPanel;


    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    boolean text = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        startbtn = (Button)findViewById(R.id.btnRecord);
        stopbtn = (Button)findViewById(R.id.btnStop);
        playbtn = (Button)findViewById(R.id.btnPlay);
        stopplay = (Button)findViewById(R.id.btnStopPlay);

        startbtn.setBackgroundResource(R.drawable.red_record);
        stopbtn.setBackgroundResource(R.drawable.grey_stop);
        playbtn.setBackgroundResource(R.drawable.grey_play);
        stopplay.setBackgroundResource(R.drawable.grey_stop);

        startbtn.setEnabled(true);
        stopbtn.setEnabled(false);
        playbtn.setEnabled(false);
        stopplay.setEnabled(false);
        mFileName = "";

        audioPanel = findViewById(R.id.audiio_panel);



        fileManager = new FileManager(this);



        editText = findViewById(R.id.reflection_text_view);


        final ImageView pen = findViewById(R.id.pen_view);
        final ImageView voice = findViewById(R.id.voice_view);
        final Button confirmButton = findViewById(R.id.reflection_confirm_button);


        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText.setVisibility(View.VISIBLE);

                audioPanel.setVisibility(View.INVISIBLE);


                text = true;
                voice.setBackgroundColor(getResources().getColor(R.color.white));
                pen.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText.setVisibility(View.INVISIBLE);
                audioPanel.setVisibility(View.VISIBLE);



                voice.setBackgroundColor(getResources().getColor(R.color.grey));
                pen.setBackgroundColor(getResources().getColor(R.color.white));



                startbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(CheckPermissions()) {



                            String time = Calendar.getInstance().getTime().toString();
                            String timeFormatted = time.replace(":","");

                            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                            mFileName += "/"+timeFormatted+".3gp";


                            startbtn.setEnabled(false);
                            stopbtn.setEnabled(true);
                            playbtn.setEnabled(false);
                            stopplay.setEnabled(false);

                            startbtn.setBackgroundResource(R.drawable.grey_play);
                            stopbtn.setBackgroundResource(R.drawable.purple_stop);
                            playbtn.setBackgroundResource(R.drawable.grey_play);
                            stopplay.setBackgroundResource(R.drawable.grey_stop);


                            mRecorder = new MediaRecorder();
                            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mRecorder.setOutputFile(mFileName);
                            try {
                                mRecorder.prepare();
                            } catch (IOException e) {
                            }
                            mRecorder.start();
                        }
                        else
                        {
                            RequestPermissions();
                        }
                    }
                });


                stopbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startbtn.setEnabled(true);
                        stopbtn.setEnabled(false);
                        playbtn.setEnabled(true);
                        stopplay.setEnabled(true);

                        startbtn.setBackgroundResource(R.drawable.red_record);
                        stopbtn.setBackgroundResource(R.drawable.grey_stop);
                        playbtn.setBackgroundResource(R.drawable.green_play);
                        stopplay.setBackgroundResource(R.drawable.purple_stop);



                        mRecorder.stop();
                        mRecorder.release();
                        mRecorder = null;
                        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                        File file[] = f.listFiles();
                        mFileName = file[file.length-1].toString();

                    }
                });

                playbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startbtn.setEnabled(true);
                        stopbtn.setEnabled(false);
                        playbtn.setEnabled(false);
                        stopplay.setEnabled(true);

                        startbtn.setBackgroundResource(R.drawable.red_record);
                        stopbtn.setBackgroundResource(R.drawable.grey_stop);
                        playbtn.setBackgroundResource(R.drawable.grey_play);
                        stopplay.setBackgroundResource(R.drawable.purple_stop);



                        mPlayer = new MediaPlayer();
                        try {
                            mPlayer.setDataSource(mFileName);
                            mPlayer.prepare();
                            mPlayer.start();
                        } catch (IOException e) {
                        }
                    }
                });

                stopplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPlayer.release();
                        mPlayer = null;

                        startbtn.setEnabled(true);
                        stopbtn.setEnabled(false);
                        playbtn.setEnabled(true);
                        stopplay.setEnabled(false);


                        startbtn.setBackgroundResource(R.drawable.red_record);
                        stopbtn.setBackgroundResource(R.drawable.grey_stop);
                        playbtn.setBackgroundResource(R.drawable.green_play);
                        stopplay.setBackgroundResource(R.drawable.grey_stop);

                    }
                });



            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textReflectionText = editText.getText().toString();
                String timeString = Calendar.getInstance().getTime().toString();

                    fileManager.createInternalStringFile(timeString + "," + textReflectionText + "," + fileManager.readInternalStringFile("textReflectionHistory"), "textReflectionHistory");
                    Intent intent = new Intent(DiaryActivity.this, MainActivity.class);
                    intent.putExtra("Exit me", true);
                    startActivity(intent);
                    finish();
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                    } else {
                    }
                }
                break;
        }
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE );
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
       ActivityCompat.requestPermissions(DiaryActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }
}






