package org.hugoandrade.calendarviewapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.hugoandrade.calendarviewapp.data.Event;
import org.hugoandrade.calendarviewapp.utils.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int ACTION_DELETE = 1;
    public static final int ACTION_EDIT = 2;
    public static final int ACTION_CREATE = 3;

    private static final String INTENT_EXTRA_ACTION = "intent_extra_action";
    private static final String INTENT_EXTRA_EVENT = "intent_extra_event";
    private static final String INTENT_EXTRA_CALENDAR = "intent_extra_calendar";

    private static final int SET_DATE_AND_TIME_REQUEST_CODE = 200;

    private final static SimpleDateFormat dateFormat
            = new SimpleDateFormat("EEEE, dd/MM    HH:mm", Locale.getDefault());

    private Event mOriginalEvent;

    private Calendar mCalendar;
    private String mTitle;
    private boolean mIsComplete;
    private int mColor;

    private boolean isViewMode = true;

    private EditText mTitleView;
    private Switch mIsCompleteCheckBox;
    private TextView mDateTextView;
    private CardView mColorCardView;
    private View mHeader;
    //  EditText t= findViewById(R.id.et_event_title);
    //  Checkbox ch= findViewById(R.id.checkbox_completed);
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    //  EditText t= findViewById(R.id.et_event_title);
    //  Checkbox ch= findViewById(R.id.checkbox_completed);

    //mostly the alarm manager will be here

    public static Intent makeIntent(Context context, @NonNull Calendar calendar) {
        return new Intent(context, CreateEventActivity.class).putExtra(INTENT_EXTRA_CALENDAR, calendar);
    }

    public static Intent makeIntent(Context context, @NonNull Event event) {
        return new Intent(context, CreateEventActivity.class).putExtra(INTENT_EXTRA_EVENT, event);
    }

    public static Event extractEventFromIntent(Intent intent) {
        return intent.getParcelableExtra(INTENT_EXTRA_EVENT);
    }

    public static int extractActionFromIntent(Intent intent) {
        return intent.getIntExtra(INTENT_EXTRA_ACTION, 0);
    }

    public static Calendar extractCalendarFromIntent(Intent intent) {
        return (Calendar) intent.getSerializableExtra(INTENT_EXTRA_CALENDAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setResult(RESULT_CANCELED);

        extractDataFromIntentAndInitialize();
        initializeUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            delete();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void extractDataFromIntentAndInitialize() {

        mOriginalEvent = extractEventFromIntent(getIntent());

        if (mOriginalEvent == null) {
            mCalendar = extractCalendarFromIntent(getIntent());
            if (mCalendar == null)
                mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, 8);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
            mColor = ColorUtils.mColors[0];
            mTitle = "";
            mIsComplete = false;
            isViewMode = false;
        } else {

            mCalendar = Calendar.getInstance();
            mCalendar.setTime(mOriginalEvent.getDate());
            mColor = mOriginalEvent.getColor();
            mTitle = mOriginalEvent.getTitle();
            mIsComplete = mOriginalEvent.isCompleted();
            isViewMode = true;
        }
        ////..........................
        //   EventRef.setValue();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_create_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mHeader = findViewById(R.id.ll_header);
        mHeader.setVisibility(View.VISIBLE);

        setupToolbar();

        View tvSave = mHeader.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        View tvCancel = mHeader.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                if (mOriginalEvent == null)
                    overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
            }
        });

        mDateTextView = findViewById(R.id.tv_date);
        mDateTextView.setText(dateFormat.format(mCalendar.getTime()));
        mDateTextView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Activity context = CreateEventActivity.this;
                Intent intent = SelectDateAndTimeActivity.makeIntent(context, mCalendar);

                startActivityForResult(intent,
                        SET_DATE_AND_TIME_REQUEST_CODE,
                        ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
            }
        });

        mColorCardView = findViewById(R.id.cardView_event_color);
        mColorCardView.setCardBackgroundColor(mColor);
        mColorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectColorDialog.Builder.instance(CreateEventActivity.this)
                        .setSelectedColor(mColor)
                        .setOnColorSelectedListener(new SelectColorDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                mColor = color;
                                mColorCardView.setCardBackgroundColor(mColor);
                            }
                        })
                        .create()
                        .show();
            }
        });
        mTitleView = findViewById(R.id.et_event_title);
        mTitleView.setText(mTitle);
        mIsCompleteCheckBox = findViewById(R.id.checkbox_completed);
        mIsCompleteCheckBox.setChecked(mIsComplete);

        if (isViewMode) {
            mIsCompleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setupEditMode();
                    mIsCompleteCheckBox.setOnCheckedChangeListener(null);
                }
            });
            mTitleView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setupEditMode();
                    mTitleView.setOnFocusChangeListener(null);
                }
            });
        } else {
            setupEditMode();
        }
    }

    private void setupEditMode() {
        if (isViewMode) {
            isViewMode = false;
            setupToolbar();
        }
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            if (isViewMode)
                getSupportActionBar().show();
            else
                getSupportActionBar().hide();
        }

        if (mHeader != null) {
            mHeader.setVisibility(isViewMode ? View.GONE : View.VISIBLE);
        }
    }

    private void delete() {
        Log.e(getClass().getSimpleName(), "delete");

        FirebaseDatabase.getInstance().getReference("Events").child(FirebaseAuth.getInstance().getUid())
                .child(mOriginalEvent.id).removeValue();

        setResult(RESULT_OK, new Intent()
                .putExtra(INTENT_EXTRA_ACTION, ACTION_DELETE)
                .putExtra(INTENT_EXTRA_EVENT, mOriginalEvent));
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);

    }

    // set the alarm manager here
    private void save() {

        int action = mOriginalEvent != null ? ACTION_EDIT : ACTION_CREATE;
        String id = mOriginalEvent != null ? mOriginalEvent.getId() : generateID();
        String rawTitle = mTitleView.getText().toString().trim();
      /*  int a1=  mCalendar.get(Calendar.MINUTE);
        int a2= mCalendar.get(Calendar.MILLISECOND);
        int a3= mCalendar.get(Calendar.HOUR_OF_DAY);
        int a4= mCalendar.get(Calendar.SECOND);*/


      /*  int a1=  mCalendar.get(Calendar.MINUTE);
        int a2= mCalendar.get(Calendar.MILLISECOND);
        int a3= mCalendar.get(Calendar.HOUR_OF_DAY);
        int a4= mCalendar.get(Calendar.SECOND);*/


        mOriginalEvent = new Event(
                id,
                rawTitle.isEmpty() ? null : rawTitle,
                mCalendar.getTime(),
                mColor,
                mIsCompleteCheckBox.isChecked()
        );

        //   DatabaseReference  mDatabaseReference = mDatabase.getReference().child("Event");


        setResult(RESULT_OK, new Intent()
                .putExtra(INTENT_EXTRA_ACTION, action)
                .putExtra(INTENT_EXTRA_EVENT, mOriginalEvent));
        finish();

        if (action == ACTION_CREATE)
            overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
        //inal FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference ref = database.getReference("Events");
        FirebaseDatabase.getInstance().getReference("Events").child(FirebaseAuth.getInstance().getUid()).child(id).setValue(mOriginalEvent);
        //  SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //String mDate = sdf.format(new date(mCalendar.getDate()));


      //  startAlarm(mCalendar);


    }

    private void startAlarm(Calendar c) {

        int hour = SelectDateAndTimeActivity.hour;
        int min = SelectDateAndTimeActivity.minute;
        String currnetTime = SelectDateAndTimeActivity.currentDDate;
        Calendar currentTimee = SelectDateAndTimeActivity.currnetDate;

        int month = SelectDateAndTimeActivity.month;
        int day = SelectDateAndTimeActivity.day;
int hh=8;
       // int hh = currentTimee.get(Calendar.HOUR);
        int mm = 12;

        //currentTimee.get(Calendar.MINUTE);

        int dd = 17;
               // currentTimee.get(Calendar.DAY_OF_MONTH);
        int momo = 8;
                // currentTimee.get(Calendar.MONTH);


        //if (hour < hh && min < mm) {


        //  if (month < momo && day < dd) {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);


     /*   if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

      */


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }
    // }

    // }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        // mTextView.setText("Alarm canceled");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SET_DATE_AND_TIME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                mCalendar = SelectDateAndTimeActivity.extractCalendarFromIntent(data);
                mDateTextView.setText(dateFormat.format(mCalendar.getTime()));

                setupEditMode();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static String generateID() {
        return Long.toString(System.currentTimeMillis());
    }

    @Override
    public void onClick(View view) {

    }
}
