package leaderboards;

import database.PlayerDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class PlayerDataHandler {

    private static final String url = "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db";
    public static void serializePlayers(List<Player> players, String filename) {
        /*
        Intent: Serializes the list of players to a specified ".ser" file.
        Example: List<Player> instance: players = [{"Toby", 5, 90}, {"James", 2, 30}], String "fullLeaderboard.ser"
        Precondition: Player elements in the list instance are not null nor are any of their fields null
         */
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Player> deserializePlayers(String filename) {
        /*
        Intent: Reads the list of player objects from the file, deserializing them.
        Example: String "fullLeaderboard.ser"
        Precondition: File exists in the system or project
        Postcondition: returns a list containing all the Player objects read from the file.
         */
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void insertToPlayerTable(Player player) {
        /*
        Intent: Calls the insertPlayer() function to add a player as a new entry to the Player table
        Precondition: Player instance is not null
         */
        try {
            PlayerDAO.insertPlayer(player, url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertToPlayerStatTable(Player player, String difficulty, int numProblems) {
        /*
        Intent: Calls the insertPlayerStat() function to add a player's additional stats to the PlayerStat table
        Precondition 1: Player object is not null
        Precondition 2: Difficulty and numProblem variables are valid
         */
        try {
            PlayerDAO.insertPlayerStat(player, difficulty, numProblems, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Player> retrieveFromTable() {
        /*
        Intent: Calls the retrievePlayers() function to get all entries of Players from the Player table
        Postcondition: returns a list containing all the Player objects from the Player table
         */
        return PlayerDAO.retrievePlayers(url);
    }
}
