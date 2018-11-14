package deptinfo.ubfc.quizz.activities.Play.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import deptinfo.ubfc.quizz.R;
import deptinfo.ubfc.quizz.activities.Play.QuestionList.QuestionActivity;


public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.ViewHolder> {
    private List<String> quizTypes;
    Context ctx ;
    private static final String TAG = "PlayAdapter";

    public void add(int position, String item) {
        quizTypes.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        quizTypes.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlayAdapter(Context context, List<String> myDataset) {
        quizTypes = myDataset;
        ctx = context ;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        final String name = quizTypes.get(position);
        holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.d(TAG, "onClick: clicked on: " + quizTypes.get(position));

                    Toast.makeText(ctx, quizTypes.get(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ctx, QuestionActivity.class);
                    intent.putExtra("quizType", quizTypes.get(position));
                    ctx.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return quizTypes.size();
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
