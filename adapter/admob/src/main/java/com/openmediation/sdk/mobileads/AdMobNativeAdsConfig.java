/*
 * // Copyright 2021 ADTIMING TECHNOLOGY COMPANY LIMITED
 * // Licensed under the GNU Lesser General Public License Version 3
 */

package com.openmediation.sdk.mobileads;

import android.widget.TextView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdMobNativeAdsConfig {
    private AdLoader mAdLoader;
    private NativeAd mAdMobNativeAd;
    private NativeAdView mUnifiedNativeAdView;

    private TextView mAdvertiserView;

    public AdLoader getAdLoader() {
        return mAdLoader;
    }

    public void setAdLoader(AdLoader adLoader) {
        this.mAdLoader = adLoader;
    }

    public NativeAd getAdMobNativeAd() {
        return mAdMobNativeAd;
    }

    public void setAdMobNativeAd(NativeAd nativeAd) {
        this.mAdMobNativeAd = nativeAd;
    }

    public NativeAdView getUnifiedNativeAdView() {
        return mUnifiedNativeAdView;
    }

    public void setUnifiedNativeAdView(NativeAdView nativeAdView) {
        this.mUnifiedNativeAdView = nativeAdView;
    }


    public void setAdvertiserView(final TextView view) {
        this.mAdvertiserView = view;
    }
    public TextView getAdvertiserView() {
        return this.mAdvertiserView;
    }
}
