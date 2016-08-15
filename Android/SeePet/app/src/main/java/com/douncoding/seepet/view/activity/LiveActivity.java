package com.douncoding.seepet.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.douncoding.seepet.R;
import com.douncoding.seepet.net.ApiConnection;

/**
 *
 */
public class LiveActivity extends BaseActivity {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private static String LIVE_URL = "rtsp://192.168.123.200:80/live/picam";
    private static String LIVE_CHECK_URL = "http://192.168.123.200:3000/live";

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, LiveActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isThereLiveReady(new DefaultCallback() {
            @Override
            public void onCallback(Object obj) {
                if ((Boolean)obj) { // 서비스 상태가 정상인 경우 스트리밍 시작
                    startRtspStreaming();
                } else { // 서비스 상태가 비정상인 경우 경고창
                    showAlertDialog("스트리밍을 시작할 수 없습니다. 상태를 확인하세요.");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * RTSP 시작
     */
    private void startRtspStreaming() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LIVE_URL));
        startActivity(intent);
        finish();
    }

    /**
     * 경고창 출력
     * @param message 보여줄 메시지
     */
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    /**
     * 서버의 스트리밍 서비스가 가능한지 확인
     */
    private void isThereLiveReady(final DefaultCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    String response = ApiConnection.createGET(LIVE_CHECK_URL).requestSyncCall();
                    Log.d(TAG, "서버 스트리밍 상태:" + response);
                    return response.equals("OK") ? true : false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean response) {
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
