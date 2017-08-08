package com.scdevteam.crclient.models;

import com.scdevteam.SCUtils;
import com.scdevteam.crclient.crypto.ClientCrypto;
import com.scdevteam.crclient.maps.MessageMap;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ResponseMessage extends Message {
    private final ByteBuffer mChunks;

    public ResponseMessage(int msgId, int len, int ver) {
        super(false);
        mMessageID = msgId;
        mPayloadLength = len;
        mVersion = ver;

        mChunks = ByteBuffer.allocate(len);
    }

    public void finish(ByteBuffer payload, ClientCrypto clientCrypto) {
        mEncrypted = payload.array();
        clientCrypto.decryptPacket(this);
    }

    public String toString() {
        return "ID: " + MessageMap.getMessageType(mMessageID) +
                "\nLength: " + mPayloadLength +
                "\nVersion: " + mVersion +
                "\nDecrypted: " + SCUtils.toHexString(mDecrypted);
    }
}
