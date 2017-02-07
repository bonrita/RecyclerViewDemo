package com.bonrita.recyclerviewdemo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    /**
     * Calling activity instance.
     */
    private RecyclerAdapter.AdapterClickListener mAdapterClickListener;

    public RecyclerAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public void setAdapterClickListener(RecyclerAdapter.AdapterClickListener adapterClickListener) {
        this.mAdapterClickListener = adapterClickListener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        // Get a list item at the current position.
        final ListItem item = this.listItems.get(position);

        holder.heading.setText(item.getHeading());
        holder.description.setText(item.getDescription());
        Picasso.with(context).load(item.getImgaeUrl()).resize(200, 200).centerCrop().into(holder.imageView);

        // Click on list_container.
        holder.listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Clicked on '" + item.getHeading() + "'", Toast.LENGTH_LONG).show();

                if(null != mAdapterClickListener) {
mAdapterClickListener.itemClicked(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.listItems.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView heading;
        private TextView description;
        private ImageView imageView;
        private LinearLayout listContainer;
//        private View listView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            heading = (TextView) itemView.findViewById(R.id.heading);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.image_bk);
            listContainer = (LinearLayout) itemView.findViewById(R.id.list_container);
//            listView = itemView;
        }
    }

    /**
     * Interface to be implemented by the calling activity to act upon the clicked item.
     */
    interface AdapterClickListener {
        void itemClicked(View v, int position);
    }

}
