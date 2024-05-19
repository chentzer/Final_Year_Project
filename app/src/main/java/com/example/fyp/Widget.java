package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: Widget.java
//Description: To manage the SOS Widget operation
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 29-march-2022

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("widget", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.SOSWidget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } //responsible for updating all widget
    }
}
