package anjali.learning.practical14;// In FinalResult Activity
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FinalResult extends AppCompatActivity {
private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result); // Replace with your actual layout file

        // Get the score passed through the Intent
        int score = getIntent().getIntExtra("score", 0); // Default value is 0 if no score is passed
        int highscore=getIntent().getIntExtra("highscore",0);
        back=findViewById(R.id.btnBack);
        // Find the TextView and set the score
        TextView finalScoreText = findViewById(R.id.finalScoreText);
        TextView highscoretext=findViewById(R.id.highScoreText);
        finalScoreText.setText("Your Score: " + score + "/10"); // Adjust this to match your score system
        highscoretext.setText("High Score "+highscore+"/10");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnredirect=new Intent(FinalResult.this,StartActivity.class);
                startActivity(btnredirect);
            }
        });
    }
}
