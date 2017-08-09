package com.scdevteam.db;

import com.scdevteam.db.models.ChallengeStatusModel;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;

import static org.dizitart.no2.IndexOptions.indexOptions;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

public class DBController {
    private static Nitrite sDB;
    private static ObjectRepository<ChallengeStatusModel> sChallengeModels;

    private static DBController sInstance;

    public static DBController getInstance() {
        if (sInstance == null) {
            sInstance = new DBController();
        }
        return sInstance;
    }

    private DBController() {
        sDB = Nitrite.builder()
                .compressed()
                .filePath("/home/igio90/Projects/SCDiscordUtils/scdevteam.db")
                .openOrCreate("SCDevTeam", "xxxxxxxxx");

        sChallengeModels = sDB.getRepository(ChallengeStatusModel.class);
        try {
            sChallengeModels.createIndex("userId",
                    indexOptions(IndexType.Fulltext, true));
        } catch (Exception ignored) {}
    }

    public ChallengeStatusModel insertNewChallenge(String userId) {
        ChallengeStatusModel challengeModel = new ChallengeStatusModel(userId, 0);
        sChallengeModels.insert(challengeModel);
        return getChallenge(userId);
    }

    public ChallengeStatusModel getChallenge(String userId) {
        Cursor<ChallengeStatusModel> results = sChallengeModels.find(eq("userId", userId));
        return results.firstOrDefault();
    }
}
