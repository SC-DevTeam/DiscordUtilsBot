package com.scdevteam;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

public class SCUtils {
    private static final char[] sHexTable = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    private static final String[] sTagChars = {"0", "2", "8", "9", "P", "Y", "L", "Q", "G", "R", "J", "C", "U", "V"};

    public static String tagFromId(BuffParser.SLong sl) {
        long id = (sl.lo << 8) + sl.hi;
        String res = "";
        while (id > 0) {
            int rem = (int) Math.floor(id % sTagChars.length);
            res = sTagChars[rem] + res;
            id -= rem;
            id /= sTagChars.length;
        }
        return res;
    }

    public static BuffParser.SLong idFromTag(String tag) {
        long id = 0;
        String[] t = tag.split("");
        for (int i = 0; i < t.length; i++) {
            String character = t[i];
            int charIndex = Arrays.asList(sTagChars).indexOf(character);

            id *= sTagChars.length;
            id += charIndex;
        }

        int high = (int) (id % 256);
        int low = (int) ((id - high) >>> 8);

        ByteBuffer b = ByteBuffer.allocate(8);
        b.putInt(high);
        b.putInt(low);
        return new BuffParser.SLong(b);
    }

    public static int toInt32(byte[] b) {
        return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static int toInt24(byte[] b) {
        return (b[0] & 0xFF) << 16 | (b[1] & 0xFF) << 8 | (b[2] & 0xFF);
    }


    public static int toInt16(byte[] b) {
        return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static byte[] fromLong(long l) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(l);
        return byteBuffer.array();
    }

    public static byte[] fromInt32(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i);

        return result;
    }

    public static byte[] fromInt24(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 16);
        result[1] = (byte) (i >> 8);
        result[2] = (byte) (i);

        return result;
    }

    public static byte[] fromInt16(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i);

        return result;
    }

    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = sHexTable[v>>>4];
            hexChars[j*2 + 1] = sHexTable[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexToBuffer(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    public static String getRandomHexString(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, length);
    }

    public static int readInt32(byte[] input) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(input);
        return byteBuffer.getInt();
    }

    public static short readShort(byte[] input) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(input);
        return byteBuffer.getShort();
    }

    public static long readLong(byte[] input) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(input);
        return byteBuffer.getLong();
    }

    public static String readString(byte[] input) {
        return new String(input);
    }

    public static RrsInt32 readRrsInt32(ByteBuffer byteBuffer) {
        int c = 0;
        int value = 0;
        int seventh;
        int msb;
        int b;

        if (byteBuffer.remaining() < 5) {
            byte[] s = Arrays.copyOf(byteBuffer.array(), 5);
            byteBuffer = ByteBuffer.wrap(s);
        }

        do {
            b = byteBuffer.get();
            if (c == 0) {
                seventh = (b & 0x40) >> 6; // save 7th bit
                msb = (b & 0x80) >> 7; // save msb
                b = b << 1; // rotate to the left
                b = b & ~(0x181); // clear 8th and 1st bit and 9th if any
                b = b | (msb << 7) | (seventh); // insert msb and 6th back in
            }

            value |= (b & 0x7f) << (7 * c);
            ++c;
        } while ((b & 0x80) != 0);
        value = ((value >>> 1) ^ -(value & 1));
        return new RrsInt32(c, value);
    }

    public static int readVarInt32Length(long value) {
        if (value < 1 << 7) {
            return 1;
        }

        if (value < 1 << 14) {
            return 2;
        }

        if (value < 1 << 21) {
            return 3;
        }

        if (value < 1 << 28) {
            return 4;
        }

        return 5;
    }

    public static class RrsInt32 {
        public int length;
        public long value;
        public RrsInt32(int length, long value) {
            this.length = length;
            this.value = value;
        }
    }

    public static void log(String s) {
        System.out.print(s + "\n");
    }
}
