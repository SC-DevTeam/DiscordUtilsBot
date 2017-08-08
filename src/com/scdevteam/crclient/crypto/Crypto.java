package com.scdevteam.crclient.crypto;

import com.scdevteam.TweetNaCl;
import com.scdevteam.crclient.models.RequestMessage;
import com.scdevteam.crclient.models.ResponseMessage;

public abstract class Crypto {
    protected byte[] privateKey = new byte[TweetNaCl.SIGN_SECRET_KEY_BYTES];
    protected byte[] serverKey;
    protected byte[] clientKey = new byte[TweetNaCl.BOX_PUBLIC_KEY_BYTES];
    protected byte[] sharedKey;
    protected Nonce decryptNonce = new Nonce();
    protected Nonce encryptNonce = new Nonce();
    protected byte[] sessionKey;

    public byte[] encrypt(byte[] message) {
        return encrypt(message, null);
    }

    public byte[] encrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            encryptNonce.increment();
            nonce = encryptNonce;
        }

        return TweetNaCl.crypto_box(message, nonce.getBytes(), sharedKey);
    }

    public byte[] decrypt(byte[] message) {
        return decrypt(message, null);
    }

    public byte[] decrypt(byte[] message, Nonce nonce) {
        if (nonce == null) {
            decryptNonce.increment();
            nonce = decryptNonce;
        }

        return TweetNaCl.crypto_box_open(message, nonce.getBytes(), sharedKey);
    }

    public abstract void decryptPacket(ResponseMessage message);
    public abstract void encryptPacket(RequestMessage message);
}
