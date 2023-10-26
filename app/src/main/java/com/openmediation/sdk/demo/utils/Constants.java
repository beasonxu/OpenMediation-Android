// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.demo.utils;

import android.util.Log;

public class Constants {

    public static final String TAG = "OmDebug";
    public static boolean ENABLE_LOG = true;
    public static final String APPKEY = "OtnCjcU7ERE0D21GRoquiQBY6YXR3YLl";

    public static final String P_BANNER = "104";
    public static final String P_NATIVE = "105";
    public static final String P_SPLASH = "228";

    public static void printLog(String msg) {
        if (ENABLE_LOG) {
            Log.e(TAG, msg);
        }
    }
}
