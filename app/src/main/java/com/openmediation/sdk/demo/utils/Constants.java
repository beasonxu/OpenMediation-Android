// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.demo.utils;

import android.util.Log;

public class Constants {

    public static final String TAG = "OmDebug";
    public static boolean ENABLE_LOG = true;
    public static final String APPKEY = "yHtyiIvylOW0RjcuWvIO9mrL63X9vtcH";

    public static final String P_BANNER = "104";
    public static final String P_NATIVE = "229";
    public static final String P_NATIVE_CAROUSEL_0 = "229";
    public static final String P_NATIVE_CAROUSEL_1 = "232";
    public static final String P_NATIVE_CAROUSEL_2 = "233";
    public static final String P_SPLASH = "231";
    public static final String P_REWARD = "230";


    public static void printLog(String msg) {
        if (ENABLE_LOG) {
            Log.e(TAG, msg);
        }
    }
}
