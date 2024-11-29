package anjali.learning.practical14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.ViewHolder> {

    private List<QuizAttempt> quizAttempts;

    public QuizHistoryAdapter(List<QuizAttempt> quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false); // Using a single TextView layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizAttempt attempt = quizAttempts.get(position);
        int attemptNumber = position + 1; // Add 1 to make it human-readable
        holder.attemptTextView.setText("Attempt " + attemptNumber + ": Score " + attempt.getScore());
    }

    @Override
    public int getItemCount() {
        return quizAttempts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView attemptTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            attemptTextView = itemView.findViewById(android.R.id.text1); // TextView for the single list item
        }
    }
}
