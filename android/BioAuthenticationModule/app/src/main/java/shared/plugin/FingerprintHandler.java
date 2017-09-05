package shared.plugin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;

/**
 * FingerprintHandler
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private final int FINGERPRINTRESULT_ERROR = 0;
    private final int FINGERPRINTRESULT_HELP = 1;
    private final int FINGERPRINTRESULT_FAIL = 2;
    private final int FINGERPRINTRESULT_SUCCESS = 3;

    private CancellationSignal cancellationSignal;
    private Context appContext;
    private FingerPrintResultListener mResultListener;

    private final int ERROR = 0;
    private final int HELP = 1;
    private final int FAIL = 2;
    private final int SUCCESS = 3;

    public interface FingerPrintResultListener {
        void onFingerPrintResult(int result, String message);
    }

    public FingerprintHandler(Context context, FingerPrintResultListener mResultListener) {
        appContext = context;
        this.mResultListener = mResultListener;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        mResultListener.onFingerPrintResult(FINGERPRINTRESULT_ERROR , errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        mResultListener.onFingerPrintResult(FINGERPRINTRESULT_HELP, helpString.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        mResultListener.onFingerPrintResult(FINGERPRINTRESULT_FAIL, "");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mResultListener.onFingerPrintResult(FINGERPRINTRESULT_SUCCESS, "");
    }
}