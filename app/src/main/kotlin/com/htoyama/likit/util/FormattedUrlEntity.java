package com.htoyama.likit.util;

import com.twitter.sdk.android.core.models.UrlEntity;

public class FormattedUrlEntity {
    int start;
    int end;
    final String displayUrl;
    final String url;

    FormattedUrlEntity(UrlEntity entity) {
        this.start = entity.getStart();
        this.end = entity.getEnd();
        this.displayUrl = entity.displayUrl;
        this.url = entity.url;
    }
}