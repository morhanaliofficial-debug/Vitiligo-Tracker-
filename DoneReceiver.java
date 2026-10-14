package com.aynora.vitiligotracker;
import android.content.*;import java.util.*;
public class DoneReceiver extends BroadcastReceiver{
 @Override public void onReceive(Context c,Intent i){String id=i.getStringExtra("id"); if(id==null)return; Schedule.Event e=ReminderReceiver.find(c,id); if(e==null)return; Calendar d=Calendar.getInstance(); Schedule.setDone(c,id,d,true); ReminderScheduler.cancel(c,id); Notifications.clear(c,id); ReminderScheduler.scheduleNext(c,e); TrackerWidget.update(c); }
}
