/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.hardware.camera2;

import com.oplus.wrapper.hardware.camera2.impl.CameraMetadataNative;

public class CaptureResult {
    private final android.hardware.camera2.CaptureResult mResult;

    public CaptureResult(android.hardware.camera2.CaptureResult result) {
        mResult = result;
    }

    public CameraMetadataNative getNativeMetadata() {
        return new CameraMetadataNative(mResult.getNativeCopy());
    }
}
