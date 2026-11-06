/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.os;

public class SystemProperties {
    public static boolean getBoolean(String key, boolean fallback) {
        return android.os.SystemProperties.getBoolean(key, fallback);
    }

    public static int getInt(String key, int fallback) {
        return android.os.SystemProperties.getInt(key, fallback);
    }
}
