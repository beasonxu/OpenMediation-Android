// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.core;

import android.app.Activity;
import android.util.SparseArray;

import com.openmediation.sdk.mediation.CustomAdParams;
import com.openmediation.sdk.mediation.CustomAdsAdapter;
import com.openmediation.sdk.utils.AdapterUtil;
import com.openmediation.sdk.utils.AdtUtil;
import com.openmediation.sdk.utils.cache.DataCache;

import java.util.HashMap;
import java.util.Map;

public class AdapterRepository {

    private static final String KEY_GDPR_CONSENT = "MetaData_GDPRConsent";
    private static final String KEY_AGE_RESTRICTED = "MetaData_AgeRestricted";
    private static final String KEY_USER_AGE = "MetaData_UserAge";
    private static final String KEY_USER_GENDER = "MetaData_UserGender";
    private static final String KEY_US_PRIVACY_LIMIT = "MetaData_USPrivacyLimit";

    private MetaData mMetaData = new MetaData();
    private Map<String, Object> mPolicySettings = new HashMap<>();

    public static AdapterRepository getInstance() {
        return AdapterRepositoryHolder.mSingleton;
    }

    private static class AdapterRepositoryHolder {
        private static AdapterRepository mSingleton = new AdapterRepository();
    }

    private AdapterRepository() {
    }

