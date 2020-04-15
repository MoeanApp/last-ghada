package org.hugoandrade.calendarviewapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/*import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;*/

public class MainActivity extends AppCompatActivity {
    //  private DatabaseReference EventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUKLocale(this);
        // UserRef=FirebaseDatabase.getInstance().getReference().child("Events");
        initializeUI();
    }

    private void initializeUI() {

        Context context = MainActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(CalendarViewWithNotesActivitySDK21.makeIntent(context));
        } else {
            startActivity(CalendarViewWithNotesActivity.makeIntent(context));
        }
        finish();

    }

    private static void setUKLocale(Context context) {
        Locale.setDefault(new Locale("AR"));
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(new Locale("AR"));
        res.updateConfiguration(config, res.getDisplayMetrics());
    }


}
