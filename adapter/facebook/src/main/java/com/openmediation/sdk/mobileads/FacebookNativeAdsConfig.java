/*
 * // Copyright 2021 ADTIMING TECHNOLOGY COMPANY LIMITED
 * // Licensed under the GNU Lesser General Public License Version 3
 */

package com.openmediation.sdk.mobileads;

import android.widget.TextView;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeBannerAd;

public class FacebookNativeAdsConfig {
    private NativeAdBase nativeAd;
    private AdOptionsView adOptionsView;
    private MediaView mediaView;
    private MediaView iconView;

    private TextView sponsoredLabelView;

    public NativeAdBase getNativeAd() {
        return nativeAd;
    }

    public boolean isBannerStyle() {
        if (nativeAd != null && nativeAd instanceof NativeBannerAd) {
            return true;
        }
        return false;
    }
    public void setNativeAd(NativeAdBase nativeAd) {
        this.nativeAd = nativeAd;
    }

    public AdOptionsView getAdOptionsView() {
        return adOptionsView;
    }

    public void setAdOptionsView(AdOptionsView adOptionsView) {
        this.adOptionsView = adOptionsView;
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    public void setMediaView(MediaView mediaView) {
        this.mediaView = mediaView;
    }

    public MediaView getIconView() {
        return iconView;
    }

    public void setIconView(MediaView iconView) {
        this.iconView = iconView;
    }

    public void setSponsoredLabelView(TextView textView){ this.sponsoredLabelView = textView; }

    public TextView getSponsoredLabelView() {return this.sponsoredLabelView;}


    public String getSponsoredText() {return this.nativeAd.getSponsoredTranslation();}
}
