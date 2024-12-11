package tests;
import exceptions.InvalidPuzzleException;
import exceptions.NullPuzzleException;
import puzzles.*;

import java.util.ArrayList;

public class PuzzleDisplayFromFile {

    public static void main(String[] args) {
        try {
            DisplayAllPuzzles();
        } catch (InvalidPuzzleException | NullPuzzleException e) {
            e.printStackTrace();
        }
    }

    public static void DisplayAllPuzzles() throws InvalidPuzzleException, NullPuzzleException {
        /*
        Postcondition 1 (thePuzzles): thePuzzles consists of every Puzzle created
        Post 2 (Various): The question and visual of all puzzles in thePuzzles are displayed to the console
         */
        PuzzleSolver<Object[][]> sudokuSolver = new PuzzleSolver<>();
        PuzzleSolver<String[]> oddOneOutSolver = new PuzzleSolver<>();
        PuzzleSolver<String> riddleSolver = new PuzzleSolver<>();
        PuzzleSolver<String[][]> wrdSearchSolver = new PuzzleSolver<>();
        ArrayList<Puzzle> thePuzzles = PuzzleDataHandler.loadPuzzlesFromFile("allPuzzles.txt", 5);
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
}
