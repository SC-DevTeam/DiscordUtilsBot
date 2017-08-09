package com.scdevteam.discord;

import com.scdevteam.BuffParser;
import com.scdevteam.DiscordUtils;
import com.scdevteam.SCUtils;
import com.scdevteam.challenges.ChallengeManager;
import com.scdevteam.crclient.CRClient;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.HashSet;

public class MessageHandler implements MessageCreateListener {
    private static final HashSet<String> sAdmins = new HashSet<>();

    private static final boolean DEBUG = false;
    private static CRClient sCRClient = CRClient.getInstance();

    public MessageHandler() {
        sAdmins.add("168018245943558144"); // GIO
    }

    @Override
    public void onMessageCreate(DiscordAPI discordAPI, Message message) {
        String content = message.getContent();
        if (content.startsWith("@")) {
            String userId = message.getAuthor().getId();
            SCUtils.log("CMD: " + content);

            if (!DEBUG || sAdmins.contains(userId)) {
                String[] parts = content.split(" ");
                String cmd = parts[0];

                switch (cmd) {
                    case "@help":
                        DiscordUtils.getInstance().sendMessage(
                                new String(SCUtils.hexToBuffer("2020205f5f5f5f5f205f5f5f5f5f5f5f5f5f5f20202020202020202020205f5f5f5f5f5f202020202020202020202020202020202020200d0a20202f205f5f5f2f2f205f5f5f5f2f205f5f205c5f5f5f205f2020205f2f5f20205f5f2f5f5f20205f5f5f5f205f5f5f5f5f205f5f5f200d0a20205c5f5f205c2f202f2020202f202f202f202f205f205c207c202f202f2f202f202f205f205c2f205f5f20602f205f5f20605f5f205c0d0a205f5f5f2f202f202f5f5f5f2f202f5f2f202f20205f5f2f207c2f202f2f202f202f20205f5f2f202f5f2f202f202f202f202f202f202f0d0a2f5f5f5f5f2f5c5f5f5f5f2f5f5f5f5f5f2f5c5f5f5f2f7c5f5f5f2f2f5f2f20205c5f5f5f2f5c5f5f2c5f2f5f2f202f5f2f202f5f2f"))
                                + "\n\nUtils:"
                                + "\n@strlen [str]"
                                + "\n@random [len]"
                                        + "\n@hex2str [str]"
                                        + "\n@str2hex [str]"
                                + "\n@tag2id [tag]"
                                + "\n@id2tag [id]"
                                + "\n@dec2hex [num]"
                                + "\n@hex2dec [num]"
                                + "\n@parser [hex payload] [optional - offset]\n"
                                        + "\nChallenge:"
                                        + "\n@challenge\n"
                                        + "\nInfo:"
                                        + "\n@repo"
                                        + "\n@info\n"
                                        + "\n== ADMINS ONLY ==\n"
                                + "\nEncryption:"
                                + "\n@nonce [s1] [s2] [s3 - optional]"
                                + "\n@beforenm [s1] [s2]"
                                + "\n@encrypt [hex payload] [nonce] [s1]"
                                + "\n@decrypt [hex payload] [nonce] [s1]\n"
                                + "\nClients\n"
                                + "\n@status"
                                + "\n@upcr"
                                + "\n@killcr\n"
                                + "\nRE Utils:"
                                + "\n@notes"
                                + "\n@addnote"
                                + "\n@delnote"
                                , message
                        );
                        break;
                    case "@repo":
                        DiscordUtils.getInstance().sendMessage("https://github.com/SC-DevTeam/DiscordUtilsBot", message);
                        break;
                    case "@info":
                        DiscordUtils.getInstance().sendMessage(SCUtils.toHexString("SCDevTeam BOT by @iGio90".getBytes()), message);
                        break;
                    case "@parser":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @parser [hexpayload] [optional - offset]", message);
                            return;
                        }

                        byte[] buffer;
                        try {
                             buffer = SCUtils.hexToBuffer(parts[1]);
                        } catch (Exception e) {
                            DiscordUtils.getInstance().sendMessage("Invalid buffer", message);
                            return;
                        }

                        int offset;
                        try {
                            offset = Integer.parseInt(parts[2]);
                        } catch (Exception e) {
                            offset = 0;
                        }
                        if (offset >= buffer.length) {
                            DiscordUtils.getInstance().sendMessage("Invalid offset", message);
                            return;
                        }
                        BuffParser parser = new BuffParser(buffer, offset);
                        DiscordUtils.getInstance().sendMessage(
                                parser.toDiscordResponse(),
                                message);
                        break;
                    case "@strlen":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @strlen [str]", message);
                            break;
                        }
                        DiscordUtils.getInstance().sendMessage(parts[1].length() + "", message);
                        break;
                    case "@random":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @random [len]", message);
                            break;
                        }

                        try {
                            int l = Integer.parseInt(parts[1]);
                            if (l > 1000) {
                                DiscordUtils.getInstance().sendMessage("Don't play with me", message);
                                return;
                            }
                            SecureRandom secureRandom = new SecureRandom();
                            byte[] bytes = new byte[l];
                            secureRandom.nextBytes(bytes);
                            DiscordUtils.getInstance().sendMessage(SCUtils.toHexString(bytes), message);
                        } catch (Exception e) {
                            DiscordUtils.getInstance().sendMessage("Don't play with me", message);
                        }
                        break;
                    case "@hex2dec":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @hex2dec [hex]", message);
                            break;
                        }

