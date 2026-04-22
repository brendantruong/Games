package src_cards;

import Game.GameGeneric;
import Game.GameWriteable;
import processing.core.PApplet;

public class SnapsGame extends GameGeneric implements GameWriteable {

    private String score = "0";

    @Override
    public String getGameName() {
        return "Cards";
    }

    @Override
    public void play() {

        App.portalMode = true;
        App.gameFinished = false;
        App.finalScore = "0";

        App app = new App();
        PApplet.runSketch(new String[]{"App"}, app);

        while (!App.gameFinished) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
        }

        score = App.finalScore;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public boolean isHighScore(String score, String currentHighScore) {

        if (currentHighScore == null) return true;

        if (score.equals("WIN") && !currentHighScore.equals("WIN")) return true;

        return false;
    }
}
