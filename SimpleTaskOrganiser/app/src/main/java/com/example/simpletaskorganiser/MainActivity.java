package com.example.simpletaskorganiser;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * MainActivity class
 * Created by Glynn Bolton on 03/03/2019.
 * Copyright (c) 2019 Glynn Bolton. All rights reserved.
 */
public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_TASK = "com.example.android.simpletaskorganiser.task";


    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter myCursorAdapter;

    /**
     * Performs initialization of all fragments and loaders.
     *
     * @param savedInstanceState if the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     * @see #onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TaskDisplayActivity.class);
                intent.putExtra("id", -1);
                startActivityForResult(intent, 1);
            }
        });

        ListView tasksListView;
        tasksListView = findViewById(R.id.listView_tasks);
        Cursor cursor = dbHelper.getAllTasks();
        String[] fromFieldNames = dbHelper.getAllColumns();
        int[] toViewIDs = new int[]{R.id.textView_id, R.id.textView_description,};
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs);
        tasksListView.setAdapter(myCursorAdapter);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent        The AdapterView where the click happened.
             * @param viewClicked   The view within the AdapterView that was clicked (this
             *                      will be a view provided by the adapter)
             * @param position      The position of the view in the adapter.
             * @param id            The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), TaskDisplayActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("description", dbHelper.getTask(id).getString(1));
                startActivityForResult(intent, 2);
            }
        });
        tasksListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            /**
             * Callback method to be invoked when an item in this view has been
             * clicked and held.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need to access
             * the data associated with the selected item.
             *
             * @param parent   The AbsListView where the click happened
             * @param view     The view within the AbsListView that was clicked
             * @param position The position of the view in the list
             * @param id       The row id of the item that was clicked
             * @return true if the callback consumed the long click, false otherwise
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.deleteTask(id);
                Cursor cursor = dbHelper.getAllTasks();
                myCursorAdapter.swapCursor(cursor);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = dbHelper.getAllTasks();
        myCursorAdapter.swapCursor(cursor);
    }

    /**
     * Responds to results returns from dialog forms.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra("id");
                String description = data.getStringExtra("description");
                dbHelper.insertTask(description);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra("id");
                String description = data.getStringExtra("description");
                dbHelper.updateTask(id, description);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
