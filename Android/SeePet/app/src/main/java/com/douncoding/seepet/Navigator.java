package com.douncoding.seepet;

import android.content.Context;
import android.content.Intent;

import com.douncoding.seepet.view.activity.LiveActivity;

/**
 * Created by douncoding on 16. 8. 5..
 */
public class Navigator {

    /**
     * 실시간 스트리밍 액티비티 호출
     */
    public static void navigateToLive(Context context) {
        if (context != null) {
            Intent intent = LiveActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }
}
