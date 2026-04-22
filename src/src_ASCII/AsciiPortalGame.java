package src_ASCII;

import Game.GameGeneric;
import Game.GameWriteable;

public class AsciiPortalGame extends GameGeneric implements GameWriteable {

    private String score = "0";

    @Override
    public String getGameName() {
        return "ASCII Scroll Art";
    }

    @Override
    public void play() {
        try {
            scrollart.playForPortal();
            score = scrollart.lastScore;
        } catch (InterruptedException e) {
            System.out.println("ASCII game was interrupted.");
            score = "0";
        }
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