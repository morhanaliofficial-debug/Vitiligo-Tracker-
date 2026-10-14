package com.aynora.vitiligotracker;

import android.app.*;
import android.content.*;
import android.os.Build;
import android.provider.Settings;
import java.util.*;

public final class ReminderScheduler {
    private ReminderScheduler(){}
    static int requestCode(String id){return id.hashCode() & 0x7fffffff;}
    static PendingIntent pi(Context c,String action,String id){
        Intent i=new Intent(c, ReminderReceiver.class).setAction(action).putExtra("id",id);
        return PendingIntent.getBroadcast(c,requestCode(id),i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
    }
    static void scheduleAll(Context c){
        for(Schedule.Event e: all(c)) scheduleNext(c,e);
    }
    static Schedule.Event[] all(Context c){return new Schedule.Event[]{Schedule.DAILY[0],Schedule.DAILY[1],Schedule.DAILY[2],Schedule.DAILY[3],Schedule.MTX,Schedule.PHOTO,Schedule.FOLIC};}
    static void scheduleNext(Context c, Schedule.Event e){
        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Calendar occurrence=Schedule.nextOccurrence(c,e,System.currentTimeMillis());
        Calendar reminder=(Calendar)occurrence.clone(); reminder.add(Calendar.MINUTE,-15);
        if(reminder.getTimeInMillis()<=System.currentTimeMillis()) reminder=occurrence;
        PendingIntent p=pi(c,"REMIND",e.id);
        if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()) am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(),p);
        else if(Build.VERSION.SDK_INT>=23) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(),p);
        else am.setExact(AlarmManager.RTC_WAKEUP,reminder.getTimeInMillis(),p);
    }
    static void repeat(Context c,String id){
        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        PendingIntent p=pi(c,"REMIND",id);
        long t=System.currentTimeMillis()+15*60*1000L;
        if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()) am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,t,p);
        else if(Build.VERSION.SDK_INT>=23) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,t,p); else am.setExact(AlarmManager.RTC_WAKEUP,t,p);
    }
    static void cancel(Context c,String id){AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE); am.cancel(pi(c,"REMIND",id));}
}
