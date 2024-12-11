package exceptions;

public class InvalidPuzzleException extends Exception{
    private String type;

    public InvalidPuzzleException(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InvalidPuzzleException: Type " + type + " is not a recognized Puzzle type.";
    }
}
