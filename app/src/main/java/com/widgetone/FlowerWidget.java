package com.widgetone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import static com.widgetone.FlowerConfigActivity.FLOWER_KEY_TEXT;
import static com.widgetone.FlowerConfigActivity.VALUES_SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class FlowerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Intent selectIntent = new Intent(context, MainActivity.class);
        selectIntent.putExtra("flower", "flower");
        PendingIntent pendingIntentSelect = PendingIntent.getActivity(context,
                appWidgetId, selectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent configIntent = new Intent(context, FlowerConfigActivity.class).setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context,appWidgetId,configIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent penIntent = new Intent(context, DiaryActivity.class);
        PendingIntent penPendingIntent = PendingIntent.getActivity(context,appWidgetId,penIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flower__widget);

        views.setOnClickPendingIntent(R.id.pen,penPendingIntent);
        views.setOnClickPendingIntent(R.id.color_flower, pendingIntentSelect);
        views.setOnClickPendingIntent(R.id.settings, configPendingIntent);

        SharedPreferences preferences = context.getSharedPreferences(VALUES_SHARED_PREFS + String.valueOf(appWidgetId), Context.MODE_PRIVATE);
        String tText = preferences.getString(FLOWER_KEY_TEXT + String.valueOf(appWidgetId), "D");


        views.setCharSequence(R.id.topic_text, "setText", tText);



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();
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
                this.onDeleted(context, new int[]{appWidgetId});
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
            this.onEnabled(context);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
            this.onDisabled(context);
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

