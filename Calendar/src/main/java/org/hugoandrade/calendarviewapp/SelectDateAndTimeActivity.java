package org.hugoandrade.calendarviewapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


import org.hugoandrade.calendarviewapp.uihelpers.NumberPicker;
import org.hugoandrade.calendarviewlib.CalendarView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class SelectDateAndTimeActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_CALENDAR = "intent_extra_calendar";

    private CalendarView cvSelect;
    private NumberPicker npHour;
    private NumberPicker npMinute;

    private Calendar mCalendar;

    public static int hour,minute,month,day;
    public static Calendar currnetDate;
    public static String currentDDate;


//  AlarmManager alarmManager= (AlarmManager) ;


    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectDateAndTimeActivity.class);
    }

    public static Intent makeIntent(Context context, Calendar calendar) {
        return makeIntent(context).putExtra(INTENT_EXTRA_CALENDAR, calendar);
    }

    public static Calendar extractCalendarFromIntent(Intent data) {
        return (Calendar) data.getSerializableExtra(INTENT_EXTRA_CALENDAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        mCalendar = extractCalendarFromIntent(getIntent());
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, 8);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
        }

        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_select_date_and_time);

        View vGoBack = findViewById(R.id.v_go_back);
        vGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        View tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        View tvSet = findViewById(R.id.tv_set);
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        cvSelect = findViewById(R.id.cv_select_date);
        cvSelect.setCurrentDate(mCalendar);
        cvSelect.setOnItemClickedListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClicked(List<CalendarView.CalendarObject> calendarObjects,
                                      Calendar previousDate,
                                      Calendar selectedDate) {
                cvSelect.setCurrentDate(selectedDate);
            }
        });

        npHour = findViewById(R.id.np_hour);
        npHour.setValue(mCalendar.get(Calendar.HOUR_OF_DAY));
        npMinute = findViewById(R.id.np_minute);
        npMinute.setValue(mCalendar.get(Calendar.MINUTE));
    }

    private void confirm() {
         hour = npHour.getValue();
         minute = npMinute.getValue();

         currnetDate= Calendar.getInstance();
        currentDDate=DateFormat.getDateInstance().format(currnetDate.getTime());


         day=mCalendar.get(Calendar.DAY_OF_MONTH);
         month=mCalendar.get(Calendar.MONTH);




        mCalendar = cvSelect.getCurrentDate();
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.MILLISECOND, 0);

       // updateTimeText(mCalendar);
        //startAlarm(mCalendar);

        setResult(RESULT_OK, new Intent().putExtra(INTENT_EXTRA_CALENDAR, mCalendar));


        onBackPressed();
    }

    private void startAlarm(Calendar c){

        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
         if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }

    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
       // mTextView.setText("Alarm canceled");
    }
   /* private void updateTimeText(Calendar c){
        String timetext="Alram set";
        timetext+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c);


    }

    */



    /*
    class Alarm extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Alock");
            wl.acquire();
            Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

            wl.release();

        }
        public void setAlarm(Context context)
        {
            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
        }

        public void cancelAlarm(Context context)
        {
            Intent intent = new Intent(context, Alarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

     */
}
