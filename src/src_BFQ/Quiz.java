package src_BFQ;
import java.util.HashMap;
import java.util.Scanner;

public class Quiz {
    static Scanner sc = new Scanner(System.in);

  
    static HashMap<String, Integer> globalResults = new HashMap<>();
    static String lastResult = "";
static String lastScore = "0";

    public static void main(String[] args) {
        while (true) { 


            Category Claire = new Category("Claire",
                "You are a simple and loving person, but you can be slightly grouchy due to your intense workload and responsibilities.");
            Category Phil = new Category("Phil",
                "You are fun, creative, and chill. You love games and trying new things.");
            Category Manny = new Category("Manny",
                "You are emotional, romantic, and expressive.");
            Category Haley = new Category("Haley",
                "You are confident, social, and stylish.");


            HashMap<Category, Integer> counts = new HashMap<>();
            counts.put(Claire, 0);
            counts.put(Phil, 0);
            counts.put(Manny, 0);
            counts.put(Haley, 0);

            Question q1 = new Question("Your friend steals your favorite cookie, how do you react?");
            q1.possibleAnswers[0] = new Answer("You scold them", Claire);
            q1.possibleAnswers[1] = new Answer("You do nothing", Phil);
            q1.possibleAnswers[2] = new Answer("Post an embarrassing photo", Haley);
            q1.possibleAnswers[3] = new Answer("You cry", Manny);

            Question q2 = new Question("You wake up and see that you have a snow day! What will you do?");
            q2.possibleAnswers[0] = new Answer("Hangout with friends", Haley);
            q2.possibleAnswers[1] = new Answer("Find a new hobby", Phil);
            q2.possibleAnswers[2] = new Answer("Go for a jog", Claire);
            q2.possibleAnswers[3] = new Answer("Listen to music", Manny);

            Question q3 = new Question("What is your ideal weather?");
            q3.possibleAnswers[0] = new Answer("Chilly and cloudy", Claire);
            q3.possibleAnswers[1] = new Answer("Warm sunset", Manny);
            q3.possibleAnswers[2] = new Answer("Hot beach weather", Haley);
            q3.possibleAnswers[3] = new Answer("Sunny and clear", Phil);

            Question q4 = new Question("What is your favorite fruit?");
            q4.possibleAnswers[0] = new Answer("Apple", Claire);
            q4.possibleAnswers[1] = new Answer("Plum", Phil);
            q4.possibleAnswers[2] = new Answer("Watermelon", Haley);
            q4.possibleAnswers[3] = new Answer("Exotic fruits", Manny);

            Question q5 = new Question("You walk into a CVS, what are you buying?");
            q5.possibleAnswers[0] = new Answer("Chocolates for loved ones", Manny);
            q5.possibleAnswers[1] = new Answer("Newest video game/electronic", Phil);
            q5.possibleAnswers[2] = new Answer("Kitchen/household items", Claire);
            q5.possibleAnswers[3] = new Answer("Makeup", Haley);

            Question q6 = new Question("You’re feeling blue. How are you gonna cheer yourself up?");
            q6.possibleAnswers[0] = new Answer("Hang out with friends", Haley);
            q6.possibleAnswers[1] = new Answer("Hang out with family", Phil);
            q6.possibleAnswers[2] = new Answer("Stay in and rest", Claire);
            q6.possibleAnswers[3] = new Answer("Cry and listen to music", Manny);

            Question q7 = new Question("What is your ideal dinner?");
            q7.possibleAnswers[0] = new Answer("Any dinner with your family", Phil);
            q7.possibleAnswers[1] = new Answer("A homemade dinner", Claire);
            q7.possibleAnswers[2] = new Answer("A romantic dinner", Manny);
            q7.possibleAnswers[3] = new Answer("An expensive dinner", Haley);

            Question q8 = new Question("You just saw a UFO. What do you do?");
            q8.possibleAnswers[0] = new Answer("Tweet it", Haley);
            q8.possibleAnswers[1] = new Answer("Do nothing", Claire);
            q8.possibleAnswers[2] = new Answer("Chase it and ask to join", Phil);
            q8.possibleAnswers[3] = new Answer("Cry", Manny);

            Question[] qList = { q1, q2, q3, q4, q5, q6, q7, q8 };

            gameIntro();

 
            for (Question q : qList) {
                Category chosen = q.ask(sc);
 
                counts.put(chosen, counts.get(chosen) + 1);
            }

            Category mostChosen = null;
            int max = -1;
            for (Category cat : counts.keySet()) {
                int val = counts.get(cat);
                if (val > max) {
                    max = val;
                    mostChosen = cat;
                }
            }

            System.out.println("\nIf you were a Modern Family Character, you would be " + mostChosen.label + ".");
            System.out.println(mostChosen.description);
            lastResult = mostChosen.label;
lastScore = Integer.toString(max);


            String resultName = mostChosen.label;
            globalResults.put(resultName, globalResults.getOrDefault(resultName, 0) + 1);


            String topName = "";
            int topCount = 0;
            for (String key : globalResults.keySet()) {
                int val = globalResults.get(key);
                if (val > topCount) {
                    topCount = val;
                    topName = key;
                }
            }

            System.out.println("\n--- Overall Most Common Result So Far ---");
            System.out.println(topName + " has been the final result " + topCount + " time(s).");


            System.out.println("\nType 1 to play again, anything else to quit.");
            if (sc.hasNextInt() && sc.nextInt() == 1) {
                sc.nextLine(); 
                continue;
            } else {
                break;
            }
        }
    }

    public static void gameIntro() {
        System.out.println("Which Modern Family Character Are You?");
        System.out.println("You get to choose numbers 1-4 for every question.");
        System.out.println("Enter '1' to play!");

        while (!sc.hasNextInt() || sc.nextInt() != 1) {
            System.out.println("Unidentifiable input. Please enter '1' to play");
            sc.nextLine();
        }
        sc.nextLine(); 
    }
}











