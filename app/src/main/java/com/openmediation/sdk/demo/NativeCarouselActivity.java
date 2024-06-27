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
import com.github.islamkhsh.CardSliderIndicator;
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
    private CarouselAdapter myAdapter;
    private List<CarouselAdapter.CarouselAdInfo> mData;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private List<String> mCarouselPlacmentIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_carousel);
        mCarouselPlacmentIds = new ArrayList<String>();//NativeAd.getCachedPlacementIds("carousel");
        if (mCarouselPlacmentIds.isEmpty()) {
            //mCarouselPlacmentIds.add(Constants.P_NATIVE_CAROUSEL_0);
            mCarouselPlacmentIds.add(Constants.P_NATIVE_CAROUSEL_1);
            //mCarouselPlacmentIds.add(Constants.P_NATIVE_CAROUSEL_2);
        }
        for (final String placementId : mCarouselPlacmentIds) {
            NativeAd.addAdListener(placementId, mNativeAdListener);
        }
        initListView();
        Log.d(TAG, "onCreate");
    }

    private void initListView() {
        mViewPager = (CardSliderViewPager) findViewById(R.id.viewPager);
        mViewPager.setAutoSlideTime(10);
        mViewPager.setSliderPageMargin(3);
        mViewPager.setOtherPagesWidth(0);
        mViewPager.setSmallScaleFactor(0.9f);
        mViewPager.setSmallAlphaFactor(0.5f);
        //mViewPager.setMinShadow();
        //mViewPager.setBaseShadow();
        mData = new ArrayList<>();
        myAdapter = new CarouselAdapter(this, mData);
        mViewPager.setAdapter(myAdapter);
        CardSliderIndicator indicator = (CardSliderIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        loadNativeAd();
    }

    private final NativeAdListener mNativeAdListener = new NativeAdListener() {
        @Override
        public void onNativeAdLoaded(String placementId, AdInfo info) {
            Log.d(TAG, "onNativeAdLoaded, placementId: " + placementId + ", AdInfo : " + info);
            loadSuccess(new CarouselAdapter.CarouselAdInfo(placementId, info));
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


    private void loadSuccess(CarouselAdapter.CarouselAdInfo info) {
        if (info != null) {
            mData.add(info);
            Collections.sort(mData, new Comparator<CarouselAdapter.CarouselAdInfo>() {

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
                public int compare(CarouselAdapter.CarouselAdInfo o1, CarouselAdapter.CarouselAdInfo o2) {
                    // TODO Auto-generated method stub
                    return Integer.compare(strToInt(o1.mPlacementId) , strToInt(o2.mPlacementId));
                }
            });
            myAdapter.notifyDataSetChanged();
        }

    }

    private void loadNativeAd() {
        Log.d(TAG, "loadNativeAd");
        for (final String placementId : mCarouselPlacmentIds) {
            // for TikTok and TencentAd in China traffic
            NativeAd.setDisplayParams(placementId, 320, 0);
            NativeAd.loadAd(placementId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (final String placementId : mCarouselPlacmentIds) {
            NativeAd.removeAdListener(placementId, mNativeAdListener);
        }

        for (CarouselAdapter.CarouselAdInfo info : mData) {
            if (info != null) {
                NativeAd.destroy(info.mPlacementId, info.mAdInfo);
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class CarouselAdapter extends CardSliderAdapter<CarouselAdapter.NativeAdViewHolder> {

        private static final int ITEM_VIEW_TYPE_AD = 1;

        private List<CarouselAdInfo> mData;
        private Context mContext;

        public CarouselAdapter(Context context, List<CarouselAdInfo> data) {
            this.mContext = context;
            this.mData = data;
        }

        @NonNull
        @Override
        public CarouselAdapter.NativeAdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            CarouselAdInfo info = mData.get(position);
            holder.setData(info.mPlacementId, info.mAdInfo);
        }

        private static class CarouselAdInfo {
            final public AdInfo mAdInfo;
            final public String mPlacementId;
            public CarouselAdInfo(String placementId, AdInfo adInfo) {
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
                    if (info.getTitle() == null || info.getTitle().isEmpty()) {
                        title.setVisibility(View.INVISIBLE);
                    }

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
