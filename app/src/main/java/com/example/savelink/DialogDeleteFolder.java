package com.example.savelink;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DialogDeleteFolder extends DialogFragment implements View.OnClickListener{
    SQLiteDatabase db;
    MainActivity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.fragment_dialog_delete_folder, null);

        db = activity.db;

        v.findViewById(R.id.cancel).setOnClickListener(this);
        v.findViewById(R.id.delete_folder).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == v.findViewById(R.id.cancel)){
            activity.renderScreen(activity.search);
            dismiss();
        }else if(v == v.findViewById(R.id.delete_folder)){
            String DELETE_FOLDER = String.format("DELETE FROM folders WHERE name = '%s'", activity.folders.get(activity.folder).name);
            String DELETE_ITEM = String.format("DELETE FROM urls WHERE folder = '%s'", activity.folders.get(activity.folder).id);
            db.execSQL(DELETE_FOLDER);
            db.execSQL(DELETE_ITEM);
            activity.renderScreen(activity.search);
            Toast.makeText(getActivity(), "Folder deleted", Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
}
