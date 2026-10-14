package com.aynora.vitiligotracker;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.*;

final class Schedule {
    static final String PREFS="tracker";
    static final String FOLIC_TIME="folic_time";
    static final String CHANNEL="vitiligo_reminders";

    static final Event[] DAILY = new Event[]{
        new Event("pred_am","Prednisolone 5 mg","08:30","After morning meal",-1),
        new Event("tacrolimus","Tacrolimus","15:00","Hands, feet & face",-1),
        new Event("pred_pm","Prednisolone 5 mg","21:30","After evening meal",-1),
        new Event("steroid","Steroid cream","23:00","Hands, feet & face",-1)
    };
    static final Event MTX=new Event("mtx","Methotrexate 2.5 mg × 4 tablets (10 mg total)","12:45","Once weekly — Sunday",1);
    static final Event PHOTO=new Event("photo","Phototherapy","09:00","Wednesday",4);
    static final Event FOLIC=new Event("folic","Folic acid 5 mg","12:45","Once weekly — Thursday",5);

    static class Event {
        final String id,name,time,info; final int day;
        Event(String i,String n,String t,String inf,int d){id=i;name=n;time=t;info=inf;day=d;}
    }
    static String folicTime(Context c){return c.getSharedPreferences(PREFS,0).getString(FOLIC_TIME,"12:45");}
    static String time(Context c, Event e){return e.id.equals("folic")?folicTime(c):e.time;}
    static String dateKey(Calendar c){return new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(c.getTime());}
    static String doneKey(String id,Calendar c){return "done_"+dateKey(c)+"_"+id;}
    static boolean done(Context c,String id,Calendar d){return c.getSharedPreferences(PREFS,0).getBoolean(doneKey(id,d),false);}
    static void setDone(Context c,String id,Calendar d,boolean v){c.getSharedPreferences(PREFS,0).edit().putBoolean(doneKey(id,d),v).apply();}
    static Calendar nextOccurrence(Context c, Event e, long afterMillis){
        Calendar now=Calendar.getInstance(); now.setTimeInMillis(afterMillis);
        int[] hm=parse(time(c,e));
        Calendar target=(Calendar)now.clone(); target.set(Calendar.HOUR_OF_DAY,hm[0]); target.set(Calendar.MINUTE,hm[1]); target.set(Calendar.SECOND,0); target.set(Calendar.MILLISECOND,0);
        if(e.day<0){ if(target.getTimeInMillis()<=afterMillis) target.add(Calendar.DAY_OF_YEAR,1); }
        else { int delta=(e.day-target.get(Calendar.DAY_OF_WEEK)+7)%7; target.add(Calendar.DAY_OF_YEAR,delta); if(target.getTimeInMillis()<=afterMillis) target.add(Calendar.DAY_OF_YEAR,7); }
        return target;
    }
    static int[] parse(String s){String[] p=s.split(":"); return new int[]{Integer.parseInt(p[0]),Integer.parseInt(p[1])};}
    static String fmt(String s){int[] x=parse(s); int h=x[0]%12; if(h==0)h=12; return h+":"+String.format(Locale.US,"%02d",x[1])+" "+(x[0]>=12?"PM":"AM");}
}
