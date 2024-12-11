package tests;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;

import database.*;
import leaderboards.*;
public class PlayerTableDBTests {
    public static final String testUrl =
            "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/PuzzlesTest.db";
    Leaderboard leaderboard = new Leaderboard();
    ArrayList<Player> players = new ArrayList<>();


    /*
    // DO NOT UNCOMMENT AND RUN unless Game Player's Table is empty and .ser file has entries
    @Test
    public void loadGameLeaderboardTable() {
        String url = "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db";
        players = leaderboard.getLeaderboard("fullLeaderboard.ser");
        try {
            Connection conn = DriverManager.getConnection(url);
            for (Player player : players) {
                PlayerDAO.insertPlayer(player, url);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     */
    @Test
    public void testRetrievePlayerTable() {
        players = PlayerDAO.retrievePlayers(testUrl);
        assertEquals(10, players.size());
    }

    /*
    @Test
    public void testAddPlayersToDB() throws SQLException {
        // ONLY UNCOMMENT AND RUN if Player table in PuzzlesTest.db has no entries first
        String query = "SELECT COUNT(*) as total_count FROM Player";
        players = leaderboard.getLeaderboardFromBinary("testExpBinary.ser");
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            for (Player player : players) {
                PlayerDAO.insertPlayer(player, testUrl);
            }
            ResultSet rs = stmt.executeQuery(query);

            int count = rs.getInt(1);
            assertEquals(10, count);



        }
    }
     */

    @Test
    public void testPlayerAvgScore() {
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT AVG(final_score) as avg_score FROM Player ";
            ResultSet rs = stmt.executeQuery(query);
            int avgScore = rs.getInt(1);
            System.out.println("The average score is " + avgScore);
            assertEquals(2575, avgScore);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlayerOrderByScore() {
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT player_name, final_score FROM Player " +
                    "WHERE final_score > 3000 "+
                    "ORDER BY final_score " +
                    "DESC ";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString(1);
                int finalScore = rs.getInt(2);
                System.out.println("Name: " + name + "\nScore: " + finalScore + "\n-");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlayerOrderByTime() {
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT player_name, time_played, final_score FROM Player " +
                    "WHERE time_played > 100 " +
                    "ORDER BY time_played " +
                    "DESC ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString(1);
                long timePlayed = rs.getLong(2);
                int finalScore = rs.getInt(3);
                System.out.println("Name: " + name + "\nTime: " + timePlayed + "\nScore: " + finalScore + "\n-");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJoinTables() {
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT Player.id, Player.player_name, Player.final_score, PlayerStat.difficulty, " +
                    "Player.puzzles_solved, PlayerStat.num_problems " +
                    "FROM Player " +
                    "JOIN PlayerStat ON Player.id = PlayerStat.player_id ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int finalScore = rs.getInt(3);
                String difficulty = rs.getString(4);
                int puzzlesSolved = rs.getInt(5);
                int numProblems = rs.getInt(6);

                System.out.println(id + " " + name + " " + finalScore + " " + difficulty + " " +
                        puzzlesSolved + " " + numProblems);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddPlayerStat() {
        Player player = new Player("Test", 3, 100, 1000);
        try {
            int size = PlayerDAO.retrievePlayers(testUrl).size();
            PlayerDAO.insertPlayer(player, testUrl);
            PlayerDAO.insertPlayerStat(player, "Easy", 3, testUrl);

            players = PlayerDAO.retrievePlayers(testUrl);
            assertEquals(size+1, players.size());

            String query = "SELECT COUNT(*) as total_count FROM PlayerStat";
            Connection conn = DriverManager.getConnection(testUrl);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int count = rs.getInt(1);
            assertEquals(size+1, count);
            //Please remove both entries from their tables before running this test again
            //PlayerDAO.removePlayerStat(player, testUrl);
            //PlayerDAO.removePlayer(player, testUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
