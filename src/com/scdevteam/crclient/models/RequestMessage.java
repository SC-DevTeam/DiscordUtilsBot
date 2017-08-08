package com.scdevteam.crclient.models;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.scdevteam.SCUtils;
import com.scdevteam.crclient.crypto.ClientCrypto;

public class RequestMessage extends Message {
    public RequestMessage(int messageId, int payloadLength,
                          int version, byte[] payload, ClientCrypto encrypter) throws IOException {
        super(true);

        mMessageID = messageId;
        mPayloadLength = payloadLength;
        mVersion = version;

        mDecrypted = payload;
        encrypter.encryptPacket(this);
    }

    public ByteBuffer buildMessage() {
        byte[] out = new byte[7 + mEncrypted.length];
        System.arraycopy(SCUtils.fromInt16(mMessageID), 0, out, 0, 2);
        System.arraycopy(SCUtils.fromInt24(mEncrypted.length), 0, out, 2, 3);
        System.arraycopy(SCUtils.fromInt16(mVersion), 0, out, 5, 2);
        System.arraycopy(mEncrypted, 0, out, 7, mEncrypted.length);
        return ByteBuffer.wrap(out);
    }
}
