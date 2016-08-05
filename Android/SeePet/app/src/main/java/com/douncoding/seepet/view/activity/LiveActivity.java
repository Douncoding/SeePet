package com.douncoding.seepet.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.VideoView;

import com.douncoding.seepet.R;
import com.douncoding.seepet.net.ApiConnection;

/**
 *
 */
public class LiveActivity extends BaseActivity {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private static String LIVE_URL = "rtsp://192.168.123.200:80/live/picam";

    VideoView mVideoView;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, LiveActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        mVideoView = (VideoView)findViewById(R.id.videoView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isThereLiveReady(new DefaultCallback() {
            @Override
            public void onCallback(Object obj) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    /**
     * 서버의 스트리밍 서비스가 가능한지 확인
     */
    private void isThereLiveReady(final DefaultCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String response = ApiConnection.createGET("/live").requestSyncCall();
                    Log.d(TAG, "서버 스트리밍 상태:" + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                if (callback != null) {
                    callback.onCallback(response);
                }
            }
        }.execute();
    }

    /**
     * 서버 응답 공통 콜백리스너
     */
    private interface DefaultCallback {
        void onCallback(Object obj);
    }
}
