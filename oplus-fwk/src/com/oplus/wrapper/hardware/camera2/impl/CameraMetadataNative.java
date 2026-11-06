/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.oplus.wrapper.hardware.camera2.impl;

public class CameraMetadataNative {
    // hang on to it or the native side gets finalized under the sdk's feet
    private final android.hardware.camera2.impl.CameraMetadataNative mMetadata;

    public CameraMetadataNative(android.hardware.camera2.impl.CameraMetadataNative metadata) {
        mMetadata = metadata;
    }

    public long getMetadataPtr() {
        return mMetadata.getMetadataPtr();
    }
}
