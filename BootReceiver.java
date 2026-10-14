package com.aynora.vitiligotracker;
import android.content.*;
public class BootReceiver extends BroadcastReceiver{ @Override public void onReceive(Context c,Intent i){Notifications.ensure(c); ReminderScheduler.scheduleAll(c); TrackerWidget.update(c);} }
