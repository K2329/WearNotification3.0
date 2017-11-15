package fi.jamk.wearnotification;

import android.support.wear.widget.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by osmel on 15.11.2017.
 */

public class CustomRecyclerAdapter extends WearableRecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private static final String TAG = "CustomRecyclerAdapter";

    private String[] mDataSet;
    private Controller mController;

    public static class ViewHolder extends WearableRecyclerView.ViewHolder {

        private final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);
        }

        @Override
        public String toString() { return (String) mTextView.getText(); }
    }

    public CustomRecyclerAdapter(String[] dataSet, Controller controller) {
        mDataSet = dataSet;
        mController = controller;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mController.itemSelected(mDataSet[position]);
            }
        });

        viewHolder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}