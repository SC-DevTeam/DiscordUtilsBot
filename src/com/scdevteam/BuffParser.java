package com.scdevteam;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BuffParser {

    private final byte[] mPayload;

    public BuffParser(byte[] payload, int offset) {
        mPayload = Arrays.copyOfRange(payload, offset, payload.length);
    }

    public String toDiscordResponse() {
        StringBuilder res = new StringBuilder();
        parseLong(res);
        parseInt(res);
        parseBoolean(res);
        parseString(res);
        parseRrsInt(res);
        return res.toString();
    }

    private void parseLong(StringBuilder res) {
        res.append("LONG:");
        res.append("\n");
        try {
            byte[] z = new byte[8];
            res.append("hex: ");
            ByteBuffer p = ByteBuffer.wrap(mPayload);
            p.get(z, 0, 8);
            res.append(SCUtils.toHexString(z));
            p = ByteBuffer.wrap(z);
            res.append("\n");
            res.append("value: ");
            res.append(p.getLong());
        } catch (Exception ignored) {
            res.append("0");
        }
        res.append("\n\n");
    }

    private void parseInt(StringBuilder res) {
        res.append("INT 32:");
        res.append("\n");
        try {
            byte[] z = new byte[4];
            res.append("hex: ");
            ByteBuffer p = ByteBuffer.wrap(mPayload);
            p.get(z, 0, 4);
            res.append(SCUtils.toHexString(z));
            p = ByteBuffer.wrap(z);
            res.append("\n");
            res.append("value: ");
            res.append(p.getInt());
        } catch (Exception ignored) {
            res.append("0");
        }
        res.append("\n\n");
    }

    private void parseBoolean(StringBuilder res) {
        res.append("BOOLEAN:");
        res.append("\n");
        try {
            byte[] z = new byte[1];
            res.append("hex: ");
            ByteBuffer p = ByteBuffer.wrap(mPayload);
            p.get(z, 0, 1);
            res.append(SCUtils.toHexString(z));
            p = ByteBuffer.wrap(z);
            res.append("\n");
            res.append("value: ");
            byte bool = p.get();
            res.append(bool > (byte) 1 ? "-" : bool == (byte) 1);
        } catch (Exception ignored) {
            res.append("0");
        }
        res.append("\n\n");
    }

    private void parseString(StringBuilder res) {
        res.append("STRING:");
        res.append("\n");
        try {
            ByteBuffer p = ByteBuffer.wrap(mPayload);
            int len = p.getInt();
            res.append("len: ");
            res.append(len);
            res.append("\n");

            if (len > p.remaining()) {
                res.append("value: -");
            } else {
                byte[] z = new byte[len];
                p.get(z, 0, len);
                res.append("value: ");
                res.append(new String(z));
            }
        } catch (Exception ignored) {
            res.append("-");
        }
        res.append("\n\n");
    }

    private void parseRrsInt(StringBuilder res) {
        res.append("RRSINT32:");
        res.append("\n");
        try {
            byte[] z = new byte[1];
            res.append("hex: ");
            ByteBuffer p = ByteBuffer.wrap(mPayload);
            p.get(z, 0, 1);
            res.append(SCUtils.toHexString(z));
            p = ByteBuffer.wrap(z);
            SCUtils.RrsInt32 rrsInt32 = SCUtils.readRrsInt32(p.get());
            res.append("\n");
            res.append("value: ");
            res.append(rrsInt32.value);
        } catch (Exception ignored) {
            res.append("-");
        }
        res.append("\n\n");
    }
}
