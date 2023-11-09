// Copyright 2019 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.crosspromotion.sdk.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.provider.Browser;

import com.openmediation.sdk.utils.DeveloperLog;
import com.openmediation.sdk.utils.crash.CrashUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GpUtil {
    public static boolean goGp(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            return false;
        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = null;
            if (url.startsWith("intent://")) {
                try {
                    intent = parseIntent(url);
                    if (intent != null) {
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        return true;
                    }
                } catch (ActivityNotFoundException e) {
                    DeveloperLog.LogD("GpUtil", e);
                    CrashUtil.getSingleton().saveException(e);
                    if (intent != null && intent.getExtras() != null) {
                        String ref = intent.getExtras().getString("market_referrer");
                        String market_url = "market://details?id=" + intent.getPackage() + "&referrer=" + ref;
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(market_url));
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.vending");
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else if (url.startsWith("market://")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.vending");
                intent.putExtra("callerId", context.getPackageName());
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                //intent.setPackage("com.android.vending");
                intent.setPackage(context.getPackageName());
                intent.putExtra("com.google.chrome.transition_type", 0);
                intent.putExtra("org.chromium.chrome.browser.tab_launch_type", 0);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true);
                try {
                    Class<?> launcherClass = Class.forName("org.chromium.chrome.browser.ChromeTabbedActivity");
                    intent.setClass(context, launcherClass);
                } catch (Throwable e) {
                }

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            DeveloperLog.LogD("GpUtil", e);
            CrashUtil.getSingleton().saveException(e);
        }
        return false;
    }

    public static boolean isGp(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        try {
            Uri dst = Uri.parse(url);
            if (dst == null) {
                return false;
            }

            String scheme = lowerCase(dst.getScheme());
            String host = lowerCase(dst.getHost());
            return "market".equals(scheme)
                    || "play.google.com".equals(host)
                    || "mobile.gmarket.co.kr".equals(host);
        } catch (Exception e) {
            DeveloperLog.LogD("GpUtil", e);
            CrashUtil.getSingleton().saveException(e);
            return false;
        }
    }

    private static String lowerCase(String s) {
        return s == null ? null : s.toLowerCase();
    }

    public static Intent parseIntent(String url) {
        // performs generic parsing of the URI to turn it into an Intent.
        try {
            int i = url.indexOf("%23Intent&");
            if (i != -1) {
                String before = url.substring(0, i);
                String after = url.substring(i + 3);
                after = after.replace('&', ';');
                url = before + '#' + after;
            }
            return Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            DeveloperLog.LogD("GpUtil", e);
            CrashUtil.getSingleton().saveException(e);
        }
        return null;
    }
}
