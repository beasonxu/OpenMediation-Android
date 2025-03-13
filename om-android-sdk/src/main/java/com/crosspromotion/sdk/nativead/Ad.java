// Copyright 2019 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.crosspromotion.sdk.nativead;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.crosspromotion.sdk.utils.ImageUtils;
import com.openmediation.sdk.utils.IOUtil;

import java.io.File;
import java.io.FileNotFoundException;

public final class Ad {
    private final String mTitle;
    private final String mDescription;
    private final String mCTA;
    private final Bitmap mIcon;
    private File mRawContent;

    private Ad(Builder builder) {
        this.mTitle = builder.mTitle;
        this.mDescription = builder.mDescription;
        this.mCTA = builder.mCTA;
        this.mIcon = builder.mIcon;
        this.mRawContent = builder.mRawContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCTA() {
        return mCTA;
    }

    private Bitmap getBitmapContent() {
        try {
            return ImageUtils.getBitmap(IOUtil.getFileInputStream(mRawContent));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private boolean isGif() {
        try {
            return ImageUtils.isGif(IOUtil.getFileInputStream(mRawContent));
        } catch (FileNotFoundException e) {
            return false;
        }
    }
    private boolean isWebp() {
        try {
            return ImageUtils.isWebp(IOUtil.getFileInputStream(mRawContent));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public void fillImageView(final Context context, final ImageView imageView) {
        if (isGif()) {
            Glide.with(context)
                    .asGif()
                    .load(getRawContent() )
                    .into(imageView);
        }/* else if (isWebp()) {
            Glide.with(context)
                    .as(WebpDrawable.class)
                    .load(getRawContent() )
                    .into(imageView);
        }*/else {
            imageView.setImageBitmap(getBitmapContent());
        }
    }

    public File getRawContent() {
        return mRawContent;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public static class Builder {
        private String mTitle;
        private String mDescription;
        private String mCTA;
        private Bitmap mIcon;

        private File mRawContent;

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder description(String des) {
            this.mDescription = des;
            return this;
        }

        public Builder cta(String cta) {
            this.mCTA = cta;
            return this;
        }


        public Builder rawContent(File raw) {
            this.mRawContent = raw;
            return this;
        }

        public Builder icon(Bitmap icon) {
            this.mIcon = icon;
            return this;
        }

        public Ad build() {
            return new Ad(this);
        }
    }
}
