package src_BFQ;

import Game.GameGeneric;
import Game.GameWriteable;

public class BuzzFeedQuizGame extends GameGeneric implements GameWriteable {

    private String score = "0";

    @Override
    public String getGameName() {
        return "BuzzFeed Quiz";
    }

    @Override
    public void play() {
        Quiz.main(new String[0]);
        score = Quiz.lastScore;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public boolean isHighScore(String score, String currentHighScore) {
        if (currentHighScore == null) return true;
        return Integer.parseInt(score) > Integer.parseInt(currentHighScore);
    }
}
