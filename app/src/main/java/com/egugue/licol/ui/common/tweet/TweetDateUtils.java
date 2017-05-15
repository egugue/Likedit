/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.egugue.licol.ui.common.tweet;

import android.content.res.Resources;

import com.egugue.licol.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class TweetDateUtils {
    // Sat Mar 14 02:34:20 +0000 2009
    private static final SimpleDateFormat DATE_TIME_RFC822
            = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat RELATIVE_DATE_FORMAT =
            new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
    private static final long INVALID_DATE = -1;

    private TweetDateUtils() {}

    static long apiTimeToLong(String apiTime) {
        if (apiTime == null) {
            return INVALID_DATE;
        }

        try {
            return DATE_TIME_RFC822.parse(apiTime).getTime();
        } catch (ParseException e) {
            return INVALID_DATE;
        }
    }

    static boolean isValidTimestamp(String timestamp) {
        return TweetDateUtils.apiTimeToLong(timestamp) != TweetDateUtils.INVALID_DATE;
    }

    /**
     * This method is not thread safe. It has been modified from the original to not rely on global
     * time state. If a timestamp is in the future we return it as an absolute date string. Within
     * the same second we return 0s
     *
     * @param res resource
     * @param currentTimeMillis timestamp for offset
     * @param timestamp timestamp
     * @return the relative time string
     */
    public static String getRelativeTimeString(Resources res, long currentTimeMillis, long timestamp) {
        final long diff = currentTimeMillis - timestamp;
        if (diff < 0) {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(
                R.string.relative_date_format_out_year));
            return RELATIVE_DATE_FORMAT.format(new Date(timestamp));
        }

        final Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);

        final Date d = new Date(timestamp);

        if (now.get(Calendar.YEAR) != c.get(Calendar.YEAR)) {
            // Outside of our year
            RELATIVE_DATE_FORMAT.applyPattern(
                res.getString(R.string.relative_date_format_out_year));
            return RELATIVE_DATE_FORMAT.format(d);
        }

        if ( now.get(Calendar.MONTH) == c.get(Calendar.MONTH) &&
            now.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
            //Same day
            RELATIVE_DATE_FORMAT.applyPattern(
                res.getString(R.string.relative_date_format_in_day));
            return "Today, " + RELATIVE_DATE_FORMAT.format(d);
        }

        // Same year
        RELATIVE_DATE_FORMAT.applyPattern(
            res.getString(R.string.relative_date_format_in_year));
        return RELATIVE_DATE_FORMAT.format(d);
    }

}
