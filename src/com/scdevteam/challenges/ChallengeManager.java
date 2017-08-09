package com.scdevteam.challenges;

import com.scdevteam.DiscordUtils;
import com.scdevteam.db.DBController;
import com.scdevteam.db.models.ChallengeStatusModel;
import de.btobastian.javacord.entities.message.Message;

public class ChallengeManager {
    private static ChallengeManager sInstance;

    public static ChallengeManager getsInstance() {
        if (sInstance == null) {
            sInstance = new ChallengeManager();
        }
        return sInstance;
    }

    private DBController mDBController;

    private ChallengeManager() {
        mDBController = DBController.getInstance();
    }

    public void handleMessage(Message message) {
        String userId = message.getAuthor().getId();
        ChallengeStatusModel challengeStatusModel = mDBController.getChallenge(userId);

        if (challengeStatusModel == null) {
            challengeStatusModel = mDBController.insertNewChallenge(userId);
        }

        if (challengeStatusModel != null) {
            DiscordUtils.getInstance().sendMessage("Welcome: @" + message.getAuthor().getName() + ".", message);
            int level = challengeStatusModel.getScore();

            switch (level) {

            }
        }
    }
}
