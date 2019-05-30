package com.example.savelink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FolderRecyclerViewAdapter.OnNoteListener{

    private static final String TAG = "MainActivity";
    DialogFragment dlg_rename, dlg_password, dlg_delete, contacts;
    RecyclerView recyclerView;
    FloatingActionButton fab, fab_1, fab_2;
    Animation fab_open, fab_close, fab_rotate_forward, fab_rotate_backward;
    SQLiteDatabase db;
    Toolbar toolbar;
    Intent item_intent;
    Object clipboardService;
    ClipboardManager clipboardManager;
    String item_name, password_now, count_now, result, field, search, flag;
    FolderRecyclerViewAdapter adapter;
    Integer folder;
    ArrayList<String> intro_flag = new ArrayList<>();
    ArrayList<Folder> folders = new ArrayList<>();
    private long mTime = 0;
    boolean is_open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = "ID";
        result = "ASC";

        recyclerView = findViewById(R.id.lvMain);

        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(getDrawable(R.drawable.bookmark1));

        ActionBar actionBar = getSupportActionBar();

        dlg_rename = new DialogRenameFolder();
        dlg_password = new DialogPassword();
        dlg_delete = new DialogDeleteFolder();
        contacts = new DialogAboutMe();

        db = openOrCreateDatabase("db.db", Context.MODE_PRIVATE, null);
        create_table_main();
        create_table_side();
        create_table_for_intro();

        fab =  findViewById(R.id.button_plus);
        fab_1 = findViewById(R.id.fab_1);
        fab_2 = findViewById(R.id.fab_2);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fab_rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        fab_rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        fab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click("fab_1");
            }
        });

        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click("fab_2");
            }
        });

        search = "";

        renderScreen(search);

        String SQL = "SELECT * FROM intro";
        Cursor cursor = db.rawQuery(SQL, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String record = cursor.getString(0);
            intro_flag.add(record);
            cursor.moveToNext();
        }
        cursor.close();

        if (intro_flag.size() == 0){
            add_flag();
            flag = "true";
        }else{
            flag = "false";
        }

        if (flag.equals("true")){
            Intent intent = new Intent(this, AppIntroActivity.class);
            startActivity(intent);
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu){
            showSortDialog();
            return true;
        } else if(id == R.id.contacts){
            showContacts();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContacts() {
        contacts.show(getSupportFragmentManager(), "contacts");
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

    @Override
    public void onNoteClick(int position) {
        Integer num;
        item_name = folders.get(position).name;
        password_now = folders.get(position).password;
        count_now = folders.get(position).count;
        item_intent = new Intent(this, Items.class);
        item_intent.putExtra("SESSION_ID", folders.get(position).id);
        num = Integer.parseInt(count_now) + 1;
        String SQL = String.format("UPDATE folders SET count= %s WHERE name='%s'", num, item_name);
        db.execSQL(SQL);
        if(password_now.isEmpty()){
            startActivity(item_intent);
        }else{
            dlg_password.show(getSupportFragmentManager(), "dlg_password");
        }
    }


    private ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if(i == ItemTouchHelper.RIGHT) {
                folder = viewHolder.getAdapterPosition();
                dlg_delete.show(getSupportFragmentManager(), "dlg_delete_folder");
            }
            if(i == ItemTouchHelper.LEFT){
                item_name = folders.get(viewHolder.getAdapterPosition()).name;
                dlg_rename.show(getSupportFragmentManager(), "dlg_rename");
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

        switch (name){
            case "fab_1": open_without_pass();
                break;
            case "fab_2": open_password();
                break;
            default: break;
        }

    }

    private void create_table_main() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS folders" +
                "(ID INTEGER primary key AUTOINCREMENT, " +
                "name TEXT, "+
                "password TEXT );";

        String SQL = "ALTER TABLE folders ADD COLUMN count INTEGER";

        db.execSQL(CREATE_TABLE);

        try {
            db.execSQL(SQL);
        }catch (Exception e){
            Log.d("FIELD", "field was created");
        }
    }

    private void create_table_side(){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS urls" +
                "( folder TEXT, " +
                "name TEXT, "+
                "url TEXT, " +
                "count INTEGER," +
                "id INTEGER primary key AUTOINCREMENT);";

        db.execSQL(CREATE_TABLE);

        String SQL = "ALTER TABLE urls ADD COLUMN id INTEGER primary key AUTOINCREMENT";

        try {
            db.execSQL(SQL);
        }catch (Exception e){
            Log.d("FIELD", "field was created");
        }

    }

    private void create_table_for_intro(){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS intro (flag TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    private void add_flag(){
        String ADD_FLAG = "INSERT INTO intro (flag) VALUES('true')";
        db.execSQL(ADD_FLAG);
    }

    private void open_password(){
        Intent intent = new Intent(this, AddFolderPassword.class);
        startActivity(intent);
    }

    private void open_without_pass(){
        Intent intent = new Intent(this, AddFolderWithoutPass.class);
        startActivity(intent);
    }

    private void animateFab()
    {
        if(is_open){
            fab.startAnimation(fab_rotate_forward);
            fab_1.startAnimation(fab_close);
            fab_2.startAnimation(fab_close);
            fab_1.setClickable(false);
            fab_2.setClickable(false);
            is_open = false;
        }else{
            fab.startAnimation(fab_rotate_backward);
            fab_1.startAnimation(fab_open);
            fab_2.startAnimation(fab_open);
            fab_1.setClickable(true);
            fab_2.setClickable(true);
            is_open = true;
        }

    }

    public void renderScreen(String search){
        try{
            folders = new ArrayList<>();
            String SQL = "SELECT * FROM FOLDERS WHERE name LIKE '%" + search + "%'" + "ORDER BY " + field + " " + result;
            Cursor cursor = db.rawQuery(SQL, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String password = cursor.getString(2);
                String count = cursor.getString(3);
                Folder folder = new Folder(id, name, password, count);
                folders.add(folder);
                cursor.moveToNext();
            }
            cursor.close();
            adapter = new FolderRecyclerViewAdapter(this, folders, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {
        super.onResume();
        renderScreen(search);
    }
}
