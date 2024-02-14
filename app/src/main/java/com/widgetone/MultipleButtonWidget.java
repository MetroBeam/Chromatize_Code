package com.widgetone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;


import static com.widgetone.MultipleConfigActivity.KEY_COLOR_FIVE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_FOUR;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_ONE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_THREE;
import static com.widgetone.MultipleConfigActivity.KEY_COLOR_TWO;
import static com.widgetone.MultipleConfigActivity.KEY_TEXT;


import  static com.widgetone.MultipleConfigActivity.KEY_VISIBILITY_ONE;
import  static com.widgetone.MultipleConfigActivity.KEY_VISIBILITY_TWO;
import  static com.widgetone.MultipleConfigActivity.KEY_VISIBILITY_THREE;
import  static com.widgetone.MultipleConfigActivity.KEY_VISIBILITY_FOUR;
import  static com.widgetone.MultipleConfigActivity.KEY_VISIBILITY_FIVE;

import static com.widgetone.MultipleConfigActivity.VALUES_SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class MultipleButtonWidget extends AppWidgetProvider {


    public static final String ACTION_BUTTON_ONE = "ACTION_BUTTON_ONE";
    public static final String ACTION_BUTTON_TWO = "ACTION_BUTTON_TWO";
    public static final String ACTION_BUTTON_THREE = "ACTION_BUTTON_THREE";
    public static final String ACTION_BUTTON_FOUR = "ACTION_BUTTON_FOUR";
    public static final String ACTION_BUTTON_FIVE = "ACTION_BUTTON_FIVE";

    //private int thisAppWidgetId;



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {




        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {





           /* SharedPreferences idPreferences = context.getSharedPreferences(ID_SHARED_PREFS, Context.MODE_PRIVATE);
            int incomingAppWidgetId = idPreferences.getInt(Key_WIDGETID, 0);

            if (incomingAppWidgetId != appWidgetManager.INVALID_APPWIDGET_ID) {
                updateMultipleAppWidget(appWidgetManager,incomingAppWidgetId, intent);
            }
            */

            SharedPreferences valuePreferences = context.getSharedPreferences(VALUES_SHARED_PREFS + String.valueOf(appWidgetId), Context.MODE_PRIVATE);


            String tText = valuePreferences.getString(KEY_TEXT + String.valueOf(appWidgetId), "D");
            String colorOneInt = valuePreferences.getString(KEY_COLOR_ONE + String.valueOf(appWidgetId), "0");
            String colorTwoInt = valuePreferences.getString(KEY_COLOR_TWO + String.valueOf(appWidgetId), "0");
            String colorThreeInt = valuePreferences.getString(KEY_COLOR_THREE + String.valueOf(appWidgetId), "0");
            String colorFourInt = valuePreferences.getString(KEY_COLOR_FOUR + String.valueOf(appWidgetId), "0");
            String colorFiveInt = valuePreferences.getString(KEY_COLOR_FIVE + String.valueOf(appWidgetId), "0");

            int oneVisibility = Integer.parseInt(valuePreferences.getString(KEY_VISIBILITY_ONE + String.valueOf(appWidgetId), "0"));
            int twoVisibility = Integer.parseInt(valuePreferences.getString(KEY_VISIBILITY_TWO + String.valueOf(appWidgetId), "0"));
            int threeVisibility = Integer.parseInt(valuePreferences.getString(KEY_VISIBILITY_THREE + String.valueOf(appWidgetId), "0"));
            int fourVisibility = Integer.parseInt(valuePreferences.getString(KEY_VISIBILITY_FOUR + String.valueOf(appWidgetId), "0"));
            int fiveVisibility = Integer.parseInt(valuePreferences.getString(KEY_VISIBILITY_FIVE + String.valueOf(appWidgetId), "0"));


            Intent buttonOneIntent = new Intent(context, MultipleButtonWidget.class).setAction(ACTION_BUTTON_ONE);
            buttonOneIntent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntentOne = PendingIntent.getBroadcast(context,
                    appWidgetId, buttonOneIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent buttonTwoIntent = new Intent(context, MultipleButtonWidget.class).setAction(ACTION_BUTTON_TWO);
            buttonTwoIntent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntentTwo = PendingIntent.getBroadcast(context,
                   appWidgetId, buttonTwoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent buttonThreeIntent = new Intent(context, MultipleButtonWidget.class).setAction(ACTION_BUTTON_THREE);
            buttonThreeIntent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntentThree = PendingIntent.getBroadcast(context,
                    appWidgetId, buttonThreeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent buttonFourIntent = new Intent(context, MultipleButtonWidget.class).setAction(ACTION_BUTTON_FOUR);
            buttonFourIntent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntentFour = PendingIntent.getBroadcast(context,
                    appWidgetId, buttonFourIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent buttonFiveIntent = new Intent(context, MultipleButtonWidget.class).setAction(ACTION_BUTTON_FIVE);
            buttonFiveIntent.putExtra (AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntentFive = PendingIntent.getBroadcast(context,
                    appWidgetId, buttonFiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);



            Intent penIntent = new Intent(context, DiaryActivity.class);
            penIntent.putExtra("alarm note", "Reflect on what it was like to use the app today");
            PendingIntent penPendingIntent = PendingIntent.getActivity(context,appWidgetId,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            Intent configIntent = new Intent(context, MultipleConfigActivity.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context,appWidgetId,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);




            //Intent serviceIntent = new Intent(context, WidgetService.class);
            //serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)))

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

            views.setViewVisibility(R.id.frame_one, oneVisibility);
            views.setViewVisibility(R.id.frame_two, twoVisibility);
            views.setViewVisibility(R.id.frame_three, threeVisibility);
            views.setViewVisibility(R.id.frame_four, fourVisibility);
            views.setViewVisibility(R.id.frame_five, fiveVisibility);

            //views.setRemoteAdapter(R.id.widget_listview, serviceIntent);


            views.setOnClickPendingIntent(R.id.pen,penPendingIntent);
            views.setOnClickPendingIntent(R.id.colour_button_one, pendingIntentOne);
            views.setOnClickPendingIntent(R.id.colour_button_two, pendingIntentTwo);
            views.setOnClickPendingIntent(R.id.colour_button_three, pendingIntentThree);
            views.setOnClickPendingIntent(R.id.colour_button_four, pendingIntentFour);
            views.setOnClickPendingIntent(R.id.colour_button_five, pendingIntentFive);
            views.setOnClickPendingIntent(R.id.settings, configPendingIntent);

           // thisAppWidgetId = appWidgetId;


            updateAppWidget(context, appWidgetManager, appWidgetId);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        int thisAppWidgetId = 0;


         final   String action = intent.getAction();
            if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int[] appWidgetIds = extras
                            .getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                    if (appWidgetIds.length > 0) {
                        this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);//here you can call onUpdate method, and update your views as you wish
                    }
                }
            } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras != null
                        && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                    final int appWidgetId = extras
                            .getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                    this.onDeleted(context, new int[] { appWidgetId });
                }
            } else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
                this.onEnabled(context);
            } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
                this.onDisabled(context);
            }




        if (ACTION_BUTTON_ONE.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                thisAppWidgetId= extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("multiple", "0" + "," + String.valueOf(thisAppWidgetId));
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_TWO.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                thisAppWidgetId= extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("multiple", "1" + "," + String.valueOf(thisAppWidgetId));
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_THREE.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                thisAppWidgetId= extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("multiple", "2"+ "," + String.valueOf(thisAppWidgetId));
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_FOUR.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                thisAppWidgetId= extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            Intent activityIntent = new Intent(context, MainActivity.class);
             activityIntent.putExtra("multiple", "3"+ "," + String.valueOf(thisAppWidgetId));
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }

        if (ACTION_BUTTON_FIVE.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                thisAppWidgetId= extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            Intent activityIntent = new Intent(context, MainActivity.class);
             activityIntent.putExtra("multiple", "4"+ "," + String.valueOf(thisAppWidgetId));
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

