package com.scdevteam.db.models;

import java.io.Serializable;

public class ChallengeStatusModel implements Serializable {
    private String userId;
    private int score;

    public ChallengeStatusModel() {
    }


    public ChallengeStatusModel(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserID() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
