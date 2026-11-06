/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.osense;

import com.oplus.osense.eventinfo.EventConfig;
import com.oplus.osense.eventinfo.OsenseEventCallback;

// no osense on lineage, events just never fire
public class OsenseResEventClient {
    private static final OsenseResEventClient INSTANCE = new OsenseResEventClient();

    public static OsenseResEventClient getInstance() {
        return INSTANCE;
    }

    public int registerEventCallback(OsenseEventCallback callback, EventConfig config) {
        return 0;
    }

    public int unregisterEventCallback(OsenseEventCallback callback) {
        return 0;
    }
}
