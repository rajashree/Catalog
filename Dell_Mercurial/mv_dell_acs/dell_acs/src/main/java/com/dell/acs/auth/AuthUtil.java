package com.dell.acs.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 7/6/12
 * Time: 12:55 PM
 *
 */
public class AuthUtil {

    private static final Logger log = Logger.getLogger(AuthUtil.class);
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     *
     * @param data
     * @param secretKey
     * @return
     */
    public static String generateHMAC(final String data, final String secretKey) {
        // get an hmac_sha1 key from the raw key bytes
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);

        // get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        // compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(data.getBytes());
        // base64-encode the hmac
        return new String(Base64.encodeBase64(rawHmac));

    }
}
