package com.scdevteam;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.exceptions.RateLimitedException;

import java.util.ArrayList;

public class DiscordUtils {
    private static DiscordUtils sInstance;
    private static final ArrayList<M> sMessageQueue = new ArrayList<>();

    public static DiscordUtils getInstance() {
        if (sInstance == null) {
            sInstance = new DiscordUtils();
        }
        return sInstance;
    }

    private DiscordUtils() {
        new Thread(this::cycle).start();
    }

    private void cycle() {
        flushQueue();
    }

    private void flushQueue() {
        if (sMessageQueue.isEmpty()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            flushQueue();
            return;
        }

        final M m = sMessageQueue.get(0);
        m.d.reply("```" + m.m + "```", new FutureCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                sMessageQueue.remove(m);
                flushQueue();
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (throwable instanceof RateLimitedException) {
                    RateLimitedException rateLimitedException = (RateLimitedException) throwable;
                    try {
                        rateLimitedException.waitTillRetry();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    sMessageQueue.remove(0);
                }

                flushQueue();
            }
        });
    }

    public void sendMessage(String m, Message message) {
        if (m.length() >= 1950) {
            int index = 0;
            while (index < m.length()) {
                sMessageQueue.add(new M(m.substring(index,
                        Math.min(index + 1950, m.length())), message));
                index += Math.min(index + 1950, m.length());
            }
        } else {
            sMessageQueue.add(new M(m, message));
        }
    }

    static class M {
        public String m;
        public Message d;
        M(String m, Message d) {
            this.m = m;
            this.d = d;
        }
    }
}
