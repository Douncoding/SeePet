package com.douncoding.seepet.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.douncoding.seepet.Constant;
import com.douncoding.seepet.R;
import com.douncoding.seepet.net.ApiConnection;

import java.util.Locale;

public class MotorActivity extends AppCompatActivity {
    private static final String TAG = MotorActivity.class.getSimpleName();
    private static final String MOTOR_URL = "http://" + Constant.APP_SERVER_IP + "/motor";

    Button mMotorStart;

    TimePicker mTimePicker;
    int hourOfDay;
    int minute;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MotorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("먹이주기");
        }

        mTimePicker = (TimePicker)findViewById(R.id.timePicker);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hourOfDay = i;
                minute = i1;
            }
        });

        mMotorStart = (Button)findViewById(R.id.motor_start);
        mMotorStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionMotor().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class ActionMotor extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            // 현재 미사용
            int motorSeq = 0;
            String reservation = String.format(Locale.KOREA, "%d:%d", hourOfDay, minute);

            try {
                ApiConnection.createGET(MOTOR_URL + "/" + motorSeq + "?time=" + reservation)
                        .requestSyncCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
