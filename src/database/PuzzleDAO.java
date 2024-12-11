package database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import puzzles.*;

public class PuzzleDAO {

    public static ArrayList<Puzzle> getPuzzles(String url, int numPuzzles) throws SQLException {
        /*
        Intent: Performs SQL statements to filter out puzzles based on the user's chosen difficulty
        Precondition 1: int numPuzzles is a valid integer
        Precondition 2: Url string is one that is valid and connects to a database
        Postcondition: Returns an Arraylist of filtered Puzzle objects to potentially use in game
         */
        ArrayList<Puzzle> thePuzzles = new ArrayList<>();
        String query = "";
        if (numPuzzles == 3) {
            query = "SELECT * FROM Puzzle " +
                    "WHERE difficulty = 'Easy' " +
                    "ORDER BY puzzle_type ";
        }
        else if (numPuzzles == 5) {
            query = "SELECT * FROM Puzzle " +
                    "WHERE difficulty IN ('Easy', 'Medium') " +
                    "ORDER BY puzzle_type ";
        }
        else if (numPuzzles == 7) {
            query = "SELECT * FROM Puzzle " +
                    "WHERE difficulty IN ('Medium', 'Hard') " +
                    "ORDER BY puzzle_type ";
        }
        else if (numPuzzles == 0) {
            query = "SELECT * FROM Puzzle";
        }

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("puzzle_type");
                String difficulty = rs.getString("difficulty");
                String hint = rs.getString("hint");
                String answer = rs.getString("answer");
                String question = rs.getString("question");
                String visualString = rs.getString("visual");
                Puzzle puzzle = null;
                switch (type) {
                    case "Sudoku" -> {
                        Object[][] sudokuVisual = stringTo2DObjectArray(visualString);
                        puzzle = new Sudoku(difficulty, hint, answer, type, question, sudokuVisual);
                    }
                    case "OddOneOut" -> {
                        puzzle = new OddOneOut(difficulty, hint, answer, type, question, new String[]{visualString});
                    }
                    case "Riddle" -> {
                        puzzle = new Riddle(difficulty, hint, answer, type, question, "");
                    }
                    case "WordSearch" -> {
                        String[][] wordSearchVisual = stringTo2DStringArray(visualString);
                        puzzle = new WordSearch(difficulty, hint, answer, type, question, wordSearchVisual);
                    }

                }
                if (puzzle != null) {
                    thePuzzles.add(puzzle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thePuzzles;
    }

    public static void addPuzzle(Puzzle puzzle, String url) {
        /*
        Intent: Performs an SQL insert of a Puzzle object into the Puzzle table in the database
        Precondition 1: puzzle type is valid and has no fields that are nulll
        Precondition 2: Url string is one that is valid and connects to a database
         */
        String query =
                "INSERT INTO Puzzle(difficulty, hint, answer, puzzle_type, question, visual) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, puzzle.getDifficulty());
            pstmt.setString(2, puzzle.getHint());
            pstmt.setString(3, puzzle.getAnswer());
            pstmt.setString(4, puzzle.getType());
            pstmt.setString(5, puzzle.getQuestion());

            if (puzzle instanceof Sudoku) {
                pstmt.setString(6, object2DArrayToString(((Sudoku) puzzle).getVisual()));
            } else if (puzzle instanceof OddOneOut) {
                pstmt.setString(6, Arrays.toString(((OddOneOut) puzzle).getVisual()));
            } else if (puzzle instanceof WordSearch) {
                pstmt.setString(6, string2DArrayToString(((WordSearch) puzzle).getVisual()));
            } else {
                pstmt.setString(6, null); // Riddle has no visual representation
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static Object[][] stringTo2DObjectArray(String input) {
        String[] rows = input.split("\n");
        Object[][] result = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(" ");
            result[i] = new Object[cols.length];
            for (int j = 0; j < cols.length; j++) {
                try {
                    result[i][j] = Integer.parseInt(cols[j]);
                } catch (NumberFormatException e) {
                    result[i][j] = cols[j];
                }
            }
        }
        return result;
    }

    private static String[][] stringTo2DStringArray(String input) {
        String[] rows = input.split("\n");
        String[][] result = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].split(" ");
        }
        return result;
    }

    private static String object2DArrayToString(Object[][] input) {
        StringBuilder sb = new StringBuilder();
        for (Object[] row : input) {
            for (Object col : row) {
                sb.append(col).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static String string2DArrayToString(String[][] input) {
        StringBuilder sb = new StringBuilder();
        for (String[] row : input) {
            for (String col : row) {
                sb.append(col).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }




}
