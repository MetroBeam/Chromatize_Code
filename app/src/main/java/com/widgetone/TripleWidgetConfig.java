package com.widgetone;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import java.util.ArrayList;
import java.util.Calendar;

public class TripleWidgetConfig extends AppCompatActivity {

    //public static final String ID_SHARED_PREFS = "prefs";
    public static final String VALUES_SHARED_PREFS = "values_prefs";

    public static final String TRIPLE_KEY_TEXT = "key_text";
    public static final String KEY_COLOR_ONE = "key_color_one";
    public static final String KEY_COLOR_TWO = "key_color_two";
    public static final String KEY_COLOR_THREE = "key_color_three";
    public static final String KEY_COLOR_FOUR = "key_color_four";
    public static final String KEY_COLOR_FIVE = "key_color_five";

    public static final String Key_WIDGETID = "ket_widget_id";

    public static final String KEY_VISIBILITY_ONE = "key_visibility_one";
    public static final String KEY_VISIBILITY_TWO = "key_visibility_two";
    public static final String KEY_VISIBILITY_THREE = "key_visibility_three";
    public static final String KEY_VISIBILITY_FOUR = "key_visibility_four";
    public static final String KEY_VISIBILITY_FIVE = "key_visibility_five";

    public static final String ACTION_BUTTON_ONE = "ACTION_BUTTON_ONE";
    public static final String ACTION_BUTTON_TWO = "ACTION_BUTTON_TWO";
    public static final String ACTION_BUTTON_THREE = "ACTION_BUTTON_THREE";
    public static final String ACTION_BUTTON_FOUR = "ACTION_BUTTON_FOUR";
    public static final String ACTION_BUTTON_FIVE = "ACTION_BUTTON_FIVE";

    final ArrayList<Item> backgroundColorList = new ArrayList<>();

    private RecyclerView recyclerView;

    private boolean editTextSelected = false;
    private int topicChangeTarget = 3;

    private boolean colourChanged = false;
    private int colourChangedTarget = 3;

    //Context context;


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editText;
    private int lastClicked;
    private int clicked = 0;
    FileManager fileManager;
    private ImageView tapView;
    private int nTaps;
    String topicsHistoryString = "";
    private String colourOverviewFile;

    String configMode = "none";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triple_config_activity_layout);

        final Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();




        if (extras != null) {
            Log.d("MULTIPLE ID", Integer.toString(appWidgetId));
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            Log.d("MULTIPLE ID", Integer.toString(appWidgetId));
        }



        fileManager = new FileManager(this);


        final ImageView addView = findViewById(R.id.add_box);
        final ImageView confirmButton = findViewById(R.id.confirm_box);

        final ImageView multipleConfigConfirm =findViewById(R.id.capture_config_confirm_button);



        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }




        editText = findViewById(R.id.edit_text);
        final ImageView grid = findViewById(R.id.grid_link);


        String  topicsHistoryString = "";
        topicsHistoryString = fileManager.readInternalStringFile("topics_history");

        if (topicsHistoryString.length()>0) {
            String[] topicArray = topicsHistoryString.split("~");
            String lastTitle = topicArray[2];
            if (!lastTitle.equals("no topic")) {
                editText.setText(lastTitle);
            }
            else {
                editText.setText("");
            }
        }

        else {
            editText.setHint("set your topic...");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editText.requestFocus();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.box_list);
        recyclerView.setLayoutManager(linearLayoutManager);


