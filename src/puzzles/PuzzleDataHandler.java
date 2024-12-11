package puzzles;

import database.PuzzleDAO;
import exceptions.NullPuzzleException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleDataHandler {
    public static ArrayList<Puzzle> loadPuzzlesFromFile(String fileName, int numPuzzles) throws NullPuzzleException {
        /*
        Intent: Read puzzles from a file and load them to a list to be used in-game, depending on a specified number
        Precondition 1: File exists in the directory, int is a valid number of puzzles to solve
        Postcondition: Returns an ArrayList containing a certain number of randomly selected puzzles
        */
        ArrayList<Puzzle> thePuzzles = new ArrayList<>();
        ArrayList<Puzzle> gamePuzzles = new ArrayList<>();

        // Reads all the puzzles from the specified file into a list
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            String difficulty = null;
            String hint = null;
            String answer = null;
            String type = null;
            String question = null;
            String subLine;
            while ((line = reader.readLine()) != null) {
                // Based on formatting on input file
                if (line.startsWith("difficulty:")) {
                    subLine = "difficulty:";
                    difficulty = line.substring(subLine.length()+1).trim();
                } else if (line.startsWith("hint:")) {
                    subLine = "hint:";
                    hint = line.substring(subLine.length()+1).trim();
                } else if (line.startsWith("answer:")) {
                    subLine = "answer:";
                    answer = line.substring(subLine.length()+1).trim();
                } else if (line.startsWith("type:")) {
                    subLine = "type:";
                    type = line.substring(subLine.length()+1).trim();
                } else if (line.startsWith("question:")) {
                    subLine = "question:";
                    question = line.substring(subLine.length()+1).trim();
                } else if (line.startsWith("visual:")) {
                    assert type != null;
                    switch (type) {
                        case "Sudoku" -> {
                            List<Object[]> rows = new ArrayList<>();
                            while ((line = reader.readLine()) != null && !line.trim().equals("-")) {
                                String[] numbers = line.trim().split(" ");
                                rows.add(numbers);
                            }

                            Object[][] grid = rows.toArray(new Object[0][]);
                            thePuzzles.add(new Sudoku(difficulty, hint, answer, type, question, grid));
                        }
                        case "WordSearch" -> {
                            List<String[]> rows = new ArrayList<>();
                            while ((line = reader.readLine()) != null && !line.trim().equals("-")) {
                                String[] numbers = line.trim().split(" ");
                                rows.add(numbers);
                            }
                            String[][] grid = rows.toArray(new String[0][]);
                            thePuzzles.add(new WordSearch(difficulty, hint, answer, type, question, grid));
                        }
                        case "OddOneOut" -> {
                            List<String> items = new ArrayList<>();
                            while ((line = reader.readLine()) != null && !line.trim().equals("-")) {
                                items.add(line.trim());
                            }
                            thePuzzles.add(new OddOneOut(difficulty, hint, answer, type, question, items.toArray(new String[0])));
                        }
                        case "Riddle" -> {
                            String visual = "";
                            thePuzzles.add(new Riddle(difficulty, hint, answer, type, question, visual));
                        }
                    }
                }
            }

            // Randomly chooses a certain number of puzzles to load into the game for the player to solve
            gamePuzzles = selectPuzzles(thePuzzles, numPuzzles);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPuzzleException e) {
            throw new NullPuzzleException();
        }
        if (gamePuzzles.isEmpty()) {
            return thePuzzles;
        }
        return gamePuzzles;
    }

    public static ArrayList<Puzzle> loadPuzzlesFromTable(int numPuzzles) {
        /*
        Intent: Read puzzles from a table and load them to a list to be used in-game, depending on a specified number
        Precondition 1: int is a valid number of puzzles to solve
        Postcondition: Returns an ArrayList containing a certain number of selected puzzles
        */
        ArrayList<Puzzle> thePuzzles = new ArrayList<>();
        ArrayList<Puzzle> gamePuzzles = new ArrayList<>();
        String url = "jdbc:sqlite:C:/Users/silva/IdeaProjects/cs622TermProject/src/database/Puzzles.db";
        try {
            thePuzzles = PuzzleDAO.getPuzzles(url, numPuzzles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            gamePuzzles = selectPuzzles(thePuzzles, numPuzzles);
        } catch (NullPuzzleException e) {
            e.printStackTrace();
        }
        return gamePuzzles;
    }
    public static ArrayList<Puzzle> selectPuzzles(ArrayList<Puzzle> thePuzzles, int numPuzzles) throws NullPuzzleException {
        /*
        Intent: Randomly selects a specified number of puzzles from the filtered list to display in game
        Precondition 1: Arraylist of puzzle objects is not empty
        Precondition 2: int is a valid number of puzzles to solve
        Postcondition: Returns an ArrayList containing a certain number of selected in-game puzzles
        */
        // Randomly chooses a certain number of puzzles to load into the game for the player to solve
        ArrayList<Puzzle> gamePuzzles = new ArrayList<>();
        Random random = new Random();
        if (thePuzzles.isEmpty()) {
            throw new NullPuzzleException();
        } else if (numPuzzles > 0){
            //Checks whether to return a selected number of puzzles or all puzzles (testing purposes)
            for (int i = 0; i < numPuzzles; i++) {
                int randInt = random.nextInt(thePuzzles.size());

                gamePuzzles.add(thePuzzles.get(randInt));
                thePuzzles.remove(randInt);
            }
        }
        return gamePuzzles;
    }

}
