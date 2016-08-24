package com.douncoding.seepet.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.douncoding.seepet.Navigator;
import com.douncoding.seepet.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup mLiveButton;
    ViewGroup mVideoButton;
    ViewGroup mMotorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLiveButton = (ViewGroup)findViewById(R.id.live);
        mVideoButton = (ViewGroup)findViewById(R.id.video);
        mMotorButton = (ViewGroup)findViewById(R.id.motor);

        mLiveButton.setOnClickListener(this);
        mVideoButton.setOnClickListener(this);
        mMotorButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.live:
                Navigator.navigateToLive(this);
                break;
            case R.id.video:
                Navigator.navigateToMedia(this);
                break;
            case R.id.motor:
                Navigator.navigateToMotor(this);
                break;
        }
    }
}
