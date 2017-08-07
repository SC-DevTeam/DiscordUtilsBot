package com.scdevteam;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;

public class Main  {

    private static final String TOKEN = "MzQ0MDUxMzc0NTc0NjY1NzI4.DGnGUw.jO2ABjBR-d9Xam03ctLZIssaf8c";

    private static MessageHandler sHandler = new MessageHandler();

    public static void main(String[] args) {
        DiscordAPI api = Javacord.getApi(TOKEN, true);
        api.setWaitForServersOnStartup(true);

        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                System.out.print("CONN");

                api.registerListener(sHandler);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
