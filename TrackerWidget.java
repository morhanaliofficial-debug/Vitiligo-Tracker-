package com.aynora.vitiligotracker;
import android.appwidget.*;import android.content.*;import android.widget.*;import java.util.*;
public class TrackerWidget extends AppWidgetProvider{
 public static void update(Context c){AppWidgetManager m=AppWidgetManager.getInstance(c); ComponentName n=new ComponentName(c,TrackerWidget.class); for(int id:m.getAppWidgetIds(n)) updateOne(c,m,id);}
 static void updateOne(Context c,AppWidgetManager m,int id){RemoteViews v=new RemoteViews(c.getPackageName(),R.layout.widget_layout); Schedule.Event next=null; Calendar d=Calendar.getInstance(); for(Schedule.Event e:ReminderScheduler.all(c)){if(!Schedule.done(c,e.id,d)){Calendar o=Schedule.nextOccurrence(c,e,System.currentTimeMillis()-1); if(next==null || o.before(Schedule.nextOccurrence(c,next,System.currentTimeMillis()-1)))next=e;}} if(next!=null)v.setTextViewText(R.id.widget_next,next.name+"\n"+Schedule.fmt(Schedule.time(c,next))+" — "+next.info); Intent i=new Intent(c,MainActivity.class); v.setOnClickPendingIntent(R.id.widget_open,PendingIntent.getActivity(c,9001,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE)); m.updateAppWidget(id,v);}
 @Override public void onUpdate(Context c,AppWidgetManager m,int[] ids){for(int id:ids)updateOne(c,m,id);}
}
