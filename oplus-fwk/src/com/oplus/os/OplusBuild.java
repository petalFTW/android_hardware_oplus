/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.os;

import android.os.SystemProperties;

public class OplusBuild {
    public static class VERSION {
        // 38 is what the dump this sdk came off reports
        public static final int SDK_VERSION = SystemProperties.getInt("ro.build.version.oplus.api", 38);
    }
}
