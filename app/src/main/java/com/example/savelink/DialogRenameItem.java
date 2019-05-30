package com.example.savelink;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DialogRenameItem extends DialogFragment implements View.OnClickListener{
    Intent intent;
    String new_name;
    TextView textview;
    Button cancel, rename;
    TextInputEditText text;
    SQLiteDatabase db;
    Items activity;
    ArrayList names;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (Items) getActivity();

        View v = inflater.inflate(R.layout.fragment_dialog_rename_item, null);

        textview = v.findViewById(R.id.url_name);
        textview.setText(activity.getName());

        db = activity.db;

        activity.renderScreen(activity.search);

        text = v.findViewById(R.id.new_name_url);

        v.findViewById(R.id.cancel_url).setOnClickListener(this);
        v.findViewById(R.id.rename_url).setOnClickListener(this);

        return v;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        text.setText("");
        activity.renderScreen(activity.search);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        text.setText("");
        activity.renderScreen(activity.search);

    }

    @Override
    public void onClick(View v) {
        new_name = text.getText().toString().trim();
        String CHANGE_NAME = String.format("UPDATE urls SET name='%s' WHERE name='%s'", new_name, textview.getText().toString());
        if(v == v.findViewById(R.id.cancel_url)){
            dismiss();
        }else if(v == v.findViewById(R.id.rename_url)){
            names = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM urls", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                names.add(name);
                cursor.moveToNext();
            }
            cursor.close();

            if(names.contains(new_name)) {
                Toast.makeText(getActivity(), "This name exiting", Toast.LENGTH_LONG).show();
                text.setText("");
            }else if (!new_name.isEmpty()) {
                db.execSQL(CHANGE_NAME);
                Toast.makeText(getActivity(), "Name changed", Toast.LENGTH_LONG).show();
                dismiss();
            }else{
                Toast.makeText(getActivity(), "Fields can't be empty", Toast.LENGTH_LONG).show();
                text.setText("");
            }

        }
    }
}
