package com.widgetone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import java.util.ArrayList;
import java.util.Calendar;

public class ConfigActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_TEXT = "key_text";
    public static final String KEY_COLOR_ONE = "key_color_one";
    public static final String KEY_COLOR_TWO = "key_color_two";
    public static final String KEY_COLOR_THREE = "key_color_three";
    public static final String KEY_COLOR_FOUR = "key_color_four";
    public static final String KEY_COLOR_FIVE = "key_color_five";


    public static final String ACTION_BUTTON_ONE = "ACTION_BUTTON_ONE";
    public static final String ACTION_BUTTON_TWO = "ACTION_BUTTON_TWO";
    public static final String ACTION_BUTTON_THREE = "ACTION_BUTTON_THREE";
    public static final String ACTION_BUTTON_FOUR = "ACTION_BUTTON_FOUR";
    public static final String ACTION_BUTTON_FIVE = "ACTION_BUTTON_FIVE";

    final ArrayList<Item> backgroundColorList = new ArrayList<>();

    private RecyclerView recyclerView;

    //Context context;


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editText;
    private int lastClicked;
    private int clicked = 0;
    FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity_layout);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();


        if (extras != null) {

            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        fileManager = new FileManager(this);




        /*
        "to xomplete the activity we need this resultvalue stuff " I think it;s the instructions to do something. Canceled is usually the default mode
        but some phones don't have it. So by making ti explicit we set it outselves.
        */
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


        editText = findViewById(R.id.edit_text);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.box_list);
        recyclerView.setLayoutManager(linearLayoutManager);


// Change this so the colours == to the ones saved ininter

        String currentPresents = fileManager.readInternalStringFile("current_preset_colours");


        if (currentPresents.length()>0) {
            String [] presetsArray = currentPresents.split(",");
            backgroundColorList.add(new Item(Integer.parseInt(presetsArray[0]), View.INVISIBLE));
            backgroundColorList.add(new Item(Integer.parseInt(presetsArray[1]), View.INVISIBLE));
            backgroundColorList.add(new Item(Integer.parseInt(presetsArray[2]), View.INVISIBLE));
            backgroundColorList.add(new Item(Integer.parseInt(presetsArray[3]), View.INVISIBLE));
            backgroundColorList.add(new Item(Integer.parseInt(presetsArray[4]), View.INVISIBLE));
        }



        else {

            backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
            backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
            backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
            backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
            backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
        }




        final CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, backgroundColorList);

        recyclerView.setAdapter(customRecyclerViewAdapter);


        final ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);

        LightnessSlider slider = findViewById(R.id.slider);

        colorPickerView.setLightnessSlider(slider);


        colorPickerView.setInitialColor(Color.BLACK, false);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                lastClicked = clicked;
                clicked = position;

               colorPickerView.setInitialColor(backgroundColorList.get(clicked).getBackgroundColour(), false);


                backgroundColorList.get(lastClicked).setVisibility(View.INVISIBLE);
                backgroundColorList.get(clicked).setVisibility(View.VISIBLE);
                customRecyclerViewAdapter.notifyDataSetChanged();




                colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {

                        backgroundColorList.get(clicked).setBackgroundColour(selectedColor);
                        customRecyclerViewAdapter.notifyDataSetChanged();

                    }

                });

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));






    }


    public void confirmConfiguration(View v) {

        String timeString;
        String preSetColoursString = "";

        timeString = Calendar.getInstance().getTime().toString();


for (int x = 0; x< 5; x++) {
    preSetColoursString = preSetColoursString + Integer.toString(backgroundColorList.get(x).getBackgroundColour()) + ",";
}

        String currentHistoryFileString = fileManager.readInternalStringFile("preset_colours_history");

        fileManager.createInternalStringFile(timeString + "," + preSetColoursString + currentHistoryFileString, "preset_colours_history");
        fileManager.createInternalStringFile(preSetColoursString, "current_preset_colours");


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);



        String text = editText.getText().toString();


        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.new_app_widget);




        //For a widget there are different methodNames but you use them by setting the data type first e.g. .setCharSequence for text, setInt tfor color.
        views.setCharSequence(R.id.topic_text, "setText", text);
        views.setInt(R.id.colour_button_one, "setBackgroundColor", backgroundColorList.get(0).getBackgroundColour());
        views.setInt(R.id.colour_button_two, "setBackgroundColor", backgroundColorList.get(1).getBackgroundColour());
        views.setInt(R.id.colour_button_three, "setBackgroundColor", backgroundColorList.get(2).getBackgroundColour());
        views.setInt(R.id.colour_button_four, "setBackgroundColor", backgroundColorList.get(3).getBackgroundColour());
        views.setInt(R.id.colour_button_five, "setBackgroundColor", backgroundColorList.get(4).getBackgroundColour());


        Intent buttonOneIntent = new Intent(this, NewAppWidget.class).setAction(ACTION_BUTTON_ONE);
        PendingIntent pendingIntentOne = PendingIntent.getBroadcast(this,
                0, buttonOneIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent buttonTwoIntent = new Intent(this, NewAppWidget.class).setAction(ACTION_BUTTON_TWO);
        PendingIntent pendingIntentTwo = PendingIntent.getBroadcast(this,
                0, buttonTwoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent buttonThreeIntent = new Intent(this, NewAppWidget.class).setAction(ACTION_BUTTON_THREE);
        PendingIntent pendingIntentThree = PendingIntent.getBroadcast(this,
                0, buttonThreeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent buttonFourIntent = new Intent(this, NewAppWidget.class).setAction(ACTION_BUTTON_FOUR);
        PendingIntent pendingIntentFour = PendingIntent.getBroadcast(this,
                0, buttonFourIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent buttonFiveIntent = new Intent(this, NewAppWidget.class).setAction(ACTION_BUTTON_FIVE);
        PendingIntent pendingIntentFive = PendingIntent.getBroadcast(this,
                0, buttonFiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent configIntent = new Intent(this, ConfigActivity.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(this,0,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent penIntent = new Intent(this, DiaryActivity.class);
        PendingIntent penPendingIntent = PendingIntent.getActivity(this,0,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        views.setOnClickPendingIntent(R.id.colour_button_one, pendingIntentOne);
        views.setOnClickPendingIntent(R.id.colour_button_two, pendingIntentTwo);
        views.setOnClickPendingIntent(R.id.colour_button_three, pendingIntentThree);
        views.setOnClickPendingIntent(R.id.colour_button_four, pendingIntentFour);
        views.setOnClickPendingIntent(R.id.colour_button_five, pendingIntentFive);
        views.setOnClickPendingIntent(R.id.settings, configPendingIntent);
        views.setOnClickPendingIntent(R.id.pen,penPendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);






        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TEXT + appWidgetId, text);
        editor.putString(KEY_COLOR_ONE + appWidgetId, Integer.toString(backgroundColorList.get(0).getBackgroundColour()));
        editor.putString(KEY_COLOR_TWO + appWidgetId, Integer.toString(backgroundColorList.get(1).getBackgroundColour()));
        editor.putString(KEY_COLOR_THREE + appWidgetId, Integer.toString(backgroundColorList.get(2).getBackgroundColour()));
        editor.putString(KEY_COLOR_FOUR + appWidgetId, Integer.toString(backgroundColorList.get(3).getBackgroundColour()));
        editor.putString(KEY_COLOR_FIVE + appWidgetId, Integer.toString(backgroundColorList.get(4).getBackgroundColour()));
        // this could be putInt, which would remove thge need to change it to a string etc. need to verify
        editor.apply();




        //"to xomplete the activity we need this" I think it;s the instructions to do something

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();


    }
}
