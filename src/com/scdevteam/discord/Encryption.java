package com.scdevteam.discord;

import com.scdevteam.SCUtils;
import com.scdevteam.TweetNaCl;
import ove.crypto.digest.Blake2b;

public class Encryption {

    public static String buildNonce(String s1, String s2, String s3) {
        Blake2b.Digest digest = Blake2b.Digest.newInstance(24);
        digest.update(SCUtils.hexToBuffer(s1));
        digest.update(SCUtils.hexToBuffer(s2));
        if (s3 != null) {
            digest.update(SCUtils.hexToBuffer(s3));
        }

        return SCUtils.toHexString(digest.digest());
    }

    public static boolean checkStringLen(String s) {
        return s.length() == 64;
    }

    public static String beforeNm(String s1, String s2) {
        byte[] b = new byte[32];
        TweetNaCl.crypto_box_beforenm(b,
                SCUtils.hexToBuffer(s1), SCUtils.hexToBuffer(s2));
        return SCUtils.toHexString(b);
    }

    public static String encrypt(String what, String nonce, String s1) {
        byte[] p = SCUtils.hexToBuffer(what);
        return SCUtils.toHexString(TweetNaCl.crypto_box(
                p, SCUtils.hexToBuffer(nonce), SCUtils.hexToBuffer(s1)
        ));
    }

    public static String decrypt(String what, String nonce, String s1) {
        byte[] p = SCUtils.hexToBuffer(what);
        return SCUtils.toHexString(TweetNaCl.crypto_box_open(
                p, SCUtils.hexToBuffer(nonce), SCUtils.hexToBuffer(s1)
        ));
    }
}
