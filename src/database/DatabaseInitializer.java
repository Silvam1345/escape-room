package database;
import java.sql.*;

public class DatabaseInitializer {
    private static final String url = "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db";

    public static void initializeDB() throws SQLException, ClassNotFoundException {
        /*
        Intent: Update the leaderboard file with the addition of a new player's stats.
        Example: Player object: playerName = "Toby", puzzlesSolved = 5, timeCompleted = 90 secs
        Precondition 1: Player newPlayer is not null nor are any of its fields null
        Precondition 2: ArrayList of Player objects are not null nor are any of its fields null
        Precondition 3: Output file is the same name as the file from which the previous ArrayList of players was
        extracted from
        Postcondition: Rewrites the leaderboard file to add the new player's stats.
         */

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT sqlite_version()");
            if (rs.next()) {
                System.out.println("SQLite version used by JDBC: " + rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute("CREATE TABLE IF NOT EXISTS Puzzle (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "difficulty TEXT, " +
                        "hint TEXT, " +
                        "answer TEXT, " +
                        "puzzle_type TEXT, " +
                        "question TEXT, " +
                        "visual TEXT)");

                stmt.execute("CREATE TABLE IF NOT EXISTS Player (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "player_name TEXT, " +
                        "puzzles_solved INTEGER, " +
                        "time_played INTEGER, " +
                        "final_score INTEGER)");

                stmt.execute("CREATE TABLE IF NOT EXISTS PlayerStat (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "player_id INTEGER NOT NULL, " +
                        "difficulty TEXT NOT NULL CHECK (difficulty IN ('Easy', 'Medium', 'Hard')), " +
                        "num_problems INTEGER NOT NULL CHECK (num_problems IN (3, 5, 7)), " +
                        "FOREIGN KEY (player_id) REFERENCES Player(id))");

                System.out.println("Database initialized.");
            }
        }

    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        initializeDB();
    }
}
