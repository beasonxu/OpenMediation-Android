package com.openmediation.sdk.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import com.github.islamkhsh.CardSliderViewPager;
import com.openmediation.sdk.demo.utils.Constants;
import com.openmediation.sdk.nativead.AdIconView;
import com.openmediation.sdk.nativead.AdInfo;
import com.openmediation.sdk.nativead.MediaView;
import com.openmediation.sdk.nativead.NativeAd;
import com.openmediation.sdk.nativead.NativeAdListener;
import com.openmediation.sdk.nativead.NativeAdView;
import com.openmediation.sdk.utils.error.Error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NativeCarouselActivity extends Activity {
    private static final String TAG = "NativeCarouselActivity";

    private CardSliderViewPager mViewPager;
    private CarrouselAdapter myAdapter;
    private List<CarrouselAdapter.CarrouselAdInfo> mData;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_carousel);
        NativeAd.addAdListener(Constants.P_NATIVE_CAROUSEL_0, mNativeAdListener);
        NativeAd.addAdListener(Constants.P_NATIVE_CAROUSEL_1, mNativeAdListener);
        NativeAd.addAdListener(Constants.P_NATIVE_CAROUSEL_2, mNativeAdListener);
        initListView();
        Log.d(TAG, "onCreate");
    }

    private void initListView() {
        mViewPager = (CardSliderViewPager) findViewById(R.id.viewPager);
        mData = new ArrayList<>();
        myAdapter = new CarrouselAdapter(this, mData);
        mViewPager.setAdapter(myAdapter);
        loadNativeAd();
    }

    private final NativeAdListener mNativeAdListener = new NativeAdListener() {
        @Override
        public void onNativeAdLoaded(String placementId, AdInfo info) {
            Log.d(TAG, "onNativeAdLoaded, placementId: " + placementId + ", AdInfo : " + info);
            loadSuccess(new CarrouselAdapter.CarrouselAdInfo(placementId, info));
        }

        @Override
        public void onNativeAdLoadFailed(String placementId, Error error) {
            Log.d(TAG, "onNativeAdLoadFailed, placementId: " + placementId + ", error : " + error);
            loadSuccess(null);
        }

        @Override
        public void onNativeAdImpression(String placementId, AdInfo info) {
            Log.d(TAG, "onNativeAdImpression, placementId: " + placementId + ", info : " + info);
        }

        @Override
        public void onNativeAdClicked(String placementId, AdInfo info) {
            Log.d(TAG, "onNativeAdClicked, placementId: " + placementId + ", info : " + info);
        }
    };


    private void loadSuccess(CarrouselAdapter.CarrouselAdInfo info) {
        if (info != null) {
            mData.add(info);
            Collections.sort(mData, new Comparator<CarrouselAdapter.CarrouselAdInfo>() {

                private int strToInt(final String myString) {
                    int ret = 0;
                    try {
                        ret = Integer.parseInt(myString);
                    }
                    catch (NumberFormatException e) {

                    }
                    return ret;
                }
                @Override
                public int compare(CarrouselAdapter.CarrouselAdInfo o1, CarrouselAdapter.CarrouselAdInfo o2) {
                    // TODO Auto-generated method stub
                    return Integer.compare(strToInt(o1.mPlacementId) , strToInt(o1.mPlacementId));
                }
            });
            myAdapter.notifyDataSetChanged();
        }

    }

    private void loadNativeAd() {
        Log.d(TAG, "loadNativeAd");
        // for TikTok and TencentAd in China traffic
        NativeAd.setDisplayParams(Constants.P_NATIVE_CAROUSEL_0, 320, 0);
        NativeAd.loadAd(Constants.P_NATIVE_CAROUSEL_0);
        NativeAd.setDisplayParams(Constants.P_NATIVE_CAROUSEL_1, 320, 0);
        NativeAd.loadAd(Constants.P_NATIVE_CAROUSEL_1);
        NativeAd.setDisplayParams(Constants.P_NATIVE_CAROUSEL_2, 320, 0);
        NativeAd.loadAd(Constants.P_NATIVE_CAROUSEL_2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAd.removeAdListener(Constants.P_NATIVE_CAROUSEL_0, mNativeAdListener);
        NativeAd.removeAdListener(Constants.P_NATIVE_CAROUSEL_1, mNativeAdListener);
        NativeAd.removeAdListener(Constants.P_NATIVE_CAROUSEL_2, mNativeAdListener);
        for (CarrouselAdapter.CarrouselAdInfo info : mData) {
            if (info != null) {
                NativeAd.destroy(info.mPlacementId, info.mAdInfo);
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class CarrouselAdapter extends CardSliderAdapter<CarrouselAdapter.NativeAdViewHolder> {

        private static final int ITEM_VIEW_TYPE_AD = 1;

        private List<CarrouselAdInfo> mData;
        private Context mContext;

        public CarrouselAdapter(Context context, List<CarrouselAdInfo> data) {
            this.mContext = context;
            this.mData = data;
        }

        @NonNull
        @Override
        public CarrouselAdapter.NativeAdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NativeAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.carousel_item_nativead, parent, false));
        }


        private int getColorRandom() {
            int a = Double.valueOf(Math.random() * 255).intValue();
            int r = Double.valueOf(Math.random() * 255).intValue();
            int g = Double.valueOf(Math.random() * 255).intValue();
            int b = Double.valueOf(Math.random() * 255).intValue();
            return Color.argb(a, r, g, b);
        }

        @Override
        public int getItemCount() {
            int count = mData == null ? 0 : mData.size();
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (mData != null) {
                int count = mData.size();
                if (count > 0) {
                    return ITEM_VIEW_TYPE_AD;
                }

            }
            return super.getItemViewType(position);
        }


        @Override
        public void bindVH(@NonNull NativeAdViewHolder holder, int position) {
            CarrouselAdInfo info = mData.get(position);
            holder.setData(info.mPlacementId, info.mAdInfo);
        }

        private static class CarrouselAdInfo {
            final public AdInfo mAdInfo;
            final public String mPlacementId;
            public CarrouselAdInfo(String placementId, AdInfo adInfo) {
                mAdInfo = adInfo;
                mPlacementId = placementId;
            }
        }
        /**
         * NativeAdViewHolder
         */
        private static class NativeAdViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout itemContainView;
            private Context mContext;

            public NativeAdViewHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
                itemContainView = itemView.findViewById(R.id.native_ad_container);
            }

            public void setData(String placementId, AdInfo info) {
                if (info == null) {
                    return;
                }
                itemContainView.removeAllViews();
                if (info.isTemplateRender()) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.addRule(Gravity.CENTER);
                    itemContainView.addView(info.getView(), layoutParams);
                } else {
                    View adView = LayoutInflater.from(mContext).inflate(R.layout.native_ad_layout, null);
                    TextView title = adView.findViewById(R.id.ad_title);
                    title.setText(info.getTitle());
                    TextView desc = adView.findViewById(R.id.ad_desc);
                    desc.setText(info.getDesc());
                    Button btn = adView.findViewById(R.id.ad_btn);
                    btn.setText(info.getCallToActionText());
                    MediaView mediaView = adView.findViewById(R.id.ad_media);
                    NativeAdView nativeAdView = new NativeAdView(mContext);
                    AdIconView adIconView = adView.findViewById(R.id.ad_icon_media);
                    ImageView badgeView = adView.findViewById(R.id.ad_badge);

                    badgeView.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);

                    nativeAdView.addView(adView);
                    nativeAdView.setTitleView(title);
                    nativeAdView.setDescView(desc);
                    nativeAdView.setAdIconView(adIconView);
                    nativeAdView.setCallToActionView(btn);
                    nativeAdView.setMediaView(mediaView);


                    NativeAd.registerNativeAdView(placementId, nativeAdView, info);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    itemContainView.addView(nativeAdView, layoutParams);
                }
            }
        }

    }

}
