/*
 * SPDX-FileCopyrightText: 2026 petalOS
 * SPDX-License-Identifier: Apache-2.0
 */

package android.hardware.camera2;

import android.content.Context;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// what the camera unit sdk expects off oplus-framework, metadata bits copied off the dump
public class OplusCameraManager {
    private static final String TAG = "OplusCameraManager";
    private static final OplusCameraManager INSTANCE = new OplusCameraManager();

    public static OplusCameraManager getInstance() {
        return INSTANCE;
    }

    // our camera gets in, nobody else
    public boolean isAuthedClient(Context context) {
        return context != null && "org.petalos.cam".equals(context.getPackageName());
    }

    public void preOpenCamera(Context context) {}

    public void setCallInfo() {}

    public void setDeathRecipient(IBinder binder) {}

    public Object getEmptyCameraMetadataNative(long[] pointer) {
        CameraMetadataNative metadata = new CameraMetadataNative();
        if (pointer != null && pointer.length > 0) {
            pointer[0] = metadata.getMetadataPtr();
        }
        return metadata;
    }

    public TotalCaptureResult generateTotalCaptureResult(Object metadata, long frameNumber) {
        if (!(metadata instanceof CameraMetadataNative)) {
            return null;
        }
        TotalCaptureResult result = new TotalCaptureResult((CameraMetadataNative) metadata, 0);
        try {
            // final and private, fuck it
            Field field = CaptureResult.class.getDeclaredField("mFrameNumber");
            field.setAccessible(true);
            field.setLong(result, frameNumber);
        } catch (ReflectiveOperationException e) {
            Log.w(TAG, "no frame number for this one", e);
        }
        return result;
    }

    public int getMetadataTag(CaptureResult.Key<?> key) {
        return key == null ? 0 : key.getNativeKey().getTag();
    }

    public Object metaDataValueConvert(CaptureResult.Key<?> key, int type, byte[] value) {
        if (key == null || value == null) {
            return null;
        }
        return MarshalRegistry.getMarshaler(key.getNativeKey().getTypeReference(), type)
                .unmarshal(ByteBuffer.wrap(value).order(ByteOrder.nativeOrder()));
    }
}
