package com.example.savelink;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DialogDeleteItem extends DialogFragment implements View.OnClickListener{
    SQLiteDatabase db;
    Items activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (Items) getActivity();

        View v = inflater.inflate(R.layout.fragment_dialog_delete_item, null);

        db = activity.db;

        v.findViewById(R.id.cancel).setOnClickListener(this);
        v.findViewById(R.id.delete_item).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == v.findViewById(R.id.cancel)){
            activity.renderScreen(activity.search);
            dismiss();
        }else if(v == v.findViewById(R.id.delete_item)){
            String DELETE_ITEM = String.format("DELETE FROM urls WHERE id = '%s'", activity.items.get(activity.item).id);
            db.execSQL(DELETE_ITEM);
            activity.renderScreen(activity.search);
            Toast.makeText(getActivity(), "URL deleted", Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
}
