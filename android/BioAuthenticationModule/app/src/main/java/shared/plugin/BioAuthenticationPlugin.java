package shared.plugin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.t5online.nebulacore.plugin.Plugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by JoAmS on 2017. 6. 15..
 */

public class BioAuthenticationPlugin extends Plugin {

    public static final String PLUGIN_GROUP_BIO_AUTHENTICATION = "bioauthentication";


    protected final int FINGERPRINTRESULT_ERROR = 0;
    protected final int FINGERPRINTRESULT_HELP = 1;
    protected final int FINGERPRINTRESULT_FAIL = 2;
    protected final int FINGERPRINTRESULT_SUCCESS = 3;

    private FingerprintHandler helper;

    private KeyStore keyStore;
    private String KEY_NAME = "bio_key";
    private String ANDROID_KEY_STORE = "AndroidKeyStore";
    private Cipher cipher;


    public void isSupported() {
        Log.d("", "isSupported");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = this.bridgeContainer.getActivity().getSystemService(FingerprintManager.class);

            if (ActivityCompat.checkSelfPermission(this.bridgeContainer.getActivity(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // Check Permit
                try {
                    JSONObject ret = new JSONObject();
                    ret.put("code", STATUS_CODE_ERROR);
                    ret.put("message", "permission denied");
                    resolve(ret);
                } catch (JSONException e) {
                    reject();
                }
                return;
            }

            if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                try {
                    JSONObject ret = new JSONObject();
                    ret.put("code", STATUS_CODE_SUCCESS);
                    ret.put("message", "");
                    resolve(ret);
                } catch (JSONException e) {
                    reject();
                }
            } else {
                try {
                    JSONObject ret = new JSONObject();
                    ret.put("code", STATUS_CODE_ERROR);
                    ret.put("message", "unsupported device");
                    resolve(ret);
                } catch (JSONException e) {
                    reject();
                }
            }
        } else {
            try {
                JSONObject ret = new JSONObject();
                ret.put("code", STATUS_CODE_ERROR);
                ret.put("message", "unsupported device");
                resolve(ret);
            } catch (JSONException e) {
                reject();
            }
        }
    }

    public void authentication(String message) {

    }




    private boolean checkUseFingerprint() {
        boolean returnValue = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) bridgeContainer.getActivity().getSystemService(Context.FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                return returnValue;
            } else {

                //Check whether the user has granted your app the USE_FINGERPRINT permission//
                if (ActivityCompat.checkSelfPermission(bridgeContainer.getActivity(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    return returnValue;
                }

                //Check that the user has registered at least one fingerprint//
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    return returnValue;
                } else {

                    returnValue = generatorKey();

                    if (cipherInit()) {
                        // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                        // for starting the authentication process (via the startAuth method) and processing the authentication process events//

                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
//                        FingerprintHandler helper = new FingerprintHandler(this, this);
                        helper = new FingerprintHandler(bridgeContainer.getActivity(), new FingerprintHandler.FingerPrintResultListener() {
                            @Override
                            public void onFingerPrintResult(int result, String message) {
                                switch (result) {
                                    case FINGERPRINTRESULT_ERROR:
                                        // TODO
                                        // 이전 지문인식 요청시 하드웨어에서 5번 실패를하면 바로 error를 넘겨준다.
                                        // 그뒤 일정시간이 지나야 지문인식을 사용할수 있다.
                                        break;
                                    case FINGERPRINTRESULT_HELP:
//                Toast.makeText(this, "HELP", Toast.LENGTH_SHORT).show();
                                        break;
                                    case FINGERPRINTRESULT_FAIL:

//                Toast.makeText(this, "다시 입력해 주세요", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show();
                                        break;
                                    case FINGERPRINTRESULT_SUCCESS:
//                Toast.makeText(this, "FINGERPRINTRESULT_SUCCESS", Toast.LENGTH_SHORT).show();
                                        stopListening();
                                        break;
                                }
                            }
                        });

                        helper.startAuth(fingerprintManager, cryptoObject);

                    } else {
                        returnValue = false;
                    }
                }
            }
        } else {
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * 지문인식 cipherInit
     *
     * @return
     */
    private boolean cipherInit() {
        //If the cipher is initialized successfully, then create a CryptoObject instance//
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e1) {
            //Return false if cipher initialization failed//
            e1.printStackTrace();
            return false;
        }
    }

    /**
     * 지문인식 generatorKey
     *
     * @return
     */
    protected boolean generatorKey() {
        boolean returnValue = false;
        KeyGenerator keyGenerator = null;

        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return returnValue;
        }

        try {
            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return returnValue;
        }

        try {
            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Specify the operation(s) this key can be used for//
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
                //Generate the key//
                keyGenerator.generateKey();
                returnValue = true;
            } else {
                return returnValue;
            }

        } catch (IOException | NoSuchAlgorithmException | CertificateException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return returnValue;
        }
        return returnValue;
    }


    private void stopListening() {
        if (helper != null) {
            helper.stopListening();
        }
    }
}
