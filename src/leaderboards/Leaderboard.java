package leaderboards;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Leaderboard {
    private ArrayList<Player> leaderboard;

    public Leaderboard() {
        this.leaderboard = new ArrayList<>();
    }
    public ArrayList<Player> getLeaderboardFromBinary(String fileName) {
        /*
        Intent: Gets the currently saved list of player scores from the leaderboard file.
        Example: String "leaderboard"
        Precondition: String fileName must already exist in the directory
        Postcondition: returns an ArrayList of Player objects extracted from the file.
         */
        leaderboard = (ArrayList<Player>) PlayerDataHandler.deserializePlayers(fileName);
        return leaderboard;
    }

    public ArrayList<Player> getLeaderboardFromTable() {
        /*
        Intent: Gets the currently saved list of player scores from the Player Table in Puzzles.db
        Postcondition: returns an ArrayList of Player objects extracted from the file.
         */
        return PlayerDataHandler.retrieveFromTable();
    }

    public void displayLeaderboard(ArrayList<Player> players) {
        /*
        Intent: Displays the stats of the current list of players taken from the leaderboard file.
        Example: ArrayList<Player> players = [{"Toby", 5, 90}, {"James", 2, 30}]
        Precondition: Player elements in the ArrayList are not null nor are any of their fields null
        Postcondition: Displays the name, score, and time of every player in the ArrayList to the console
         */
        System.out.println("========Leaderboard========");
        System.out.println();
        for (Player player : players) {
            System.out.println(player.toString());
            System.out.println("-");
        }
        System.out.println();
    }

    public void updateLeaderboard(ArrayList<Player> players, Player newPlayer, String outputFile) {
        /*
        Intent: Update the leaderboard file with the addition of a new player's stats.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: ArrayList of Player objects are not null nor are any of its fields null
        Precondition 3: Output file is the same name as the file from which the previous ArrayList of players was
        extracted from
        Postcondition: Rewrites the leaderboard file to add the new player's stats.
         */
        if (newPlayer != null) {
            players.add(newPlayer);
            PlayerDataHandler.insertToPlayerTable(newPlayer);
        }

        PlayerDataHandler.serializePlayers(players, outputFile);


    }

    public ArrayList<Player> createLeaderboard(String fileName) {
        /*
        Intent: Create a new leaderboard by reading from a .txt file to serialize to a .ser file
        Precondition: File exists in the directory
        Postcondition: Returns a list of all players read from the file
         */
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            String subLine;
            String name = "";
            int puzzlesSolved = 0;
            long timePlayed = 0;
            int finalScore = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    subLine = "Name:";
                    name = line.substring(subLine.length()+1).trim();
                }
                else if (line.startsWith("Puzzles Solved:")) {
                    subLine = "Puzzles Solved:";
                    puzzlesSolved = Integer.parseInt(line.substring(subLine.length()+1).trim());
                }
                else if (line.startsWith("Time:")) {
                    subLine = "Time:";
                    String[] timeParts = line.substring(subLine.length()+1).trim().split(" ");
                    timePlayed = Long.parseLong(timeParts[0]);
                }
                else if (line.startsWith("Final Score:")) {
                    subLine = "Final Score:";
                    String[] timeParts = line.substring(subLine.length()+1).trim().split(" ");
                    finalScore = Integer.parseInt(timeParts[0]);
                }
                else if (line.equals("-")){
                    Player newPlayer = new Player(name, puzzlesSolved, timePlayed, finalScore);
                    leaderboard.add(newPlayer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

}
