// Copyright 2019 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.crosspromotion.sdk.utils.webview;

import android.content.Context;
import android.util.Log;

import com.openmediation.sdk.utils.AdtUtil;
import com.openmediation.sdk.utils.DeveloperLog;
import com.openmediation.sdk.utils.HandlerUtil;
import com.openmediation.sdk.utils.crash.CrashUtil;

public class AdsWebView {
    private static final String TAG = "AdsWebView";

    private BaseWebView mAdView;
    private boolean isDestroyed;

    private static final class AdtWebViewHolder {
        private static AdsWebView sInstance = new AdsWebView();
    }

    private AdsWebView() {
    }

    public static AdsWebView getInstance() {
        return AdtWebViewHolder.sInstance;
    }


    public void init(final Context context) {
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mAdView == null || isDestroyed) {
                        Log.i(TAG, "start to create BaseWebView");
                        mAdView = new BaseWebView(context.getApplicationContext());
                    }
                    isDestroyed = false;
                    mAdView.loadUrl("about:blank");
                } catch (Throwable e) {
                    DeveloperLog.LogD("AdsWebView", e);
                    CrashUtil.getSingleton().saveException(e);
                }
            }
        });
    }

    public BaseWebView getAdView() {
        if (isDestroyed) {
            init(AdtUtil.getInstance().getApplicationContext());
            return mAdView;
        }
        return mAdView;
    }

    public void destroy(BaseWebView baseWebView, String jsName) {
        if (baseWebView == null) {
            return;
        }
        baseWebView.removeAllViews();
        baseWebView.removeJavascriptInterface(jsName);
        baseWebView.setWebViewClient(null);
        baseWebView.setWebChromeClient(null);
        baseWebView.freeMemory();
        baseWebView.destroy();
        isDestroyed = true;
    }
}
