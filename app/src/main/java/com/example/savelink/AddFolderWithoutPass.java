package com.example.savelink;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddFolderWithoutPass extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{
    TextInputEditText text;
    Button btn;
    SQLiteDatabase db;
    String name_folder;
    ArrayList names;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder_without_pass);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(getDrawable(R.drawable.bookmark1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null);

        btn = findViewById(R.id.button_without_password);
        btn.setOnClickListener(this);

        text = findViewById(R.id.FolderName);
        text.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            hideKeyboard(v);
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        try {
            names = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM folders", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                names.add(name);
                cursor.moveToNext();
            }
            cursor.close();

            name_folder = text.getText().toString().trim();
            if(names.contains(name_folder)) {
                Toast.makeText(AddFolderWithoutPass.this, "This name exiting", Toast.LENGTH_LONG).show();
            }else if (!name_folder.isEmpty()) {
                Toast.makeText(AddFolderWithoutPass.this, "Folder added", Toast.LENGTH_LONG).show();
                String sql = String.format("INSERT INTO folders (name, password, count) VALUES( '%s', '', 0)", name_folder);
                db.execSQL(sql);
                finish();
            }else{
                Toast.makeText(AddFolderWithoutPass.this, "Field can't be empty", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(AddFolderWithoutPass.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
