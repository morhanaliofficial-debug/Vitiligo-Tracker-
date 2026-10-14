package com.aynora.vitiligotracker;

import android.Manifest;import android.app.*;import android.content.*;import android.net.Uri;import android.os.*;import android.provider.Settings;import android.graphics.Color;import android.view.*;import android.widget.*;import java.util.*;

public class MainActivity extends Activity{
 LinearLayout root,today,weekly;
 int purple=Color.rgb(107,78,255);
 @Override public void onCreate(Bundle b){super.onCreate(b); Notifications.ensure(this); build(); ReminderScheduler.scheduleAll(this); if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=0)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},44); if(Build.VERSION.SDK_INT>=31){try{if(!((AlarmManager)getSystemService(ALARM_SERVICE)).canScheduleExactAlarms())startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:"+getPackageName())));}catch(Exception ignored){}} }
 TextView tv(String s,int sp){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(Color.rgb(40,35,48));t.setPadding(0,6,0,6);return t;}
 void build(){
  ScrollView sv=new ScrollView(this); root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,22,28,32);sv.addView(root);setContentView(sv);
  TextView h=tv("Vitiligo Treatment Tracker",25);h.setTypeface(null,1);root.addView(h);root.addView(tv("Daily checklist + recurring reminders",14));
  Button n=new Button(this);n.setText("Enable / check notifications");n.setOnClickListener(v->{if(Build.VERSION.SDK_INT>=33)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},44); if(Build.VERSION.SDK_INT>=31)try{startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,Uri.parse("package:"+getPackageName())));}catch(Exception ignored){}});root.addView(n);
  TextView note=tv("Methotrexate is kept as 4 × 2.5 mg together once weekly on Sunday. Folic acid is Thursday; its time is a tracker setting because no time was prescribed.",13);note.setTextColor(Color.DKGRAY);root.addView(note);
  root.addView(tv("TODAY",18));today=new LinearLayout(this);today.setOrientation(LinearLayout.VERTICAL);root.addView(today);
  root.addView(tv("WEEKLY",18));weekly=new LinearLayout(this);weekly.setOrientation(LinearLayout.VERTICAL);root.addView(weekly);
  Button folic=new Button(this);folic.setText("Set Thursday folic-acid time");folic.setOnClickListener(v->pickFolic());root.addView(folic); render();
 }
 void pickFolic(){String cur=Schedule.folicTime(this);int[] hm=Schedule.parse(cur);TimePickerDialog d=new TimePickerDialog(this,(view,h,m)->{String s=String.format(Locale.US,"%02d:%02d",h,m);getSharedPreferences(Schedule.PREFS,0).edit().putString(Schedule.FOLIC_TIME,s).apply();ReminderScheduler.scheduleAll(this);render();TrackerWidget.update(this);},hm[0],hm[1],false);d.setTitle("Thursday folic acid time");d.show();}
 void render(){if(today==null)return;today.removeAllViews();Calendar d=Calendar.getInstance();for(Schedule.Event e:eventsFor(d)) addRow(today,e,d);weekly.removeAllViews();addWeek(weekly,Schedule.MTX);addWeek(weekly,Schedule.PHOTO);addWeek(weekly,Schedule.FOLIC);}
 ArrayList<Schedule.Event> eventsFor(Calendar d){ArrayList<Schedule.Event>a=new ArrayList<>();Collections.addAll(a,Schedule.DAILY);if(d.get(Calendar.DAY_OF_WEEK)==1)a.add(Schedule.MTX);if(d.get(Calendar.DAY_OF_WEEK)==4)a.add(Schedule.PHOTO);if(d.get(Calendar.DAY_OF_WEEK)==5)a.add(Schedule.FOLIC);a.sort(Comparator.comparing(e->Schedule.time(this,e)));return a;}
 void addRow(LinearLayout box,Schedule.Event e,Calendar d){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.HORIZONTAL);r.setPadding(0,10,0,10);LinearLayout info=new LinearLayout(this);info.setOrientation(LinearLayout.VERTICAL);info.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));TextView a=tv(e.name,15);a.setTypeface(null,1);info.addView(a);info.addView(tv(Schedule.fmt(Schedule.time(this,e))+" · "+e.info,12));r.addView(info);Button b=new Button(this);boolean done=Schedule.done(this,e.id,d);b.setText(done?"Done ✓":"Done");b.setOnClickListener(v->{Calendar now=Calendar.getInstance();if(Schedule.done(this,e.id,now)){Schedule.setDone(this,e.id,now,false);ReminderScheduler.scheduleNext(this,e);}else{Schedule.setDone(this,e.id,now,true);ReminderScheduler.cancel(this,e.id);Notifications.clear(this,e.id);ReminderScheduler.scheduleNext(this,e);}render();TrackerWidget.update(this);});r.addView(b);box.addView(r);}
 void addWeek(LinearLayout box,Schedule.Event e){box.addView(tv(e.name+" — "+day(e.day)+" · "+Schedule.fmt(Schedule.time(this,e))+" · "+e.info,14));}
 String day(int d){switch(d){case 1:return "Sunday";case 2:return "Monday";case 3:return "Tuesday";case 4:return "Wednesday";case 5:return "Thursday";case 6:return "Friday";default:return "Saturday";}}
 @Override protected void onResume(){super.onResume();if(root!=null){render();ReminderScheduler.scheduleAll(this);}}
}
