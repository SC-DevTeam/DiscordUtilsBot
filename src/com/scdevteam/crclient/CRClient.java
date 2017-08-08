package com.scdevteam.crclient;

import com.scdevteam.DiscordUtils;
import com.scdevteam.SCUtils;
import com.scdevteam.crclient.crypto.ClientCrypto;
import com.scdevteam.crclient.maps.MessageMap;
import com.scdevteam.crclient.models.RequestMessage;
import com.scdevteam.crclient.models.ResponseMessage;
import de.btobastian.javacord.entities.message.Message;
import sun.misc.MessageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

public class CRClient {
    private static CRClient sInstance;

    private static OutputStream sOs;
    private static ClientCrypto sCrypto = new ClientCrypto();
    private static Timer sRoutine;

    private Message sDiscordLocker;

    private static Socket sSocket;

    public static CRClient getInstance() {
        if (sInstance == null) {
            sInstance = new CRClient();
        }

        return sInstance;
    }

    private CRClient() {
    }

    public void start(Message message) {
        sDiscordLocker = message;

        new Thread(this::connect).start();
    }

    private void connect() {
        try {
            sSocket = new Socket();
            sSocket.connect(new InetSocketAddress("game.clashroyaleapp.com", 9339));

            InputStream inputStream = sSocket.getInputStream();
            sOs = sSocket.getOutputStream();

            write(10100, 0, RequestBuilder.buildClientHello());

            while (isConnected()) {
                byte[] header = new byte[7];

                if (inputStream.read(header, 0, 7) > 0) {
                    int msgId = SCUtils.toInt16(Arrays.copyOfRange(header, 0, 2));
                    int len = SCUtils.toInt24(Arrays.copyOfRange(header, 2, 5));
                    int ver = SCUtils.toInt16(Arrays.copyOfRange(header, 5, 7));

                    ResponseMessage responseMessage = new ResponseMessage(msgId, len, ver);

                    StringBuilder dPing = new StringBuilder();
                    ByteBuffer payload = ByteBuffer.allocate(len);

                    int read = 0;
                    int o = len;
                    while (o != 0) {
                        byte[] a = new byte[o];

                        int r;
                        if ((r = inputStream.read(a, 0, o)) == -1) {
                            break;
                        }
                        o -= r;
                        read += r;
                        payload.put(ByteBuffer.wrap(a, 0, r));
                        SCUtils.log("Added chunks for: " +
                                responseMessage.getMessageID() + " l: " + len
                        + " rem: " + o + " read: " + read);
                    }

                    responseMessage.finish(payload, sCrypto);
                    SCUtils.log("Finished: " + responseMessage.getMessageID());

                    if (msgId != 20107 && msgId != 20108) {
                        if (msgId != 20104) {
                            String map = MessageMap.getMap(responseMessage.getMessageID(),
                                    responseMessage.getDecryptedPayload());
                            dPing.append("IN ---> ")
                                    .append(MessageMap.getMessageType(responseMessage.getMessageID()))
                                    .append("\nLENGTH: ")
                                    .append(responseMessage.getPayloadLength());
                            if (map != null) {
                                dPing.append("\n\nMAP:\n")
                                        .append(map);
                            } else {
                                dPing.append("\n\nPAYLOAD:\n")
                                        .append(SCUtils.toHexString(responseMessage.getDecryptedPayload()));
                            }
                        } else {
                            dPing.append("IN ---> ")
                                    .append(MessageMap.getMessageType(responseMessage.getMessageID()))
                                    .append("\nLENGTH: ")
                                    .append(responseMessage.getPayloadLength());
                        }
                        SCUtils.log(SCUtils.toHexString(responseMessage.getDecryptedPayload()));
                    }

                    if (dPing.length() > 0) {
                        String d = dPing.toString();
                        DiscordUtils.sendMessage(d, sDiscordLocker);
                    }

                    handleResponse(responseMessage);
                }
            }

            sSocket = null;
        } catch (IOException e) {
            sSocket = null;
        }
    }

    public void kill() {
        if (isConnected()) {
            try {
                sSocket.close();
                sOs.close();

                sRoutine.cancel();
                sRoutine = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleResponse(ResponseMessage responseMessage) {
        switch (responseMessage.getMessageID()) {
            case 20100:
                try {
                    byte[] loginPayload = RequestBuilder.buildLogin();
                    write(10101, 0, loginPayload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 20104:
                startFiveSecRoutine();
                break;
        }
    }

    public void write(final int messageId, final int version, final byte[] payload) {
        try {
            final RequestMessage requestMessage =
                    new RequestMessage(messageId, payload.length, version, payload, sCrypto);

            sOs.write(requestMessage.buildMessage().array());
            sOs.flush();
            if (messageId != 10107 && messageId != 10108) {
                DiscordUtils.sendMessage("OUT ---> " +
                        MessageMap.getMessageType(messageId) +
                        "\nLENGTH: " + payload.length, sDiscordLocker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFiveSecRoutine() {
        TimerTask scTasks = new TimerTask() {
            @Override
            public void run() {
                byte[] keepAlivePayload = RequestBuilder.buildKeepAlive();
                byte[] clientCapabilitiesPayload = RequestBuilder.buildClientCapabilities();

                write(10108, 0, keepAlivePayload);
                write(10107, 0, clientCapabilitiesPayload);
            }
        };

        sRoutine = new Timer();
        sRoutine.schedule(scTasks, 5000, 5000);
    }

    public boolean isConnected() {
        return sSocket != null && sSocket.isConnected();
    }
}
