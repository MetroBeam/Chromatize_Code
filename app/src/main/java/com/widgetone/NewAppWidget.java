package com.widgetone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;



import static com.widgetone.ConfigActivity.KEY_COLOR_FIVE;
import static com.widgetone.ConfigActivity.KEY_COLOR_FOUR;
import static com.widgetone.ConfigActivity.KEY_COLOR_ONE;
import static com.widgetone.ConfigActivity.KEY_COLOR_THREE;
import static com.widgetone.ConfigActivity.KEY_COLOR_TWO;
import static com.widgetone.ConfigActivity.KEY_TEXT;
import static com.widgetone.ConfigActivity.SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {


    public static final String ACTION_BUTTON_ONE = "ACTION_BUTTON_ONE";
    public static final String ACTION_BUTTON_TWO = "ACTION_BUTTON_TWO";
    public static final String ACTION_BUTTON_THREE = "ACTION_BUTTON_THREE";
    public static final String ACTION_BUTTON_FOUR = "ACTION_BUTTON_FOUR";
    public static final String ACTION_BUTTON_FIVE = "ACTION_BUTTON_FIVE";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {


            Intent buttonOneIntent = new Intent(context, NewAppWidget.class).setAction(ACTION_BUTTON_ONE);
            PendingIntent pendingIntentOne = PendingIntent.getBroadcast(context,
                    0, buttonOneIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent buttonTwoIntent = new Intent(context, NewAppWidget.class).setAction(ACTION_BUTTON_TWO);
            PendingIntent pendingIntentTwo = PendingIntent.getBroadcast(context,
                    0, buttonTwoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent buttonThreeIntent = new Intent(context, NewAppWidget.class).setAction(ACTION_BUTTON_THREE);
            PendingIntent pendingIntentThree = PendingIntent.getBroadcast(context,
                    0, buttonThreeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent buttonFourIntent = new Intent(context, NewAppWidget.class).setAction(ACTION_BUTTON_FOUR);
            PendingIntent pendingIntentFour = PendingIntent.getBroadcast(context,
                    0, buttonFourIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent buttonFiveIntent = new Intent(context, NewAppWidget.class).setAction(ACTION_BUTTON_FIVE);
            PendingIntent pendingIntentFive = PendingIntent.getBroadcast(context,
                    0, buttonFiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);



            Intent penIntent = new Intent(context, DiaryActivity.class);
            PendingIntent penPendingIntent = PendingIntent.getActivity(context,0,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            Intent configIntent = new Intent(context, ConfigActivity.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context,0,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String tText = preferences.getString(KEY_TEXT + appWidgetId, "D");
            String colorOneInt = preferences.getString(KEY_COLOR_ONE + appWidgetId, "0");
            String colorTwoInt = preferences.getString(KEY_COLOR_TWO + appWidgetId, "0");
            String colorThreeInt = preferences.getString(KEY_COLOR_THREE + appWidgetId, "0");
            String colorFourInt = preferences.getString(KEY_COLOR_FOUR + appWidgetId, "0");
            String colorFiveInt = preferences.getString(KEY_COLOR_FIVE + appWidgetId, "0");



            int colorOne = Integer.parseInt(colorOneInt);
            int colorTwo = Integer.parseInt(colorTwoInt);
            int colorThree = Integer.parseInt(colorThreeInt);
            int colorFour = Integer.parseInt(colorFourInt);
            int colorFive = Integer.parseInt(colorFiveInt);


            CharSequence widgetText = context.getString(R.string.appwidget_text);





            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);


            views.setCharSequence(R.id.topic_text, "setText", tText);
            views.setInt(R.id.colour_button_one, "setBackgroundColor", colorOne);
            views.setInt(R.id.colour_button_two, "setBackgroundColor", colorTwo);
            views.setInt(R.id.colour_button_three, "setBackgroundColor", colorThree);
            views.setInt(R.id.colour_button_four, "setBackgroundColor", colorFour);
            views.setInt(R.id.colour_button_five, "setBackgroundColor", colorFive);

            views.setOnClickPendingIntent(R.id.pen,penPendingIntent);
            views.setOnClickPendingIntent(R.id.colour_button_one, pendingIntentOne);
            views.setOnClickPendingIntent(R.id.colour_button_two, pendingIntentTwo);
            views.setOnClickPendingIntent(R.id.colour_button_three, pendingIntentThree);
            views.setOnClickPendingIntent(R.id.colour_button_four, pendingIntentFour);
            views.setOnClickPendingIntent(R.id.colour_button_five, pendingIntentFive);
            views.setOnClickPendingIntent(R.id.settings, configPendingIntent);


            updateAppWidget(context, appWidgetManager, appWidgetId);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        if (ACTION_BUTTON_ONE.equals(action)) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("button", "0");
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_TWO.equals(action)) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("button", "1");
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_THREE.equals(action)) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("button", "2");
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_FOUR.equals(action)) {
             Intent activityIntent = new Intent(context, MainActivity.class);
             activityIntent.putExtra("button", "3");
             context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_FIVE.equals(action)) {
             Intent activityIntent = new Intent(context, MainActivity.class);
             activityIntent.putExtra("button", "4");
             context.startActivity(activityIntent);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

