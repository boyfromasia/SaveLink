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


public class AddFolderPassword extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{
    TextInputEditText text_password, text, text_password_repeat;
    Button btn;
    SQLiteDatabase db;
    String name_folder, password, password_repeat;
    ArrayList names;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder_password);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(getDrawable(R.drawable.bookmark1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null);

        btn = findViewById(R.id.button_password);
        btn.setOnClickListener(this);

        text = findViewById(R.id.FolderNamePass);
        text_password = findViewById(R.id.Password);
        text.setOnFocusChangeListener(this);
        text_password.setOnFocusChangeListener(this);
        text_password_repeat = findViewById(R.id.PasswordRepeat);
        text_password_repeat.setOnFocusChangeListener(this);
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
            password = text_password.getText().toString();
            password_repeat = text_password_repeat.getText().toString();

            if(names.contains(name_folder)) {
                Toast.makeText(AddFolderPassword.this, "This name exiting", Toast.LENGTH_LONG).show();
            }else if (!name_folder.isEmpty() && !password.isEmpty()) {
                if (password.equals(password_repeat)) {
                    Toast.makeText(AddFolderPassword.this, "Folder added", Toast.LENGTH_LONG).show();
                    String sql = String.format("INSERT INTO folders (name, password, count) VALUES( '%s',  '%s', 0)", name_folder, password);
                    db.execSQL(sql);
                    finish();
                }else {
                    Toast.makeText(AddFolderPassword.this, "Password do not match", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(AddFolderPassword.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(AddFolderPassword.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
