package com.scdevteam;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;

public class Main  {
    private static MessageHandler sHandler = new MessageHandler();

    public static void main(String[] args) {
        DiscordAPI api = Javacord.getApi(Configs.APIKEY, true);
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
