import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Game.Game;
import Game.ErrorCheck;
import src_BFQ.BuzzFeedQuizGame;

public class GamePortal {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Game> games = new ArrayList<Game>();

    public static void main(String[] args) {
        HashMap<String, Integer> gameCounts = new HashMap<String, Integer>();
        File f = new File("Highscore.csv");

        while (true) {
            loadGames();

            System.out.println("Which game would you like to play?");
            printGameChoices();
            Game g = getGameChoice();
            System.out.println("You're playing " + g.getGameName());

            g.play();
            g.writeHighScore(f);

            String gameKey = g.getGameName();
            if (gameCounts.containsKey(gameKey)) {
                gameCounts.put(gameKey, gameCounts.get(gameKey) + 1);
            } else {
                gameCounts.put(gameKey, 1);
            }
        }
    }

    public static void loadGames() {
        games.clear();
        games.add(new BuzzFeedQuizGame());
    }

    public static void printGameChoices() {
        int n = 1;
        for (Game s : games) {
            System.out.println("[" + (n++) + "]: " + s.getGameName());
        }
    }

    public static Game getGameChoice() {
        int choice = ErrorCheck.getInt(sc);
        while (choice < 1 || choice > games.size()) {
            System.out.println("We don't have this number. Try again.");
            choice = ErrorCheck.getInt(sc);
        }
        return games.get(choice - 1);
    }
}