/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.os;

public class Trace {
    public static void traceBegin(long tag, String name) {
        android.os.Trace.traceBegin(tag, name);
    }

    public static void traceEnd(long tag) {
        android.os.Trace.traceEnd(tag);
    }
}
