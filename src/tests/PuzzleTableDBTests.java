package tests;
import exceptions.InvalidPuzzleException;
import exceptions.NullPuzzleException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;
import puzzles.*;
import database.*;
public class PuzzleTableDBTests {

    public static final String testUrl =
            "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/PuzzlesTest.db";
    ArrayList<Puzzle> thePuzzles = new ArrayList<Puzzle>();


    // ONLY UNCOMMMENT AND RUN if Puzzle Table is empty and txt file has entries
    /*
    @Test
    public void loadPuzzleTableDB() throws NullPuzzleException {
        thePuzzles = PuzzleDataHandler.loadPuzzlesFromFile("allPuzzles.txt", 0);
        try {
            Connection conn = DriverManager.getConnection(
            "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db");
            for (Puzzle puzzle : thePuzzles) {
                PuzzleDAO.addPuzzle(puzzle,
                        "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     */

    @Test
    public void testRetrieveEntries() throws SQLException, InvalidPuzzleException {
        thePuzzles = PuzzleDAO.getPuzzles(testUrl, 0);
        assertEquals(26, thePuzzles.size());
        PuzzleSolver<Object[][]> sudokuSolver = new PuzzleSolver<>();
        PuzzleSolver<String[]> oddOneOutSolver = new PuzzleSolver<>();
        PuzzleSolver<String> riddleSolver = new PuzzleSolver<>();
        PuzzleSolver<String[][]> wrdSearchSolver = new PuzzleSolver<>();
        for (Puzzle puzzle : thePuzzles) {
            switch (puzzle) {
                case Riddle riddle -> riddleSolver.displayPuzzle(riddle);
                case OddOneOut oddOneOut -> oddOneOutSolver.displayPuzzle(oddOneOut);
                case Sudoku sudoku -> sudokuSolver.displayPuzzle(sudoku);
                case WordSearch wordSearch -> wrdSearchSolver.displayPuzzle(wordSearch);
                case null, default -> {
                    assert puzzle != null;
                    throw new InvalidPuzzleException(puzzle.getType());
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testGroupByFilter() {
        try (Connection conn = DriverManager.getConnection(testUrl);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT " +
                    "puzzle_type, " +
                    "SUM(CASE WHEN difficulty = 'Easy' THEN 1 ELSE 0 END) AS Easy, " +
                    "SUM(CASE WHEN difficulty = 'Medium' THEN 1 ELSE 0 END) AS Medium, " +
                    "SUM(CASE WHEN difficulty = 'Hard' THEN 1 ELSE 0 END) AS Hard " +
                    "FROM Puzzle " +
                    "GROUP BY puzzle_type;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String type = rs.getString("puzzle_type");
                int easy = rs.getInt("Easy");
                int medium = rs.getInt("Medium");
                int hard = rs.getInt("Hard");

                System.out.println("Type: " + type);
                System.out.println("Easy: " + easy);
                System.out.println("Medium: " + medium);
                System.out.println("Hard: " + hard);
                System.out.println("---------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
