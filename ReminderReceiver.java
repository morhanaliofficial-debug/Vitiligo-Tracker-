package com.aynora.vitiligotracker;

import android.app.*;import android.content.*;import android.os.*;import java.util.*;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String ACTION_DONE="com.aynora.vitiligotracker.DONE";
    @Override public void onReceive(Context c,Intent intent){
        String id=intent.getStringExtra("id"); if(id==null)return;
        Schedule.Event e=find(c,id); if(e==null)return;
        Calendar now=Calendar.getInstance();
        if(Schedule.done(c,id,now)){ReminderScheduler.scheduleNext(c,e);return;}
        Notifications.show(c,e);
        ReminderScheduler.repeat(c,id);
    }
    static Schedule.Event find(Context c,String id){for(Schedule.Event e:ReminderScheduler.all(c))if(e.id.equals(id))return e;return null;}
}
