package puzzles;

// To represent a Sudoku type of Puzzle
public class Sudoku extends Puzzle<Object[][]> {

    public Sudoku(String difficulty, String hint, String answer, String type, String question, Object[][] visual) {
        super(difficulty, hint, answer, type, question, visual);

    }

    public void displayVisual(Object[][] visual) {
        /*
        Intent: Displays the visual for a Sudoku type Puzzle.
        Postcondition: the 2d grid visual for the Sudoku puzzle is displayed to the console
         */
        for (int i = 0; i < visual.length; i++) {
            for (int j = 0; j < visual[i].length; j++) {
                System.out.print(visual[i][j] + " ");
            }
            System.out.println();
        }
    }
}
