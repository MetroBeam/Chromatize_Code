package com.widgetone;

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

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_TEXT = "key_text";
    public static final String KEY_COLOR_ONE = "key_color_one";
    public static final String KEY_COLOR_TWO = "key_color_two";
    public static final String KEY_COLOR_THREE = "key_color_three";
    public static final String KEY_COLOR_FOUR = "key_color_four";
    public static final String KEY_COLOR_FIVE = "key_color_five";

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


        fileManager = new FileManager(this);


        editText = findViewById(R.id.edit_text);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.box_list);
        recyclerView.setLayoutManager(linearLayoutManager);


        backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
        backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
        backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
        backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));
        backgroundColorList.add(new Item(Color.BLACK, View.INVISIBLE));



        final CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, backgroundColorList);

        recyclerView.setAdapter(customRecyclerViewAdapter);




        final ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);


        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                lastClicked = clicked;
                clicked = position;




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
        String titleString;

        timeString = Calendar.getInstance().getTime().toString();


for (int x = 0; x< 5; x++) {
    preSetColoursString = preSetColoursString + Integer.toString(backgroundColorList.get(x).getBackgroundColour()) + ",";
}



        String currentFileString = fileManager.readInternalStringFile("preset_colours_history");
        String currentTitleString = fileManager.readInternalStringFile("title_history");

        fileManager.createInternalStringFile(timeString + "," + preSetColoursString + currentFileString, "preset_colours_history");
        fileManager.createInternalStringFile(preSetColoursString, "current_preset_colours");
        String text = editText.getText().toString();
        fileManager.createInternalStringFile(text + "," + currentTitleString, "title_history");
        fileManager.createInternalStringFile(text, "current_title");





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

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);

    }
}