                        try {
                            String h = parts[1];
                            byte[] b = SCUtils.hexToBuffer(h);
                            DiscordUtils.getInstance().sendMessage(new BigInteger(b).longValue() + "", message);
                        } catch (Exception e) {
                            DiscordUtils.getInstance().sendMessage("Don't play with me", message);
                        }
                        break;
                    case "@dec2hex":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @dec2hex [num]", message);
                            break;
                        }

                        try {
                            int h = Integer.parseInt(parts[1]);
                            byte[] b = SCUtils.fromInt32(h);
                            DiscordUtils.getInstance().sendMessage( SCUtils.toHexString(b), message);
                        } catch (Exception e) {
                            DiscordUtils.getInstance().sendMessage("Don't play with me", message);
                        }
                        break;
                    case "@str2hex":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @str2hex [str]", message);
                            break;
                        }
                        DiscordUtils.getInstance().sendMessage(SCUtils.toHexString(parts[1].getBytes()), message);
                        break;
                    case "@hex2str":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @hex2str [str]", message);
                            break;
                        }
                        DiscordUtils.getInstance().sendMessage(new String(SCUtils.hexToBuffer(parts[1])), message);
                        break;
                    case "@tag2id":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @tag2id [tag]", message);
                            return;
                        }
                        String tag = parts[1];
                        if (tag.startsWith("#")) {
                            tag = tag.substring(1);
                        }
                        tag = tag.toUpperCase();
                        BuffParser.SLong sLong = SCUtils.idFromTag(tag);
                        DiscordUtils.getInstance().sendMessage("Hi: " + sLong.hi + " Lo: " + sLong.lo + " Val: " + sLong.v, message);
                        break;
                    case "@id2tag":
                        if (parts.length < 2) {
                            DiscordUtils.getInstance().sendMessage("Usage: @id2tag [id]", message);
                            return;
                        }
                        long l;
                        try {
                             l = Long.parseLong(parts[1]);
                        } catch (Exception e) {
                            DiscordUtils.getInstance().sendMessage("Insert a valid long", message);
                            return;
                        }
                        BuffParser.SLong id = new BuffParser.SLong(
                                ByteBuffer.wrap(SCUtils.fromLong(l)));
                        DiscordUtils.getInstance().sendMessage("Tag: #" + SCUtils.tagFromId(id), message);
                        break;
                }

                if (sAdmins.contains(userId)) {
                    switch (cmd) {
                        case "@challenge":
                            ChallengeManager.getsInstance().handleMessage(message);
                            break;
                        case "@status":
                            DiscordUtils.getInstance().sendMessage("CR Client: " + (!sCRClient.isConnected() ? "Offline" : "Online"), message);
                            break;
                        case "@upcr":
                        case "@startcr":
                            if (!sCRClient.isConnected()) {
                                sCRClient.start(message);
                            }
                            break;
                        case "@killcr":
                            if (sCRClient != null && sCRClient.isConnected()) {
                                sCRClient.kill();
                                DiscordUtils.getInstance().sendMessage("CR Client killed", message);
                            }
                            break;
                        case "@nonce":
                            if (parts.length < 3) {
                                DiscordUtils.getInstance().sendMessage("Usage: @nonce [s1] [s2] [s3 - optional]", message);
                                return;
                            }

                            if (!Encryption.checkStringLen(parts[1]) ||
                                    !Encryption.checkStringLen(parts[2]) ||
                                    (parts.length > 3 && !Encryption.checkStringLen(parts[3]))) {
                                DiscordUtils.getInstance().sendMessage("Keys must be 32 bytes", message);
                                return;
                            }

                            String o = parts.length > 3 ? parts[3] : null;
                            DiscordUtils.getInstance().sendMessage(Encryption.buildNonce(parts[1], parts[2], o), message);
                            break;
                        case "@beforenm":
                            if (parts.length < 3) {
                                DiscordUtils.getInstance().sendMessage("Usage: @beforenm [s1] [s2]", message);
                                return;
                            }

                            DiscordUtils.getInstance().sendMessage(Encryption.beforeNm(parts[1], parts[2]), message);
                            break;
                        case "@encrypt":
                            if (parts.length < 3) {
                                DiscordUtils.getInstance().sendMessage("Usage: @encrypt [hex payload] [nonce] [s1]", message);
                                return;
                            }

                            DiscordUtils.getInstance().sendMessage(Encryption.encrypt(parts[1], parts[2], parts[3]), message);
                            break;
                        case "@decrypt":
                            if (parts.length < 3) {
                                DiscordUtils.getInstance().sendMessage("Usage: @decrypt [hex payload] [nonce] [s1]", message);
                                return;
                            }

                            DiscordUtils.getInstance().sendMessage(Encryption.decrypt(parts[1], parts[2], parts[3]), message);
                            break;
                    }
                }
            }
        }
    }
}
