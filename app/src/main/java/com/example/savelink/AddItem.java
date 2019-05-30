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


public class AddItem extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{
    TextInputEditText text, text_url;
    Button btn;
    SQLiteDatabase db;
    String name_item, url, session_id;
    ArrayList names;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(getDrawable(R.drawable.bookmark1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null);

        btn = findViewById(R.id.button_item);
        btn.setOnClickListener(this);

        text = findViewById(R.id.linkname);
        text_url = findViewById(R.id.url);
        text.setOnFocusChangeListener(this);
        text_url.setOnFocusChangeListener(this);
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
            session_id = getIntent().getStringExtra("id");
            String SQL = String.format("SELECT * FROM urls WHERE folder = '%s'", session_id);

            names = new ArrayList<>();
            Cursor cursor = db.rawQuery(SQL, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                names.add(name);
                cursor.moveToNext();
            }
            cursor.close();

            name_item = text.getText().toString().trim();
            url = text_url.getText().toString().trim();
            if(names.contains(name_item)) {
                Toast.makeText(AddItem.this, "This name exiting", Toast.LENGTH_LONG).show();
            }else if (!name_item.isEmpty() && !url.isEmpty()) {
                Toast.makeText(AddItem.this, "Folder added", Toast.LENGTH_LONG).show();
                if(url.length() < 7){
                    url = "http://" + url;
                }else if(!url.substring(0, 7).equals("http://")){
                    url = "http://" + url;
                }
                String sql = String.format("INSERT INTO urls (folder, name, url, count) VALUES('%s', '%s', '%s', '0')",session_id, name_item, url);
                db.execSQL(sql);
                finish();
            }else{
                Toast.makeText(AddItem.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(AddItem.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}