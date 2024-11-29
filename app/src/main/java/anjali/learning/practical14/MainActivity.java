package anjali.learning.practical14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView Queno, Score, Correct, Wrong, Que;
    private RadioButton R1, R2, R3, R4;
    private Button Restart, Next, Submit;
    public String[] Questions = {
            "What does CPU stand for?", "Which of the following is an example of an input device?",
            "What is the function of an operating system?", "Which one is not a programming language?",
            "What is the smallest unit of data in a computer?", "Which protocol is used to transfer files over the internet?",
            "What does RAM stand for?", "Which type of storage is volatile?", "What is the main component of a web page?",
            "Which company created the Windows operating system?"
    };
    private char[] answers = {'b', 'b', 'd', 'c', 'b', 'b', 'b', 'c', 'b', 'b'}; // Correct answers
    private String[][] options = {
            {"Central Process Unit", "Central Processing Unit", "Central Programming Unit", "Control Processing Unit"},
            {"Monitor", "Keyboard", "Printer", "Speaker"},
            {"Manage hardware resources", "Run applications", "Provide a user interface", "All of the above"},
            {"Python", "JavaScript", "HTML", "C++"},
            {"Byte", "Bit", "Nibble", "Kilobyte"},
            {"HTTP", "FTP", "SMTP", "DNS"},
            {"Read Access Memory", "Random Access Memory", "Ready Application Module", "Run Access Memory"},
            {"Hard Disk", "SSD", "RAM", "ROM"},
            {"CSS", "HTML", "JavaScript", "PHP"},
            {"Apple", "Microsoft", "Google", "IBM"}
    };
    private int currentQuestion = 0;
    private int score = 0;
    private int correctCount = 0, wrongCount = 0;
    private RadioGroup radioGroup;
    private boolean[] answeredQuestions; // Track if a question has been answered
    private int[] selectedAnswers; // Track the selected answer for each question
    private Handler handler = new Handler(); // Handler to delay the reset of color
    private QuizDatabaseHelper dbHelper;
    private int currentScore = 0;
    private int attemptedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new QuizDatabaseHelper(this);

        // Initialize views
        Queno = findViewById(R.id.txtTotalQuestion);
        Score = findViewById(R.id.txtScore);
        Correct = findViewById(R.id.txtCorrect);
        Wrong = findViewById(R.id.txtWrong);
        Que = findViewById(R.id.que);
        R1 = findViewById(R.id.radiobutton1);
        R2 = findViewById(R.id.radiobutton2);
        R3 = findViewById(R.id.radiobutton3);
        R4 = findViewById(R.id.radiobutton4);
        Next = findViewById(R.id.btnNext);
        Restart = findViewById(R.id.btnRestart);
        Submit = findViewById(R.id.btnSubmit);

        answeredQuestions = new boolean[Questions.length]; // Initially, no question is answered
        selectedAnswers = new int[Questions.length]; // Initialize array to store selected answers

        // Set the initial question
        Que.setText(Questions[currentQuestion]);
        radioGroup = findViewById(R.id.radio_group1);
        Next.setEnabled(false);  // Disable Next button initially
        updateOptions();

        // Next Button Action
        Next.setOnClickListener(view -> {
            if (currentQuestion < Questions.length - 1) {
                // Check selected answer and update score
                checkAnswer();

                // Disable options temporarily while showing the correct answer
                disableOptions();

                // After 3 seconds, move to the next question
                handler.postDelayed(() -> {
                    currentQuestion++;
                    updateQuestion();
                    Next.setEnabled(false);
                }, 3000); // 3 seconds delay before showing the next question

            } else {
                Toast.makeText(MainActivity.this, "Last Question", Toast.LENGTH_SHORT).show();
                Next.setEnabled(false);
            }
        });

        // Submit Button Action
        Submit.setOnClickListener(view -> {
            // Check the last selected answer before submitting
            checkAnswer();
            // Disable the Submit button after submission
            Submit.setEnabled(false);
            // Display final score
            Toast.makeText(MainActivity.this, "Final Score: " + score + "/" + Questions.length, Toast.LENGTH_LONG).show();
            Next.setEnabled(false);
//            if (currentQuestion == Questions.length - 1) {
                finishQuiz();  // Save the score and attempted count
//            }
        });

        Restart.setOnClickListener(view -> {
            restartQuiz();
        });

        // OnCheckedChangeListener for RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Reset the colors when an option is selected
            resetRadioButtonColors();
            Next.setEnabled(true);
        });
    }

    private void updateOptions() {
        // Update the text for each radio button with the options for the current question
        R1.setText(options[currentQuestion][0]);
        R2.setText(options[currentQuestion][1]);
        R3.setText(options[currentQuestion][2]);
        R4.setText(options[currentQuestion][3]);
    }

    private void updateQuestion() {
        Que.setText(Questions[currentQuestion]);
        Queno.setText("Question " + (currentQuestion + 1) + "/10");
        resetRadioButtonColors();  // Reset the colors when the question changes
        radioGroup.clearCheck();   // Clear the previous selection
        updateOptions();           // Update the radio button options for the new question

        // Re-enable options if the question has not been answered
        if (!answeredQuestions[currentQuestion]) {
            enableOptions();
        } else {
            disableOptions();
            // Restore the previously selected answer
            int selectedOptionIndex = selectedAnswers[currentQuestion];
            if (selectedOptionIndex != -1) {
                RadioButton selectedRadioButton = getRadioButtonByIndex(selectedOptionIndex);
                selectedRadioButton.setChecked(true); // Check the previously selected radio button
                // Change color based on whether the answer was correct or not
                if (selectedOptionIndex == answers[currentQuestion] - 'a') {
                    selectedRadioButton.setTextColor(Color.GREEN);
                } else {
                    selectedRadioButton.setTextColor(Color.RED);
                }
            }
        }
    }

    private void enableOptions() {
        // Re-enable all radio buttons if the question hasn't been answered yet
        R1.setEnabled(true);
        R2.setEnabled(true);
        R3.setEnabled(true);
        R4.setEnabled(true);
    }

    private void disableOptions() {
        // Disable all radio buttons if the question has been answered
        R1.setEnabled(false);
        R2.setEnabled(false);
        R3.setEnabled(false);
        R4.setEnabled(false);
    }

    private void resetRadioButtonColors() {
        R1.setTextColor(Color.BLACK); // Reset to black
        R2.setTextColor(Color.BLACK); // Reset to black
        R3.setTextColor(Color.BLACK); // Reset to black
        R4.setTextColor(Color.BLACK); // Reset to black
    }

    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            int selectedOptionIndex = radioGroup.indexOfChild(selectedRadioButton);

            // Store the selected answer
            selectedAnswers[currentQuestion] = selectedOptionIndex;

            char correctAnswer = answers[currentQuestion];
            if (selectedOptionIndex == correctAnswer - 'a') {
                selectedRadioButton.setTextColor(Color.GREEN); // Correct Answer
                score++;
                correctCount++;
            } else {
                selectedRadioButton.setTextColor(Color.RED); // Incorrect Answer
                getRadioButtonByIndex(correctAnswer - 'a').setTextColor(Color.GREEN); // Show correct answer
                wrongCount++;
            }

            answeredQuestions[currentQuestion] = true; // Mark the question as answered

            // Ensure score does not exceed the number of questions
            if (score > Questions.length) {
                score = Questions.length;
            }

            // Update score display
            Score.setText("Score: " + score);
            Correct.setText("Correct: " + correctCount);
            Wrong.setText("Wrong: " + wrongCount);

            // Disable options after answering
            disableOptions();
        }
    }

    private RadioButton getRadioButtonByIndex(int index) {
        switch (index) {
            case 0:
                return R1;
            case 1:
                return R2;
            case 2:
                return R3;
            case 3:
                return R4;
            default:
                return null;
        }
    }

    private void finishQuiz() {
        // Save the score to the database
        dbHelper.insertScore(score, 1);

        // Optionally, increment the attempted count when the quiz ends
        attemptedCount++;

        // Save high score using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the current high score
        int highScore = sharedPreferences.getInt("high_score", 0);  // Default is 0 if no high score exists

        // Check if the current score is higher than the saved high score
        if (score > highScore) {
            // If so, update the high score
            editor.putInt("high_score", score);
            highScore=score;
            editor.apply();  // Save the new high score
        }

        // Display final score (e.g., using a Toast message)
        Toast.makeText(MainActivity.this, "Quiz Finished! Final Score: " + score + "/" + Questions.length, Toast.LENGTH_LONG).show();

        // Pass the score to the next activity
        Intent resultactivityredirect = new Intent(MainActivity.this, FinalResult.class);
        resultactivityredirect.putExtra("highscore", highScore);
        resultactivityredirect.putExtra("score", score);
        startActivity(resultactivityredirect);
    }


    private void restartQuiz() {
        // Reset all variables
        currentQuestion = 0;
        score = 0;
        correctCount = 0;
        wrongCount = 0;
        answeredQuestions = new boolean[Questions.length];  // Reset answered questions
        selectedAnswers = new int[Questions.length];  // Reset selected answers

        // Update UI
        updateQuestion();
        Score.setText("Score: " + score);
        Correct.setText("Correct: " + correctCount);
        Wrong.setText("Wrong: " + wrongCount);

        // Re-enable options and buttons
        enableOptions();
        Submit.setEnabled(true);
        Next.setEnabled(false);
    }
}
