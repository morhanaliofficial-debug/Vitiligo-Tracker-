package com.aynora.vitiligotracker;

import android.app.*;import android.content.*;import android.os.*;

final class Notifications {
 static void ensure(Context c){
   if(Build.VERSION.SDK_INT>=26){NotificationChannel ch=new NotificationChannel(Schedule.CHANNEL,"Treatment reminders",NotificationManager.IMPORTANCE_HIGH); ch.setDescription("Vitiligo treatment reminders"); ((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(ch);}
 }
 static void show(Context c,Schedule.Event e){
   ensure(c);
   Intent done=new Intent(c,DoneReceiver.class).setAction(ReminderReceiver.ACTION_DONE).putExtra("id",e.id);
   PendingIntent dp=PendingIntent.getBroadcast(c,e.id.hashCode(),done,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
   Intent open=new Intent(c,MainActivity.class); PendingIntent op=PendingIntent.getActivity(c,e.id.hashCode()+1,open,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
   Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,Schedule.CHANNEL):new Notification.Builder(c);
   b.setSmallIcon(com.aynora.vitiligotracker.R.drawable.ic_launcher).setContentTitle(e.name).setContentText("Scheduled at "+Schedule.fmt(Schedule.time(c,e))+" — mark Done when completed.").setContentIntent(op).setAutoCancel(false).setOngoing(true).addAction(new Notification.Action.Builder(null,"DONE",dp).build());
   ((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).notify(e.id.hashCode(),b.build());
 }
 static void clear(Context c,String id){((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id.hashCode());}
}
