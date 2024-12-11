package puzzles;

public abstract class Puzzle<P> {
    private String difficulty;
    private boolean isCompleted;
    private String hint;
    private String answer;
    private String type;
    private String question;
    private P visual;

    // To represent a generic Puzzle object
    public Puzzle(String difficulty, String hint, String answer, String type, String question, P visual) {
        this.difficulty = difficulty;
        this.hint = hint;
        this.answer = answer;
        this.type = type;
        this.question = question;
        this.isCompleted = false;
        this.visual = visual;
    }

    public String getHint() {
        return hint;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public P getVisual() {
        return visual;
    }
    public abstract void displayVisual(P visual);
    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted() {
        this.isCompleted = !isCompleted;
    }
}