    public void onPause(Activity activity) {
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            if (adapter != null) {
                adapter.onPause(activity);
            }
        }
    }

    public void onResume(Activity activity) {
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            if (adapter != null) {
                adapter.onResume(activity);
            }
        }
    }

    public void setCustomParams(CustomAdParams adEvent) {
        if (adEvent == null) {
            return;
        }
        Boolean consent = getGDPRConsent();
        if (consent != null) {
            setConsent(adEvent, consent);
        }

        Boolean restricted = getAgeRestricted();
        if (restricted != null) {
            setAgeRestricted(adEvent, restricted);
        }

        Integer userAge = getUserAge();
        if (userAge != null) {
            setUserAge(adEvent, userAge);
        }

        String gender = getUserGender();
        if (gender != null) {
            setUserGender(adEvent, gender);
        }

        Boolean privacyLimit = getUSPrivacyLimit();
        if (privacyLimit != null) {
            setUSPrivacyLimit(adEvent, privacyLimit);
        }
    }

    public synchronized void setGDPRConsent(boolean consent) {
        mMetaData.setGDPRConsent(consent);
        mPolicySettings.put("consent", consent);
        saveGDPRConsent();
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            setConsent(adapter, consent);
        }
    }

    private void setConsent(CustomAdParams adEvent, boolean consent) {
        if (adEvent != null) {
            adEvent.setGDPRConsent(AdtUtil.getInstance().getApplicationContext(), consent);
        }
    }

    public synchronized void setAgeRestricted(boolean restricted) {
        mMetaData.setAgeRestricted(restricted);
        mPolicySettings.put("restricted", restricted);
        saveAgeRestricted();
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            setAgeRestricted(adapter, restricted);
        }
    }

    private void setAgeRestricted(CustomAdParams adEvent, boolean restricted) {
        if (adEvent != null) {
            adEvent.setAgeRestricted(AdtUtil.getInstance().getApplicationContext(), restricted);
        }
    }

    public synchronized void setUserAge(int age) {
        mMetaData.setUserAge(age);
        mPolicySettings.put("age", age);
        saveUserAge();
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            setUserAge(adapter, age);
        }
    }
    public synchronized void setUserId(String userId) {
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            setUserId(adapter, userId);
        }
    }

    public synchronized void setTestMode(String adaptorName, String deviceId) {
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            if (adapter.getAdapterName().equalsIgnoreCase(adaptorName)) {
                setTestMode(adapter, deviceId);
            }
        }
    }



    private void setUserAge(CustomAdParams adEvent, int age) {
        if (adEvent != null) {
            adEvent.setUserAge(AdtUtil.getInstance().getApplicationContext(), age);
        }
    }

    private void setUserId(CustomAdParams adEvent, String userId) {
        if (adEvent != null) {
            adEvent.setUserId(AdtUtil.getInstance().getApplicationContext(), userId);
        }
    }

    private void setTestMode(CustomAdParams adEvent, String deviceId) {
        if (adEvent != null) {
            adEvent.setTestMode(AdtUtil.getInstance().getApplicationContext(), deviceId);
        }
    }

    public synchronized void setUserGender(String gender) {
        mMetaData.setUserGender(gender);
        mPolicySettings.put("gender", gender);
        saveUserGender();
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            if (adapter != null) {
                adapter.setUserGender(AdtUtil.getInstance().getApplicationContext(), gender);
            }
        }
    }

    private void setUserGender(CustomAdParams adEvent, String gender) {
        if (adEvent != null) {
            adEvent.setUserGender(AdtUtil.getInstance().getApplicationContext(), gender);
        }
    }

    public synchronized void setUSPrivacyLimit(boolean value) {
        mMetaData.setUSPrivacyLimit(value);
        mPolicySettings.put("usPrivacyLimit", value);
        saveUSPrivacyLimit();
        SparseArray<CustomAdsAdapter> adapterMap = AdapterUtil.getAdapterMap();
        int size = adapterMap.size();
        for (int i = 0; i < size; i++) {
            CustomAdsAdapter adapter = adapterMap.valueAt(i);
            setUSPrivacyLimit(adapter, value);
        }
    }

    private void setUSPrivacyLimit(CustomAdParams adEvent, boolean value) {
        if (adEvent != null) {
            adEvent.setUSPrivacyLimit(AdtUtil.getInstance().getApplicationContext(), value);
        }
    }

    public Boolean getGDPRConsent() {
        return mMetaData.getGDPRConsent();
    }

    public Boolean getAgeRestricted() {
        return mMetaData.getAgeRestricted();
    }

    public Integer getUserAge() {
        return mMetaData.getUserAge();
    }

    public String getUserGender() {
        return mMetaData.getUserGender();
    }

    public Boolean getUSPrivacyLimit() {
        return mMetaData.getUSPrivacyLimit();
    }

    public MetaData getMetaData() {
        return mMetaData;
    }

    public Map<String, Object> getPolicySettings() {
        return mPolicySettings;
    }

    private void saveMetaData() {
        saveGDPRConsent();
        saveAgeRestricted();
        saveUserAge();
        saveUserGender();
        saveUSPrivacyLimit();
    }

    private void saveGDPRConsent() {
        if (mMetaData.getGDPRConsent() != null) {
            DataCache.getInstance().set(KEY_GDPR_CONSENT, mMetaData.getGDPRConsent());
        }
    }

    private void saveAgeRestricted() {
        if (mMetaData.getAgeRestricted() != null) {
            DataCache.getInstance().set(KEY_AGE_RESTRICTED, mMetaData.getAgeRestricted());
        }
    }

    private void saveUserAge() {
        if (mMetaData.getUserAge() != null) {
            DataCache.getInstance().set(KEY_USER_AGE, mMetaData.getUserAge());
        }
    }

    private void saveUserGender() {
        if (mMetaData.getUserGender() != null) {
            DataCache.getInstance().set(KEY_USER_GENDER, mMetaData.getUserGender());
        }
    }

    private void saveUSPrivacyLimit() {
        if (mMetaData.getUSPrivacyLimit() != null) {
            DataCache.getInstance().set(KEY_US_PRIVACY_LIMIT, mMetaData.getUSPrivacyLimit());
        }
    }

    public synchronized void syncMetaData() {
        saveMetaData();
        mMetaData.setGDPRConsent(DataCache.getInstance().get(KEY_GDPR_CONSENT, boolean.class));
        mMetaData.setAgeRestricted(DataCache.getInstance().get(KEY_AGE_RESTRICTED, boolean.class));
        mMetaData.setUserAge(DataCache.getInstance().get(KEY_USER_AGE, int.class));
        mMetaData.setUserGender(DataCache.getInstance().get(KEY_USER_GENDER, String.class));
        mMetaData.setUSPrivacyLimit(DataCache.getInstance().get(KEY_US_PRIVACY_LIMIT, boolean.class));
    }

}
