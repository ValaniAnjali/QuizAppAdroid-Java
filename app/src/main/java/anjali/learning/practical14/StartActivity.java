package anjali.learning.practical14;

import static kotlinx.coroutines.android.HandlerDispatcherKt.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {
private Button StartQuiz,History;
    private TextView totalQuizText, attemptedText, highScoreText;
    private QuizDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        StartQuiz=findViewById(R.id.startquiz);
        History=findViewById(R.id.history);
        dbHelper = new QuizDatabaseHelper(this);

        // Initialize views
        totalQuizText = findViewById(R.id.totalquiz);
        attemptedText = findViewById(R.id.attempted);
        highScoreText = findViewById(R.id.highscore);

        // Fetch the data from the database
        int attemptedCount = dbHelper.getAttemptedCount();
        int highScore = dbHelper.getHighestScore();
        totalQuizText.setText("Total Max Quiz: 20"); // You can customize the max quiz count if needed
        attemptedText.setText("Attempted: " + attemptedCount);
        highScoreText.setText("High Score: " + highScore);

        SharedPreferences sharedPreferences = getSharedPreferences("QuizPreferences", MODE_PRIVATE);
        highScore = sharedPreferences.getInt("high_score", 0);

        // Display the high score
        highScoreText.setText("High Score: " + highScore + "/" + "10");
        StartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(StartActivity.this,MainActivity.class);
                startActivity(i1);
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(StartActivity.this, HistoryLayout.class);
                startActivity(i2);
            }
        });
    }
}