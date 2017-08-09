package com.scdevteam;

import com.google.common.util.concurrent.FutureCallback;
import com.scdevteam.db.DBController;
import com.scdevteam.discord.MessageHandler;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;

public class Main  {
    private static MessageHandler sHandler = new MessageHandler();

    // Hold the reference
    private static DBController sDBController = DBController.getInstance();

    public static void main(String[] args) {
        DiscordAPI api = Javacord.getApi(Configs.APIKEY, true);

        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                SCUtils.log("CONN");

                api.registerListener(sHandler);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
