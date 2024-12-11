package exceptions;

public class NullPuzzleException extends Exception{

    @Override
    public String toString() {
        return "NullPuzzleException: ArrayList contains no Puzzle type instances.";
    }
}
