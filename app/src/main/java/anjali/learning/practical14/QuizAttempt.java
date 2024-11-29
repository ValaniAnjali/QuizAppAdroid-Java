package anjali.learning.practical14;

public class QuizAttempt {
    private int score;
    private int attemptedCount;

    public QuizAttempt(int score, int attemptedCount) {
        this.score = score;
        this.attemptedCount = attemptedCount;
    }

    public int getScore() {
        return score;
    }

    public int getAttemptedCount() {
        return attemptedCount;
    }
}
