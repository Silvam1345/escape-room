package database;
import leaderboards.*;
import java.sql.*;
import java.util.ArrayList;

public class PlayerDAO {

    public static void insertPlayer(Player player, String url) throws SQLException {
        /*
        Intent: Performs an SQL insert of the player's stats into the Player Table as a new entry,
        using the specified path to the database.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs, finalScore = 5000
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: Url string is one that is valid and connects to a database
         */
        String sql = "INSERT INTO Player(player_name, puzzles_solved, time_played, final_score) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getPuzzlesSolved());
            pstmt.setLong(3, player.getTime());
            pstmt.setInt(4, player.getFinalScore());
            pstmt.executeUpdate();
        }

    }

    public static int getPlayerIdByName(String name, String url) {
        /*
        Intent: Gets the player id from the Player table, based on the specified player name
        Example: Player entry: id = 12 playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs, finalScore = 5000
        Precondition 1: Player name exists in the table
        Precondition 2: Url string is one that is valid and connects to a database
        Postcondition: Returns an int representing the id value of the player entry from the Player table
         */
        int playerId = -1;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("SELECT Player.id FROM Player WHERE player_name = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                playerId = rs.getInt("id");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playerId;
    }
        public static void insertPlayerStat(Player player, String difficulty, int numProblems, String url) {
        /*
        Intent: Inserts a player's additional stats to the PlayerStat table as a new entry.
        Example: Entry: player_id = "12", difficulty = Medium, num_problems = 5
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: String difficulty is a valid difficulty
        Precondition 3: Url string is valid
         */
        String insertQuery = "INSERT INTO PlayerStat(player_id, difficulty, num_problems) VALUES(?,?,?)";
        int playerId = getPlayerIdByName(player.getName(), url);
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement insertPS = conn.prepareStatement(insertQuery);
            insertPS.setInt(1, playerId);
            insertPS.setString(2, difficulty);
            insertPS.setInt(3, numProblems);
            insertPS.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Work in Progress
    /*
    public static void removePlayer(Player player, String url) {
        /*
        Intent: Update the leaderboard file with the addition of a new player's stats.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: ArrayList of Player objects are not null nor are any of its fields null
        Precondition 3: Output file is the same name as the file from which the previous ArrayList of players was
        extracted from
        Postcondition: Rewrites the leaderboard file to add the new player's stats.
         */
        /*
        String query = "DELETE FROM Player WHERE Player.id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            int playerId = getPlayerIdByName(player.getName(), url);
            pstmt.setInt(1, playerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
         */


    // Work in Progress
    /*
    public static void removePlayerStat(Player player, String url) {
        /*
        Intent: Update the leaderboard file with the addition of a new player's stats.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: ArrayList of Player objects are not null nor are any of its fields null
        Precondition 3: Output file is the same name as the file from which the previous ArrayList of players was
        extracted from
        Postcondition: Rewrites the leaderboard file to add the new player's stats.
         */

        /*
        int playerId = getPlayerIdByName(player.getName(), url);
        String query = "DELETE FROM PlayerStat WHERE player_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, playerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
         */
        public static ArrayList<Player> retrievePlayers(String url) {
        /*
        Intent: Gets all Player entries from the Player table and sorts by final score in descending order
        Precondition: Url string is valid
        Postcondition: Returns an Arraylist of Player objects from the Player Table in the database
         */
        ArrayList<Player> players = new ArrayList<>();
        String query = "SELECT * FROM Player " +
                        "ORDER BY final_score " +
                        "DESC ";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("player_name");
                int puzzlesSolved = rs.getInt("puzzles_solved");
                long timePlayed = rs.getLong("time_played");
                int finalScore = rs.getInt("final_score");

                players.add(new Player(name, puzzlesSolved, timePlayed, finalScore));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

}
