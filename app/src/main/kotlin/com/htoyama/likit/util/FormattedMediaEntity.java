package com.htoyama.likit.util;

import com.twitter.sdk.android.core.models.MediaEntity;

class FormattedMediaEntity extends FormattedUrlEntity {
    final String type;
    final String mediaUrlHttps;

    FormattedMediaEntity(MediaEntity entity) {
        super(entity);
        this.type = entity.type;
        this.mediaUrlHttps = entity.mediaUrlHttps;
    }
}