// Change this so the colours == to the ones saved ininter

        String currentPresets = fileManager.readInternalStringFile("current_preset_colours");

        final CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, backgroundColorList);

        recyclerView.setAdapter(customRecyclerViewAdapter);



        if (currentPresets.length()>0) {
            String[] presetsArray = currentPresets.split("~");
            for (int x = 0; x < presetsArray.length; x++) {
                Log.d("x", Integer.toString(x));
                backgroundColorList.add(new Item(Integer.parseInt(presetsArray[x]), View.INVISIBLE));
            }


            if (backgroundColorList.size()<5) {
                addView.setVisibility(View.VISIBLE);
            }
            if (backgroundColorList.size()==5) {
                addView.setVisibility(View.INVISIBLE);
            }
        }




        else {

            backgroundColorList.add(new Item(Color.WHITE, View.INVISIBLE));
            backgroundColorList.add(new Item(Color.WHITE, View.INVISIBLE));


        }

        final ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);

        final LightnessSlider slider = findViewById(R.id.slider);


        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configMode = "colours_config";

                if(backgroundColorList.size()<5) {

                    colourChanged = true;
                    addView.setVisibility(View.VISIBLE);
                    addView.setEnabled(true);
                    if (lastClicked != -1) {
                    backgroundColorList.get(lastClicked).setVisibility(View.INVISIBLE);}
                    backgroundColorList.add(new Item(Color.WHITE, View.VISIBLE));
                    lastClicked = backgroundColorList.size()-1;
                    clicked = backgroundColorList.size()-1;
                    customRecyclerViewAdapter.notifyDataSetChanged();
                }

                customRecyclerViewAdapter.notifyDataSetChanged();
                slider.setVisibility(View.VISIBLE);
                slider.setEnabled(true);
                colorPickerView.setVisibility(View.VISIBLE);
                colorPickerView.setEnabled(true);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setEnabled(true);
                confirmButton.setBackgroundResource(R.drawable.delete);



                if (backgroundColorList.size()==5) {
                    addView.setVisibility(View.INVISIBLE);
                    addView.setEnabled(false);
                }
                }

        });








        colorPickerView.setLightnessSlider(slider);


        colorPickerView.setInitialColor(Color.WHITE, false);




        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {


                slider.setVisibility(View.VISIBLE);
                slider.setEnabled(true);
                colorPickerView.setVisibility(View.VISIBLE);
                colorPickerView.setEnabled(true);

                clicked = position;

                configMode = "colours_config";

                if (backgroundColorList.size()>2) {
                    confirmButton.setBackgroundResource(R.drawable.delete);
                    confirmButton.setVisibility(View.VISIBLE);
                    confirmButton.setEnabled(true);
                }



               colorPickerView.setInitialColor(backgroundColorList.get(clicked).getBackgroundColour(), false);



               if (lastClicked>-1) {
                   backgroundColorList.get(lastClicked).setVisibility(View.INVISIBLE);
               }
                   backgroundColorList.get(clicked).setVisibility(View.VISIBLE);
                   customRecyclerViewAdapter.notifyDataSetChanged();


                lastClicked = clicked;
                Log.d("lastclicked", Integer.toString(lastClicked));



            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {

                backgroundColorList.get(clicked).setBackgroundColour(selectedColor);
                customRecyclerViewAdapter.notifyDataSetChanged();
                colourChanged = true;


            }



        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (configMode.equals("title_config")) {
                    hideKeyboard(TripleWidgetConfig.this);

                    confirmButton.setEnabled(false);
                    confirmButton.setVisibility(View.INVISIBLE);

                    addView.setVisibility(View.VISIBLE);
                    addView.setEnabled(true);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setEnabled(false);

                    multipleConfigConfirm.setVisibility(View.VISIBLE);
                    multipleConfigConfirm.setEnabled(true);



                }


                if (configMode.equals("colours_config")) {
                    confirmButton.setVisibility(View.VISIBLE);
                    confirmButton.setEnabled(true);
                    addView.setVisibility(View.VISIBLE);
                    addView.setEnabled(true);

                    if (backgroundColorList.size() > 2) {
                        backgroundColorList.get(clicked).setVisibility(View.INVISIBLE);
                        backgroundColorList.remove(clicked);
                        customRecyclerViewAdapter.notifyDataSetChanged();;
                        clicked--;
                        lastClicked--;
                        backgroundColorList.get(clicked).setVisibility(View.VISIBLE);
                        customRecyclerViewAdapter.notifyDataSetChanged();
                        Log.d("lastclicked", Integer.toString(lastClicked));

                        slider.setVisibility(View.VISIBLE);
                        slider.setEnabled(true);

                        colorPickerView.setVisibility(View.VISIBLE);
                        colorPickerView.setEnabled(true);


                    }

                    if (backgroundColorList.size()==2) {
                        confirmButton.setVisibility(View.INVISIBLE);
                        confirmButton.setEnabled(false);
                    }


                }

            }

        });



        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                configMode = "title_config";
                confirmButton.setBackgroundResource(R.drawable.checked);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setEnabled(true);

                editText.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                colorPickerView.setEnabled(false);
                colorPickerView.setVisibility(View.GONE);
                addView.setEnabled(false);
                addView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setEnabled(false);
                confirmButton.setVisibility(View.VISIBLE);
                confirmButton.setEnabled(true);
                multipleConfigConfirm.setVisibility(View.INVISIBLE);
                multipleConfigConfirm.setEnabled(false);
                slider.setVisibility(View.GONE);
                slider.setEnabled(false);
                backgroundColorList.get(lastClicked).setVisibility(View.INVISIBLE);
                customRecyclerViewAdapter.notifyDataSetChanged();



                if(editText.getText().toString().equals("set your topic...")) {
                    editText.setText("");
                }

                if(editText.getText().toString().equals("...")) {
                    editText.setText("");
                }
                return false;
            }


        });

        multipleConfigConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmConfiguration();
            }
        });


        grid.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

               String colourOverviewViews = "";
                colourOverviewViews = fileManager.readInternalStringFile("colour_overview");
               String Date = Calendar.getInstance().getTime().toString();
                fileManager.createInternalStringFile(colourOverviewViews + Date + "~", "colour_overview");
                Intent burgerIntent = new Intent(TripleWidgetConfig.this, ColorOverView.class);
                startActivity(burgerIntent);
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                editTextSelected = true;

                String enteredText = editText.getText().toString();

                if (enteredText.length()>0) {

                    if (enteredText.charAt(enteredText.length() - 1) == '~') {
                        enteredText = enteredText.replace(enteredText.substring(enteredText.length() - 1), "");
                        editText.setText(enteredText);
                        editText.setSelection(editText.getText().length());

                        Toast.makeText(TripleWidgetConfig.this, "Sorry, you cannot use the ~ symbol", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void confirmConfiguration() {
        String notificationsSchedule = fileManager.readInternalStringFile("notification_schedule");
        String[] notificationsArray = notificationsSchedule.split("~");


        String timeString;
        String preSetColoursString = "";

        timeString = Calendar.getInstance().getTime().toString();

       // timeString = fileManager.readInternalStringFile(notificationsArray[notificationsArray.length-1]);

for (int x = 0; x<backgroundColorList.size() ; x++) {
     preSetColoursString = preSetColoursString + Integer.toString(backgroundColorList.get(x).getBackgroundColour()) + "~";
}

        String currentHistoryFileString = fileManager.readInternalStringFile("preset_colours_history");




        fileManager.createInternalStringFile(timeString + "~" + preSetColoursString + currentHistoryFileString, "preset_colours_history");
        fileManager.createInternalStringFile(preSetColoursString, "current_preset_colours");




        String fileText ="";
        String displayedText = "";

        fileText= editText.getText().toString();

        if (fileText.length()<1) {
            Log.d("aha", "confirmConfiguration: ");
            displayedText= "   ";
            fileText = "no topic";

        }

        else {
            displayedText = fileText;
        }




        String  topicsHistoryString = "";
        topicsHistoryString = fileManager.readInternalStringFile("topics_history");

        fileManager.createInternalStringFile( Integer.toString(appWidgetId) + "~" + timeString + "~" + fileText + "~" + topicsHistoryString , "topics_history");


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);







        //SharedPreferences.Editor IDseditor = getSharedPreferences(ID_SHARED_PREFS, MODE_PRIVATE).edit();
        //IDseditor.putInt(Key_WIDGETID, appWidgetId);

      //  IDseditor.apply();

        appWidgetManager = AppWidgetManager
                .getInstance(TripleWidgetConfig.this);
        ComponentName thisAppWidget = new ComponentName(
                TripleWidgetConfig.this, TripleWidget.class);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(thisAppWidget);
        Intent startBroadcast = new Intent(TripleWidgetConfig.this,
                TripleWidget.class);
        //startBroadcast.putParcelableArrayListExtra("list", newsList);
        startBroadcast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                appWidgetIds);
        startBroadcast.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(startBroadcast);


        SharedPreferences.Editor valuesEditor = getSharedPreferences(VALUES_SHARED_PREFS + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();



        Intent flowerSelectIntent = new Intent(this, MainActivity.class);
        flowerSelectIntent.putExtra("flower", "flower");
        PendingIntent pendingIntentFlowerSelect = PendingIntent.getActivity(this,
                appWidgetId, flowerSelectIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent buttonOneIntent = new Intent(this, TripleWidget.class).setAction(ACTION_BUTTON_ONE);
        PendingIntent pendingIntentOne = PendingIntent.getBroadcast(this,
                appWidgetId, buttonOneIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent buttonTwoIntent = new Intent(this, TripleWidget.class).setAction(ACTION_BUTTON_TWO);
        PendingIntent pendingIntentTwo = PendingIntent.getBroadcast(this,
                appWidgetId, buttonTwoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent buttonThreeIntent = new Intent(this, TripleWidget.class).setAction(ACTION_BUTTON_THREE);
        PendingIntent pendingIntentThree = PendingIntent.getBroadcast(this,
                appWidgetId, buttonThreeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent buttonFourIntent = new Intent(this, TripleWidget.class).setAction(ACTION_BUTTON_FOUR);
        PendingIntent pendingIntentFour = PendingIntent.getBroadcast(this,
                appWidgetId, buttonFourIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent buttonFiveIntent = new Intent(this, TripleWidget.class).setAction(ACTION_BUTTON_FIVE);
        PendingIntent pendingIntentFive = PendingIntent.getBroadcast(this,
                appWidgetId, buttonFiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent configIntent = new Intent(this, TripleWidgetConfig.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(this,appWidgetId,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent penIntent = new Intent(this, DiaryActivity.class);
        PendingIntent penPendingIntent = PendingIntent.getActivity(this,appWidgetId,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.triple_app_widget);

        views.setCharSequence(R.id.topic_text, "setText", displayedText);
        views.setOnClickPendingIntent(R.id.colour_button_one, pendingIntentOne);
        views.setOnClickPendingIntent(R.id.colour_button_two, pendingIntentTwo);
        views.setOnClickPendingIntent(R.id.colour_button_three, pendingIntentThree);
        views.setOnClickPendingIntent(R.id.colour_button_four, pendingIntentFour);
        views.setOnClickPendingIntent(R.id.colour_button_five, pendingIntentFive);
        views.setOnClickPendingIntent(R.id.settings, configPendingIntent);
        views.setOnClickPendingIntent(R.id.triple_color_flower, pendingIntentFlowerSelect);
        views.setOnClickPendingIntent(R.id.pen,penPendingIntent);


          Intent captureSelectIntent = new Intent(this, MainActivity.class);
        captureSelectIntent.putExtra("capture", "capture");
        PendingIntent pendingIntentCaptureSelect = PendingIntent.getActivity(this,
              appWidgetId, captureSelectIntent, PendingIntent.FLAG_UPDATE_CURRENT);




                views.setOnClickPendingIntent(R.id.triple_capture_icon, pendingIntentCaptureSelect);



        if (backgroundColorList.size()==1) {
    views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
    views.setViewVisibility(R.id.frame_one, View.VISIBLE);
    views.setViewVisibility(R.id.frame_two, View.GONE);
    views.setViewVisibility(R.id.frame_three, View.GONE);
    views.setViewVisibility(R.id.frame_four, View.GONE);
    views.setViewVisibility(R.id.frame_five, View.GONE);
    valuesEditor.putString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0");
    valuesEditor.putString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "8");
    valuesEditor.putString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "8");
    valuesEditor.putString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "8");
    valuesEditor.putString(KEY_VISIBILITY_FIVE+ String.valueOf(appWidgetId), "8");

    valuesEditor.putString(KEY_COLOR_ONE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
    valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
    valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
    valuesEditor.putString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
    valuesEditor.putString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
}

if (backgroundColorList.size()==2) {
            views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
            views.setInt(R.id.colour_button_two, "setBackgroundColor", backgroundColorList.get(1).getBackgroundColour());
            views.setViewVisibility(R.id.frame_one, View.VISIBLE);
            views.setViewVisibility(R.id.frame_two, View.VISIBLE);
            views.setViewVisibility(R.id.frame_three, View.GONE);
            views.setViewVisibility(R.id.frame_four, View.GONE);
            views.setViewVisibility(R.id.frame_five, View.GONE);

    valuesEditor.putString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0");
    valuesEditor.putString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "0");
    valuesEditor.putString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "8");
    valuesEditor.putString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "8");
    valuesEditor.putString(KEY_VISIBILITY_FIVE+ String.valueOf(appWidgetId), "8");

    valuesEditor.putString(KEY_COLOR_ONE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
    valuesEditor.putString(KEY_COLOR_TWO + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(1).getBackgroundColour()));
    valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
    valuesEditor.putString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
    valuesEditor.putString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));

        }

        if (backgroundColorList.size()==3) {
            views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
            views.setInt(R.id.colour_button_two, "setBackgroundColor", backgroundColorList.get(1).getBackgroundColour());
            views.setInt(R.id.colour_button_three, "setBackgroundColor", backgroundColorList.get(2).getBackgroundColour());
            views.setViewVisibility(R.id.frame_one, View.VISIBLE);
            views.setViewVisibility(R.id.frame_two, View.VISIBLE);
            views.setViewVisibility(R.id.frame_three, View.VISIBLE);
            views.setViewVisibility(R.id.frame_four, View.GONE);
            views.setViewVisibility(R.id.frame_five, View.GONE);

            valuesEditor.putString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "8");
            valuesEditor.putString(KEY_VISIBILITY_FIVE+ String.valueOf(appWidgetId), "8");

            valuesEditor.putString(KEY_COLOR_ONE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_TWO + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(1).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(2).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));
            valuesEditor.putString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));

        }

        if (backgroundColorList.size()==4) {
            views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
            views.setInt(R.id.colour_button_two, "setBackgroundColor", backgroundColorList.get(1).getBackgroundColour());
            views.setInt(R.id.colour_button_three, "setBackgroundColor", backgroundColorList.get(2).getBackgroundColour());
            views.setInt(R.id.colour_button_four, "setBackgroundColor", backgroundColorList.get(3).getBackgroundColour());
            views.setViewVisibility(R.id.frame_one, View.VISIBLE);
            views.setViewVisibility(R.id.frame_two, View.VISIBLE);
            views.setViewVisibility(R.id.frame_three, View.VISIBLE);
            views.setViewVisibility(R.id.frame_four, View.VISIBLE);
            views.setViewVisibility(R.id.frame_five, View.GONE);

            valuesEditor.putString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_FIVE+ String.valueOf(appWidgetId), "8");

            valuesEditor.putString(KEY_COLOR_ONE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_TWO + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(1).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(2).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(3).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), Integer.toString(Color.WHITE));

        }

        if (backgroundColorList.size()==5) {
            views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
            views.setInt(R.id.colour_button_two, "setBackgroundColor", backgroundColorList.get(1).getBackgroundColour());
            views.setInt(R.id.colour_button_three, "setBackgroundColor", backgroundColorList.get(2).getBackgroundColour());
            views.setInt(R.id.colour_button_four, "setBackgroundColor", backgroundColorList.get(3).getBackgroundColour());
            views.setInt(R.id.colour_button_five, "setBackgroundColor", backgroundColorList.get(4).getBackgroundColour());

            views.setViewVisibility(R.id.frame_one, View.VISIBLE);
            views.setViewVisibility(R.id.frame_two, View.VISIBLE);
            views.setViewVisibility(R.id.frame_three, View.VISIBLE);
            views.setViewVisibility(R.id.frame_four, View.VISIBLE);
            views.setViewVisibility(R.id.frame_five, View.VISIBLE);

            valuesEditor.putString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "0");
            valuesEditor.putString(KEY_VISIBILITY_FIVE+ String.valueOf(appWidgetId), "0");

            valuesEditor.putString(KEY_COLOR_ONE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_TWO + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(1).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_THREE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(2).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(3).getBackgroundColour()));
            valuesEditor.putString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), Integer.toString(backgroundColorList.get(4).getBackgroundColour()));

        }
        //For a widget there are different methodNames but you use them by setting the data type first e.g. .setCharSequence for text, setInt tfor color.


        valuesEditor.putString(TRIPLE_KEY_TEXT + String.valueOf(appWidgetId), displayedText);



        // this could be putInt, which would remove thge need to change it to a string etc. need to verify
        valuesEditor.apply();

        appWidgetManager.updateAppWidget(appWidgetId, views);


        //"to xomplete the activity we need this" I think it;s the instructions to do something

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);


        if (editTextSelected) {

            Log.d("indapocket", "confirmConfiguration: ");
           // if (Integer.parseInt(notificationsArray[6]) > 22) {
                if (Integer.parseInt(notificationsArray[2]) > topicChangeTarget - 1) {
                    String scheduleReplacement = "";
                    notificationsArray[2] = "0";

                    // sendTopicSetChangedNotification();
                    for (int x = 0; x < notificationsArray.length; x++) {
                        scheduleReplacement = scheduleReplacement + notificationsArray[x] + "~";
                    }
                    fileManager.createInternalStringFile(scheduleReplacement, "notification_schedule");
                    Intent diaryIntent = new Intent(TripleWidgetConfig.this, DiaryActivity.class);
                    diaryIntent.putExtra("topic_changed", "topic_note");
                    startActivity(diaryIntent);
                }
            //}
        }

        if (colourChanged) {

           // if (Integer.parseInt(notificationsArray[6]) > 22) {

                if (Integer.parseInt(notificationsArray[3]) > colourChangedTarget - 1) {
                    String scheduleReplacement = "";
                    notificationsArray[3] = "0";
                    sendColourSetChangedNotification();
                    for (int x = 0; x < notificationsArray.length; x++) {
                        scheduleReplacement = scheduleReplacement + notificationsArray[x] + "~";
                    }
                    fileManager.createInternalStringFile(scheduleReplacement, "notification_schedule");
            Intent diaryIntent = new Intent(TripleWidgetConfig.this, DiaryActivity.class);
            diaryIntent.putExtra("colours_changed", "colour_note");
            startActivity(diaryIntent);
                }
            //}
        }

        finish();


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendColourSetChangedNotification () {
        Intent notificationIntent = new Intent(this, DiaryActivity.class);
        notificationIntent.putExtra("colours_changed", "colours_changed_note");

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
    }

    public void sendTopicSetChangedNotification () {
        Intent notificationIntent = new Intent(this, DiaryActivity.class);
        notificationIntent.putExtra("topic_changed", "topic_changed_note");

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
    }


}
