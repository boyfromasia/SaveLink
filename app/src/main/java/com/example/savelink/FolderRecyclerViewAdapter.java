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

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Folder> folders = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public FolderRecyclerViewAdapter(Context context, ArrayList<Folder> folders, OnNoteListener onNoteListener) {
        this.context = context;
        this.folders = folders;
        this.mOnNoteListener = onNoteListener;
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView folderImage;
        TextView text_for_folder;
        OnNoteListener mOnNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            folderImage = itemView.findViewById(R.id.folderImage);
            text_for_folder = itemView.findViewById(R.id.text_for_folder);
            mOnNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_for_recycleview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Folder folder = folders.get(i);
        viewHolder.text_for_folder.setText(folder.getName());
        if(folder.password.trim().isEmpty()) {
            viewHolder.folderImage.setImageResource(R.drawable.folder);
        }else{
            viewHolder.folderImage.setImageResource(R.drawable.folder_password);
        }
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
