package deptinfo.ubfc.quizz.activities.Play.SingleQuestion.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.Play.Result.ResultActivity;
import deptinfo.ubfc.quizz.activities.Play.SingleQuestion.SingleQuestionActivity;

public class SingleQuestionAdapter extends RecyclerView.Adapter<SingleQuestionAdapter.ViewHolder> {
    private List<String> answers;
    private List<String> questions;
    int correctAnswerDB;
    int nbCorrectAnswers;

    Context ctx ;
    private static final String TAG = "SingleQuestAdapter";
    int questionNumber;

    public void add(int position, String item) {
        answers.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        answers.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of answers)
    public SingleQuestionAdapter(Context context, List<String> answers , int questionNumber, List<String> questions, int correctAnswerDB, int nbCorrectAnswers) {
        this.answers = answers;
        this.ctx = context ;
        this.questionNumber = questionNumber;
        this.questions = questions;
        this.correctAnswerDB = correctAnswerDB;
        this.nbCorrectAnswers = nbCorrectAnswers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SingleQuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SingleQuestionAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = answers.get(position);
        holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                correctAnswerDB = correctAnswerDB - 1;

                if(position == correctAnswerDB){
                    correctAnswered();
                    Toast.makeText(ctx, "Correct Answer", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(ctx, "Wrong Answer", Toast.LENGTH_LONG).show();

                if(questionNumber <  questions.size() - 1){
                    Intent intent = new Intent(ctx, SingleQuestionActivity.class);
                    intent.putStringArrayListExtra("questions", (ArrayList<String>)questions);
                    intent.putExtra("questionStart", questionNumber + 1);
                    intent.putExtra("currentResult", getNbCorrectAnswers());
                    ctx.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(ctx, ResultActivity.class);
                    intent.putExtra("lastQuestion", questions.get(questionNumber));//send last question
                    intent.putExtra("currentResult" , getNbCorrectAnswers());//send current result
                    ctx.startActivity(intent);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answers.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
        }
    }

    public void correctAnswered(){
        this.nbCorrectAnswers = this.nbCorrectAnswers + 1;
    }

    public int getNbCorrectAnswers() {
        return this.nbCorrectAnswers;
    }
}
