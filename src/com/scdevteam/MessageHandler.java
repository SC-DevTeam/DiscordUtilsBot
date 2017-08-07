package com.scdevteam;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.util.HashSet;

public class MessageHandler implements MessageCreateListener {
    private static final HashSet<String> sAdmins = new HashSet<>();

    private static final boolean DEBUG = false;

    public MessageHandler() {
        sAdmins.add("168018245943558144"); // GIO
    }

    @Override
    public void onMessageCreate(DiscordAPI discordAPI, Message message) {
        String content = message.getContent();
        if (content.startsWith("@")) {
            String userId = message.getAuthor().getId();
            if (!DEBUG || sAdmins.contains(userId)) {
                String[] parts = content.split(" ");
                String cmd = parts[0];

                switch (cmd) {
                    case "@help":
                        message.reply("```" +
                                new String(SCUtils.hexToBuffer("2020205f5f5f5f5f205f5f5f5f5f5f5f5f5f5f20202020202020202020205f5f5f5f5f5f202020202020202020202020202020202020200d0a20202f205f5f5f2f2f205f5f5f5f2f205f5f205c5f5f5f205f2020205f2f5f20205f5f2f5f5f20205f5f5f5f205f5f5f5f5f205f5f5f200d0a20205c5f5f205c2f202f2020202f202f202f202f205f205c207c202f202f2f202f202f205f205c2f205f5f20602f205f5f20605f5f205c0d0a205f5f5f2f202f202f5f5f5f2f202f5f2f202f20205f5f2f207c2f202f2f202f202f20205f5f2f202f5f2f202f202f202f202f202f202f0d0a2f5f5f5f5f2f5c5f5f5f5f2f5f5f5f5f5f2f5c5f5f5f2f7c5f5f5f2f2f5f2f20205c5f5f5f2f5c5f5f2c5f2f5f2f202f5f2f202f5f2f"))
                                + "\n\n@tag2id [tag]"
                                + "\n@id2tag [id]"
                                + "\n@parser [hex payload] [optional - offset]\n" +
                                "```"
                        );
                        break;
                    case "@parser":
                        if (parts.length < 2) {
                            message.reply("Usage: @parser [hexpayload] [optional - offset]");
                            return;
                        }

                        byte[] buffer;
                        try {
                             buffer = SCUtils.hexToBuffer(parts[1]);
                        } catch (Exception e) {
                            message.reply("Invalid buffer");
                            return;
                        }

                        int offset;
                        try {
                            offset = Integer.parseInt(parts[2]);
                        } catch (Exception e) {
                            offset = 0;
                        }
                        if (offset >= buffer.length) {
                            message.reply("Invalid offset");
                            return;
                        }
                        BuffParser parser = new BuffParser(buffer, offset);
                        message.reply(parser.toDiscordResponse());
                        break;
                    case "@tag2id":
                        if (parts.length < 2) {
                            message.reply("Usage: @tag2id [tag]");
                            return;
                        }
                        String tag = parts[1];
                        if (tag.startsWith("#")) {
                            tag = tag.substring(1);
                        }
                        message.reply("Long value: " + SCUtils.idFromTag(tag));
                        break;
                    case "@id2tag":
                        if (parts.length < 2) {
                            message.reply("Usage: @id2tag [id]");
                            return;
                        }
                        long id = Long.parseLong(parts[1]);
                        message.reply("Tag: #" + SCUtils.tagFromId(id));
                        break;
                }
            }
        }
    }
}
