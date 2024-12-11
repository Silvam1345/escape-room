package tests;
import puzzles.*;
import main.RunGame;
import exceptions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


public class PuzzleErrorTests {

    @Test
    public void testNullPuzzleException() {
        NullPuzzleException thrown = assertThrows(NullPuzzleException.class, () -> PuzzleDataHandler.loadPuzzlesFromFile("Test.txt", 5),
                "Expected runGame() to throw, but it didn't");
        assertTrue(thrown.toString().contains("NullPuzzleException: ArrayList contains no Puzzle type instances."));

    }

    @Test
    public void testInvalidPuzzleType() {
        ArrayList<Puzzle> testPuzzles2 = new ArrayList<Puzzle>();
        Riddle testRiddle = new Riddle("Medium", "You do it every day.", "footsteps", "Riddle",
                "The more you take, the more you leave behind. What am I?", "");

        Sudoku testSudoku = new Sudoku("Hard", "test1", "answer1", "Survival", "Which " +
                "number goes in the marked (*) position?", null);
        testPuzzles2.add(testRiddle);
        testPuzzles2.add(testSudoku);
        testPuzzles2.add(testRiddle);
        testPuzzles2.add(testRiddle);
        testPuzzles2.add(testRiddle);
        InvalidPuzzleException thrown = assertThrows(InvalidPuzzleException.class, () -> RunGame.runGame("test", testPuzzles2),
                "Expected runGame() to throw, but it didn't");
        assertTrue(thrown.toString().contains("InvalidPuzzleException: Type Survival is not a recognized Puzzle type."));
    }


}
