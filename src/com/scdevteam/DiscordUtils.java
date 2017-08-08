package com.scdevteam;

import de.btobastian.javacord.entities.message.Message;

import java.util.ArrayList;

public class DiscordUtils {
    public static void sendMessage(String m, Message message) {
        ArrayList<String> q = new ArrayList<>();
        if (m.length() >= 1500) {
            int index = 0;
            while (index < m.length()) {
                q.add(m.substring(index, Math.min(index + 1500, m.length())));
                index += 1500;
            }
        } else {
            q.add(m);
        }

        while (q.size() != 0) {
            message.reply("```" + q.remove(0) + "```");
        }
    }
}
