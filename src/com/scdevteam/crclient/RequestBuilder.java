package com.scdevteam.crclient;

import com.scdevteam.Configs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RequestBuilder {
    static final int PROTOCOL = 1;
    static final int KEY_VERSION = 11;
    static final int MAJOR_VERSION = 3;
    static final int MINOR_VERSION = 0;
    static final int BUILD = 377;

    static int UNKNOWN_1 = 40;

    static final String CONTENT_HASH = "622384571aafa79a8453424fb4907c5f1e4268ce";

    public static byte[] buildClientHello() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteBuffer temp = ByteBuffer.allocate(4);
        temp.putInt(PROTOCOL);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(KEY_VERSION);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(MAJOR_VERSION);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(MINOR_VERSION);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(BUILD);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(UNKNOWN_1);
        outputStream.write(temp.array());

        outputStream.write(CONTENT_HASH.getBytes());

        temp = ByteBuffer.allocate(4);
        temp.putInt(2);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(2);
        outputStream.write(temp.array());
        return outputStream.toByteArray();
    }

    public static byte[] buildLogin() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteBuffer temp = ByteBuffer.allocate(8);
        temp.putInt(Configs.ID_HIGH);
        temp.putInt(4, Configs.ID_LOW);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(UNKNOWN_1);
        outputStream.write(temp.array());

        outputStream.write(Configs.PASS_TOKEN.getBytes("UTF8"));

        temp = ByteBuffer.allocate(4);
        temp.putInt(MAJOR_VERSION);
        outputStream.write(temp.array());

        temp = ByteBuffer.allocate(4);
        temp.putInt(MINOR_VERSION);
        outputStream.write(temp.array());

        outputStream.write(CONTENT_HASH.getBytes("UTF8"));
        outputStream.write("".getBytes());
        outputStream.write(Configs.OPEN_UDID.getBytes("UTF8"));
        outputStream.write("".getBytes());
        outputStream.write(Configs.DEVICE.getBytes("UTF8"));
        outputStream.write(Configs.ADVERTISING_ID.getBytes("UTF8"));
        outputStream.write(Configs.OS.getBytes("UTF8"));

        byte trueByte = 1;
        outputStream.write(trueByte);

        outputStream.write("".getBytes());
        outputStream.write(Configs.OPEN_UDID.getBytes());
        outputStream.write(Configs.LANG.getBytes());
        outputStream.write("".getBytes());
        outputStream.write(trueByte);
        outputStream.write("".getBytes());
        outputStream.write(ByteBuffer.allocate(8).array());
        outputStream.write("".getBytes());
        outputStream.write("".getBytes());
        outputStream.write("".getBytes());
        outputStream.write("".getBytes());
        outputStream.write(0);

        return outputStream.toByteArray();
    }

    public static byte[] buildTournamentSearch(String query) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.putInt(query.length());
            outputStream.write(byteBuffer.array());
            outputStream.write(query.getBytes());
            byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort((short) 32639);
            outputStream.write(byteBuffer.array());
            byteBuffer = ByteBuffer.allocate(4);
            outputStream.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static byte[] buildKeepAlive() {
        return new byte[0];
    }

    public static byte[] buildClientCapabilities() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteBuffer pingBuf = ByteBuffer.allocate(4);
        pingBuf.putInt((int) Math.floor(Math.random() * (350 - 250) + 250));

        try {
            outputStream.write(pingBuf.array());
            outputStream.write("wlan0".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private static void writeToBuf(ByteArrayOutputStream outputStream, ByteBuffer buf) {
        try {
            outputStream.write(buf.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToBuf(ByteArrayOutputStream outputStream, byte b) {
        outputStream.write(b);
    }
}
