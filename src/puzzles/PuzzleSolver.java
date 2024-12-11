package puzzles;

public class PuzzleSolver<P> {
    public void displayPuzzle(Puzzle<P> puzzle) {
        /*
        Intent: Displays the Puzzle's question and visual
        Precondition: The puzzle instance is of a valid subclass object type from Puzzle
        Postcondition: Prints the Puzzle question and its accompanying visual to the console
         */
        System.out.println("Question: " + puzzle.getQuestion());
        puzzle.displayVisual(puzzle.getVisual());
    }
}
