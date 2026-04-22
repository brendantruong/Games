import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Game.Game;
import Game.ErrorCheck;

import src_BFQ.BuzzFeedQuizGame;
import src_ASCII.AsciiPortalGame;
import src_cards.SnapsGame;

public class GamePortal {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Game> games = new ArrayList<>();

    public static void main(String[] args) {

        HashMap<String,Integer> gameCounts = new HashMap<>();

        File f = new File("Highscore.csv");

        while(true){

            loadGames();

            System.out.println("\n=== GAME PORTAL ===");

            printGameChoices();

            System.out.print("Choose a game: ");

            Game g = getGameChoice();

            System.out.println("\nYou're playing " + g.getGameName());
            System.out.println("--------------------------------");

            g.play();

            g.writeHighScore(f);

            String key = g.getGameName();

            gameCounts.put(key, gameCounts.getOrDefault(key,0)+1);

            System.out.println("\nPlay count stats:");
            for(String k : gameCounts.keySet()){
                System.out.println(k + ": " + gameCounts.get(k));
            }

            System.out.println("\nPress ENTER to continue...");
            sc.nextLine();
        }
    }

    public static void loadGames(){

        games.clear();

        games.add(new BuzzFeedQuizGame());

        games.add(new AsciiPortalGame());

        games.add(new SnapsGame());
    }

    public static void printGameChoices(){

        int i=1;

        for(Game g : games){

            System.out.println("[" + i++ + "] " + g.getGameName());
        }
    }

    public static Game getGameChoice(){

        int choice = ErrorCheck.getInt(sc);

        while(choice < 1 || choice > games.size()){

            System.out.print("Invalid choice, try again: ");

            choice = ErrorCheck.getInt(sc);
        }

        return games.get(choice-1);
    }
}