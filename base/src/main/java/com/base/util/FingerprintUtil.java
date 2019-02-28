package com.base.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by GaoTing on 2018/3/30.
 * <p>
 * Explain :指纹验证
 */
public class FingerprintUtil {
    private static CancellationSignal cancellationSignal;

    public static void callFingerPrint(Context context, @NonNull final OnCallBackListenr listener) {
        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
        if (!managerCompat.isHardwareDetected()) { //判断设备是否支持
            listener.onSupportFailed();
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {//判断设备是否处于安全保护中
            listener.onInsecurity();
            return;
        }
        if (!managerCompat.hasEnrolledFingerprints()) { //判断设备是否已经注册过指纹
            listener.onEnrollFailed(); //未注册
            return;
        }
        listener.onAuthenticationStart(); //开始指纹识别
        cancellationSignal = new CancellationSignal(); //必须重新实例化，否则cancel 过一次就不能再使用了
        managerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息，比如华为的提示就是：尝试次数过多，请稍后再试。
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                listener.onAuthenticationError(errMsgId, errString);
            }

            // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
            @Override
            public void onAuthenticationFailed() {
                listener.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                listener.onAuthenticationHelp(helpMsgId, helpString);
            }

            // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                listener.onAuthenticationSucceeded(result);
            }
        }, null);

    }

    public interface OnCallBackListenr {
        void onSupportFailed();

        void onInsecurity();

        void onEnrollFailed();

        void onAuthenticationStart();

        void onAuthenticationError(int errMsgId, CharSequence errString);

        void onAuthenticationFailed();

        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);

        void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);
    }

    public static void cancel() {
        if (cancellationSignal != null)
            cancellationSignal.cancel();
    }
}