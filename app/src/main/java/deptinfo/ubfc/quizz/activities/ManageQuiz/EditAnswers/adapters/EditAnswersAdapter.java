package deptinfo.ubfc.quizz.activities.ManageQuiz.EditAnswers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditAnswers.EditAnswerActivity;
import deptinfo.ubfc.quizz.activities.ManageQuiz.EditQuestions.EditQuestionActivity;
import deptinfo.ubfc.quizz.helpers.DatabaseHelper;


public class EditAnswersAdapter extends RecyclerView.Adapter<EditAnswersAdapter.ViewHolder> {
    private List<String> answers;
    Context ctx ;
    private static final String TAG = "EditQuestionsAdapter";
    private DatabaseHelper databaseHelper;
    private String question ;
    private long answerId ;

    public void add(int position, String item) {
        answers.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        answers.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EditAnswersAdapter(Context context, List<String> myDataset , String question) {
        answers = myDataset;
        ctx = context ;
        this.question = question;
        databaseHelper = new DatabaseHelper(ctx);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EditAnswersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = answers.get(position);

        holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
            }
        });

        holder.txtHeader.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                //quizType = databaseHelper.getQuizForQuestion(answers.get(position));


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ctx);
                LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = li.inflate(R.layout.dialog_layout, null);
                TextView title = mView.findViewById(R.id.dialog_title);
                title.setText("Edit Answer");
                final TextView txt = mView.findViewById(R.id.dialog_txt);
                txt.setHint(answers.get(position));
                Button btnValid = (Button) mView.findViewById(R.id.dialog_btnValid);
                Button btnCancel = (Button) mView.findViewById(R.id.dialog_btnCancel);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                // Onclick valid
                btnValid.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!txt.getText().toString().isEmpty()){
                            answerId = databaseHelper.getIdForAnswer(answers.get(position) , question );

                            databaseHelper.updateAnswer(answerId , txt.getText().toString());
                            Toast.makeText(ctx, R.string.success_dialog_msg, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            //Reload view after updating DB
                            Intent intent = new Intent(ctx, EditAnswerActivity.class);
                            intent.putExtra("Question", question);
                            ctx.startActivity(intent);
                        }else{
                            Toast.makeText(ctx, R.string.success_dialog_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Onclick Cancel
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return true;
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

}
