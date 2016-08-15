package com.douncoding.seepet.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.douncoding.seepet.Constant;
import com.douncoding.seepet.R;
import com.douncoding.seepet.net.ApiConnection;

public class MotorActivity extends AppCompatActivity {
    private static final String TAG = MotorActivity.class.getSimpleName();
    private static final String MOTOR_URL = "http://" + Constant.APP_SERVER_IP + "/motor";
    Button mMotorStart;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MotorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        mMotorStart = (Button)findViewById(R.id.motor_start);
        mMotorStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionMotor().execute();
            }
        });
    }

    class ActionMotor extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            // 현재 미사용
            int motorSeq = 0;

            try {
                ApiConnection.createGET(MOTOR_URL + "/" + motorSeq).requestSyncCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
