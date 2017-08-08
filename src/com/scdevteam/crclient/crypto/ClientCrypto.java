package com.scdevteam.crclient.crypto;

import com.scdevteam.SCUtils;
import com.scdevteam.TweetNaCl;
import com.scdevteam.crclient.maps.MessageMap;
import com.scdevteam.crclient.models.RequestMessage;
import com.scdevteam.crclient.models.ResponseMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ClientCrypto extends Crypto {
    public ClientCrypto() {
        super();

        TweetNaCl.crypto_box_keypair(clientKey, privateKey, false);
        serverKey = SCUtils.hexToBuffer("ac30dcbea27e213407519bc05be8e9d930e63f873858479946c144895fa3a26b");

        sharedKey = new byte[32];
        TweetNaCl.crypto_box_beforenm(sharedKey, serverKey, privateKey);
        encryptNonce = new Nonce();
    }

    @Override
    public void decryptPacket(ResponseMessage message) {
        switch (message.getMessageID()) {
            case MessageMap.SERVER_HELLO:
            case MessageMap.LOGIN_FAILED:
                int len = SCUtils.toInt32(Arrays.copyOfRange(message.getEncryptedPayload(), 0, 4));
                sessionKey = Arrays.copyOfRange(message.getEncryptedPayload(),
                        4, 4 + len);
                message.setDecryptedPayload(message.getEncryptedPayload());
                break;
            case MessageMap.LOGIN_OK:
                Nonce nonce = new Nonce(clientKey, serverKey, encryptNonce.getBytes());
                message.setDecryptedPayload(decrypt(message.getEncryptedPayload(), nonce));

                if (message.getDecryptedPayload() != null) {
                    decryptNonce = new Nonce(Arrays.copyOfRange(message.getDecryptedPayload(),
                            0, 24));
                    sharedKey = Arrays.copyOfRange(message.getDecryptedPayload(),
                            24, 56);
                    message.setDecryptedPayload(Arrays.copyOfRange(message.getDecryptedPayload(),
                            56, message.getDecryptedPayload().length));
                }
                break;
            default:
                message.setDecryptedPayload(decrypt(message.getEncryptedPayload()));
        }
    }

    @Override
    public void encryptPacket(RequestMessage message) {
        switch (message.getMessageID()) {
            case MessageMap.CLIENT_HELLO:
                message.setEncryptedPayload(message.getDecryptedPayload());
                break;
            case MessageMap.LOGIN:
                Nonce nonce = new Nonce(clientKey, serverKey);
                ByteArrayOutputStream toEncrypt = new ByteArrayOutputStream();

                try {
                    toEncrypt.write(sessionKey);
                    System.out.print(SCUtils.toHexString(serverKey) + "\n");
                    toEncrypt.write(encryptNonce.getBytes());
                    System.out.print(SCUtils.toHexString(encryptNonce.getBytes()) + "\n");
                    toEncrypt.write(message.getDecryptedPayload());
                } catch (IOException ignored) {}

                ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
                try {
                    encrypted.write(clientKey);
                    encrypted.write(encrypt(toEncrypt.toByteArray(), nonce));
                } catch (IOException ignored) {}

                message.setEncryptedPayload(encrypted.toByteArray());
                break;
            default:
                message.setEncryptedPayload(encrypt(message.getDecryptedPayload()));
        }
    }
}
