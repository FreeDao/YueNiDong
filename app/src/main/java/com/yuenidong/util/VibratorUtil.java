
package com.yuenidong.util;

import android.content.Context;
import android.os.Vibrator;

import com.yuenidong.common.AppData;


/**
 * 振动器
 * Created by 石岩 on 14-12-31.
 */
public class VibratorUtil {
    public static final long SHORT_VIBRATE_DURATION = 40;

    public static final long LONG_BIBRATE_DURATION = 60;
    //经过duration时间后震动器震动一次
    public static void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) AppData.getContext().getSystemService(
                Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }
}
