/*
 * SPDX-FileCopyrightText: 2024-2025 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

#include <aidl/android/hardware/power/BnPower.h>
#include <aidl/vendor/oplus/hardware/touch/IOplusTouch.h>

#include <android-base/logging.h>
#include <android/binder_manager.h>

#include <OplusTouchConstants.h>

using aidl::android::hardware::power::Mode;
using aidl::vendor::oplus::hardware::touch::IOplusTouch;

#ifndef LIBPERFMGR_EXT
// from the qti power hal utils.c, we get linked into it
extern "C" int perf_hint_enable(int hint_id, int duration);
extern "C" void release_request(int lock_handle);

namespace {
// petal deep idle cpu cap, resources live in powerhint.xml
constexpr int kPetalDeepIdleHint = 0x00001350;
int sDeepIdleHandle = -1;
}  // namespace
#endif

#ifdef LIBPERFMGR_EXT
namespace aidl::google::hardware::power::impl::pixel {
#else
namespace aidl::android::hardware::power::impl {
#endif

bool isDeviceSpecificModeSupported(Mode type, bool* _aidl_return) {
    switch (type) {
        case Mode::DOUBLE_TAP_TO_WAKE:
            *_aidl_return = true;
            return true;
#ifndef LIBPERFMGR_EXT
        case Mode::DEVICE_IDLE:
            *_aidl_return = true;
            return true;
#endif
        default:
            return false;
    }
}

bool setDeviceSpecificMode(Mode type, bool enabled) {
    switch (type) {
        case Mode::DOUBLE_TAP_TO_WAKE: {
            std::string tmp;
            int contents = 0;

            const std::string instance = std::string() + IOplusTouch::descriptor + "/default";
            std::shared_ptr<IOplusTouch> oplusTouch = IOplusTouch::fromBinder(
                    ndk::SpAIBinder(AServiceManager_waitForService(instance.c_str())));
            LOG(INFO) << "Power mode: " << toString(type) << " isDoubleTapEnabled: " << enabled;

            oplusTouch->touchReadNodeFile(OplusTouchConstants::DEFAULT_TP_IC_ID,
                                          OplusTouchConstants::DOUBLE_TAP_INDEP_NODE, &tmp);
            contents = std::stoi(tmp, nullptr, 16);

            if (enabled) {
                contents |= OplusTouchConstants::DOUBLE_TAP_GESTURE;
            } else {
                contents &= ~OplusTouchConstants::DOUBLE_TAP_GESTURE;
            }

            int aidl_return = 0;
            oplusTouch->touchWriteNodeFile(OplusTouchConstants::DEFAULT_TP_IC_ID,
                                           OplusTouchConstants::DOUBLE_TAP_ENABLE_NODE, "1",
                                           &aidl_return);
            oplusTouch->touchWriteNodeFile(OplusTouchConstants::DEFAULT_TP_IC_ID,
                                           OplusTouchConstants::DOUBLE_TAP_INDEP_NODE,
                                           std::to_string(contents), &aidl_return);
            return true;
        }
#ifndef LIBPERFMGR_EXT
        // framework only sends this while petal deep restricted idle is on and screen's off
        case Mode::DEVICE_IDLE: {
            if (enabled && sDeepIdleHandle <= 0) {
                sDeepIdleHandle = perf_hint_enable(kPetalDeepIdleHint, 0);
                LOG(INFO) << "petal deep idle cpu cap on, handle " << sDeepIdleHandle;
            } else if (!enabled && sDeepIdleHandle > 0) {
                release_request(sDeepIdleHandle);
                sDeepIdleHandle = -1;
                LOG(INFO) << "petal deep idle cpu cap off";
            }
            return true;
        }
#endif
        default:
            return false;
    }
}

}  // namespace
