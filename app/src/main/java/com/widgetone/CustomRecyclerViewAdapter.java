package com.widgetone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Item> arrayList = new ArrayList<>();
    int pos;
    private OnItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView strokeView;

        public MyViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            strokeView = v.findViewById(R.id.stroke_view);
            textView = v.findViewById(R.id.list_box);
        }


        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomRecyclerViewAdapter(Context context, ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_box_layout, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element



        final Item currentItem = arrayList.get(position);


        int colour = currentItem.getBackgroundColour();
        int visibiility = currentItem.getVisibility();


          holder.textView.setBackgroundColor(colour);
          holder.strokeView.setVisibility(visibiility);


    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

}




