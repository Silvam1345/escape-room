package tests;

// Example displaying different types of puzzle objects

import puzzles.Puzzle;
import puzzles.Sudoku;
import puzzles.Riddle;
import puzzles.OddOneOut;
import puzzles.PuzzleSolver;
import java.util.ArrayList;
import java.util.Objects;
import exceptions.InvalidPuzzleException;
public class PuzzleDisplayExample {
    public static ArrayList<Puzzle> thePuzzles = new ArrayList<Puzzle>();

    public static void main(String[] args) {
        System.out.println("========PuzzleDisplayExample========");
        try {
            displayAllPuzzles();
        } catch (InvalidPuzzleException e){
            e.printStackTrace();
        }
    }

    static void displayAllPuzzles() throws InvalidPuzzleException {
        /*
        Postcondition 1 (thePuzzles): thePuzzles consists of every Puzzle created
        Post 2 (Various): The question and visual of all puzzles in thePuzzles are displayed to the console
        Post 3 (Counts): As for displayTypeCount()
         */
        Object[][] testSudoku = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, "*", 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        Riddle riddle1 = new Riddle("Medium", "You do it every day.", "footsteps", "Riddle",
                "The more you take, the more you leave behind. What am I?", "");

        OddOneOut oddOneOut1 = new OddOneOut("Easy", "Put them on water. What happens?", "anvil",
                "OddOneOut", "Which one is the odd one out?", new String[]{"branch", "strawberry", "anvil", "iceberg", "boat"});

        Sudoku sudoku1 = new Sudoku("Hard", "test1", "answer1", "Sudoku", "Which " +
                "number goes in the marked (*) position?", testSudoku);

        thePuzzles.add(riddle1);
        thePuzzles.add(oddOneOut1);
        thePuzzles.add(sudoku1);

        PuzzleSolver<Object[][]> sudokuSolver = new PuzzleSolver<>();
        PuzzleSolver<String[]> oddOneOutSolver = new PuzzleSolver<>();
        PuzzleSolver<String> riddleSolver = new PuzzleSolver<>();

        sudokuSolver.displayPuzzle(sudoku1);
        System.out.println();
        oddOneOutSolver.displayPuzzle(oddOneOut1);
        System.out.println();
        riddleSolver.displayPuzzle(riddle1);
        displayTypeCount();
    }

    static void displayTypeCount() {
        /*
        Postcondition: The total counts for each type of puzzle in thePuzzles are displayed on the console
         */
        int sudokuCount = 0, riddleCount = 0, oddOneOutCount = 0;
        for (Puzzle puzzle : thePuzzles) {
            if (Objects.equals(puzzle.getType(), "Riddle")) {
                riddleCount++;
            }
            else if (Objects.equals(puzzle.getType(), "OddOneOut")) {
                oddOneOutCount++;
            }
            else {
                sudokuCount++;
            }
        }
        System.out.println(sudokuCount + " of Sudoku, " + riddleCount + " of Riddle, and " + oddOneOutCount +
                " of OddOneOut");
    }
}
