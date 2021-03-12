package com.example.simpletaskorganiser;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * TaskDisplayActivity class
 * Created by Glynn Bolton on 03/03/2019.
 * Copyright (c) 2019 Glynn Bolton. All rights reserved.
 */
public class TaskDisplayActivity extends AppCompatActivity {

    EditText descriptionTextEdit;
    String id;
    String description;

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
        setContentView(R.layout.activity_task_display);
        descriptionTextEdit = findViewById(R.id.editText_description);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        description = intent.getStringExtra("description");
        descriptionTextEdit.setText(description);
    }

    /**
     * Closes dialog activity and returns task id and description.
     *
     * @param view view calling method.
     */
    public void ButtonSubmitOnClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("id", id);
        returnIntent.putExtra("description", descriptionTextEdit.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
