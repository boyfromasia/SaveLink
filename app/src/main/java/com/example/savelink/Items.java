package com.example.savelink;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import java.util.ArrayList;

public class Items extends AppCompatActivity implements ItemRecyclerViewAdapter.OnNoteListener{

    private static final String TAG = "MainActivity";
    DialogFragment dlg_rename, dlg_delete, dlg_contacts;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    SQLiteDatabase db;
    Integer item;
    Toolbar toolbar;
    String item_name, session_id, count_now, field, result, search;
    Object clipboardService;
    ClipboardManager clipboardManager;
    ArrayList<Item> items = new ArrayList<>();
    private long mTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.lvItem);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(getDrawable(R.drawable.bookmark1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dlg_rename = new DialogRenameItem();
        dlg_delete = new DialogDeleteItem();
        dlg_contacts = new DialogAboutMe();

        field = "id";
        result = "asc";
        search = "";

        clipboardService = getSystemService(CLIPBOARD_SERVICE);
        clipboardManager = (ClipboardManager) clipboardService;

        db = openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null);

        fab =  findViewById(R.id.add_item);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click("add_url");
            }
        });

        renderScreen(search);

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = newText;
                renderScreen(search);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onNoteClick(int position, boolean LongClick) {
        try {
            if(!LongClick) {
                Integer num;
                String WEB = items.get(position).url;
                count_now = items.get(position).count;
                num = Integer.parseInt(count_now) + 1;
                String SQL = String.format("UPDATE urls SET count = %s WHERE id ='%s'", num, items.get(position).id);
                db.execSQL(SQL);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEB));
                startActivity(intent);
            }else if(LongClick){
                Toast.makeText(this, "URL has been copied to system clipboard", Toast.LENGTH_LONG).show();
                ClipData clipData = ClipData.newPlainText("Source text", items.get(position).url);
                clipboardManager.setPrimaryClip(clipData);
            }
        }catch (Exception e){
            Toast.makeText(Items.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Log.d(TAG, "onSwiped: called");
            if(i == ItemTouchHelper.RIGHT) {
                try {
                    item = viewHolder.getAdapterPosition();
                    dlg_delete.show(getSupportFragmentManager(), "dlg_delete_url");
                }catch (Exception e){
                    Toast.makeText(Items.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            if(i == ItemTouchHelper.LEFT){
                item_name = items.get(viewHolder.getAdapterPosition()).name;
                dlg_rename.show(getSupportFragmentManager(), "dlg_rename_url");
            }
            renderScreen(search);
        }
    };


    public String getName(){
        return item_name;
    }

    public void Click(String name){
        if (SystemClock.elapsedRealtime() - mTime < 1000){
            return;
        }

        mTime = SystemClock.elapsedRealtime();

        switch(name){
            case "add_url": addUrl();
        }

    }

    private void addUrl() {
        Intent intent = new Intent(this, AddItem.class);
        intent.putExtra("id", session_id);
        startActivity(intent);
    }

    public void renderScreen(String search){
        try{
            items = new ArrayList<>();
            session_id = getIntent().getStringExtra("SESSION_ID");
            String get_item = String.format("SELECT * FROM urls WHERE folder = '%s'", session_id);
            get_item = get_item + " AND name LIKE '%" + search + "%'" + " ORDER BY " + field + " " + result;
            Cursor cursor = db.rawQuery(get_item, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String folder = cursor.getString(0);
                String name = cursor.getString(1);
                String url = cursor.getString(2);
                String count = cursor.getString(3);
                String id = cursor.getString(4);
                Item url_item = new Item(folder, name, url, count, id);
                items.add(url_item);
                cursor.moveToNext();
            }
            cursor.close();
            ItemRecyclerViewAdapter adapterWithPassword = new ItemRecyclerViewAdapter(this, items, this);
            recyclerView.setAdapter(adapterWithPassword);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch (Exception e) {
            Toast.makeText(Items.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu){
            showSortDialog();
            return true;
        }else if(id == R.id.contacts){
            dlg_contacts.show(getSupportFragmentManager(), "contacts");
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] options = {"A-Z", "Z-A", "least visited", "most visited", "last added", "previously added" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setIcon(R.drawable.ic_sort);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    field = "name";
                    result = "asc";
                }else if(which == 1){
                    field = "name";
                    result = "desc";
                }else if(which == 2){
                    field = "count";
                    result = "asc";
                }else if(which == 3){
                    field = "count";
                    result = "desc";
                }else if(which == 4){
                    field = "id";
                    result = "asc";
                }else if(which == 5) {
                    field = "id";
                    result = "desc";
                }
                renderScreen(search);
            }
        });
        builder.create().show();
    }

    protected void onResume() {
        super.onResume();
        renderScreen("");
    }

}
