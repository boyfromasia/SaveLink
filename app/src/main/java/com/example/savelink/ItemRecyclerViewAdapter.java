package com.example.savelink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> items = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public ItemRecyclerViewAdapter(Context context, ArrayList<Item> items, OnNoteListener onNoteListener) {
        this.context = context;
        this.items = items;
        this.mOnNoteListener = onNoteListener;
    }

    public interface OnNoteListener{
        void onNoteClick(int position, boolean LongClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView urlImage;
        TextView text_for_url;
        OnNoteListener mOnNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            urlImage = itemView.findViewById(R.id.urlImage);
            text_for_url = itemView.findViewById(R.id.text_for_url);
            mOnNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnNoteListener.onNoteClick(getAdapterPosition(), true);
            return true;
        }

        @Override
        public void onClick(View v) {
            mOnNoteListener.onNoteClick(getAdapterPosition(), false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.url_for_recycleview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Item item = items.get(i);
        viewHolder.text_for_url.setText(item.getName());
        viewHolder.urlImage.setImageResource(R.drawable.url);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
