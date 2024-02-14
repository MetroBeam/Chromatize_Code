package com.widgetone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.OutputStream;
import java.util.Calendar;


public class CaptureMainActivity extends AppCompatActivity implements OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;
    private Scalar mBlobColorHsv;
    private Scalar mBlobColorRgba;
    FrameLayout register;
    Context context;
    private ImageView check;

    FileManager fileManager;
    private int numFiles;

    double x = -1;
    double y = -1;

    static {
        OpenCVLoader.initDebug();
    }



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(CaptureMainActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



        setContentView(R.layout.capture_activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }



        fileManager = new FileManager(this);


        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_camera_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        FrameLayout cross = findViewById(R.id.cross_frame);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaptureMainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (mOpenCvCameraView != null) {
                    mOpenCvCameraView.disableView();
                }
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onPause() {

        OutputStream outputStream;
        String filename = "colourFile";

        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();



    }

    @Override
    public void onResume() {
        super.onResume();



            if (!OpenCVLoader.initDebug()) {
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
            } else {
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }

        check = findViewById(R.id.confirm_capture);


        register = (FrameLayout) findViewById(R.id.register);
            register.setRotation(270);

            View.OnClickListener registered = new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String rgb = (int) mBlobColorRgba.val[0] + "," + (int) mBlobColorRgba.val[1] + "," + (int) mBlobColorRgba.val[2] + ",";




                    String selectedColourString = (Integer.toString (getIntFromColor((int)mBlobColorRgba.val[0], (int)mBlobColorRgba.val[1], (int)mBlobColorRgba.val[2])));
                    String timeString = Calendar.getInstance().getTime().toString();
                    //String currentFileString = askPermissionAndReadExtermalFile();
                    String currentFileString = fileManager.readInternalStringFile("recorded_colours_history");

                    fileManager.createInternalStringFile(timeString + "~" + "Capture~" + selectedColourString + "~" + currentFileString, "recorded_colours_history");


                  if (!selectedColourString.equals("-65536")) {
                      Log.d("logged color", selectedColourString);
                      Intent nextActivity = new Intent(CaptureMainActivity.this, SetWallpaperActivity.class);
                      nextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      if (mOpenCvCameraView != null) {
                          mOpenCvCameraView.disableView();
                      }
                      startActivity(nextActivity);
                      finish();


                    }


                }
            };
            register.setOnClickListener(registered);
        }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int cols = mRgba.cols();
        int rows = mRgba.rows();


        double yLow = (double)mOpenCvCameraView.getHeight() * 0.2401961;
        double yHigh = (double)mOpenCvCameraView.getHeight() * 0.7696078;

        double xScale = (double)cols / (double)mOpenCvCameraView.getWidth();
        double yScale = (double)rows / (yHigh - yLow);


        y = event.getY();
        x = event.getX();

        y = y - yLow;

        x = x * xScale;
        y = y * yScale;

        if((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

       Rect touchedRect = new Rect();

        touchedRect.x = (int)x;
        touchedRect.y = (int)y;

        touchedRect.width = 8;
        touchedRect.height = 8;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);

        ImageView colorSquare = findViewById(R.id.color_display_square);
       colorSquare.setBackgroundColor(Color.rgb((int)mBlobColorRgba.val[0],(int) mBlobColorRgba.val[1],  (int) mBlobColorRgba.val[2])   );


        String selectedColourString = (Integer.toString (getIntFromColor((int)mBlobColorRgba.val[0], (int)mBlobColorRgba.val[1], (int)mBlobColorRgba.val[2])));

        if (!selectedColourString.equals("-65536")) {
            register.setVisibility(View.VISIBLE);
            register.setEnabled(true);
        }


        return false;
    }

    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
