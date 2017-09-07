package com.example.battleships;

<<<<<<< HEAD
=======
import android.content.Intent;
>>>>>>> origin/master
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
<<<<<<< HEAD
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hmaug on 03/04/2017.
 */

public class ListDataActivity  extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

=======
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ListDataActivity  extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
>>>>>>> origin/master
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);
<<<<<<< HEAD
        mListView = (ListView) findViewById(R.id.ListView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

=======
        mListView = (ListView) findViewById(R.id.Highscores);
        mDatabaseHelper = new DatabaseHelper(this);

        setButtonOnClickListeners(); //  initialises the button listeners.
        populateListView();
    }

    private void setButtonOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDataActivity.this, menu.class); // initialises intent.
                finish(); // ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.returnButton).setOnClickListener(listener); // initialises listener.
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListDataActivity.this, menu.class);  // initialises intent.
        finish(); // ends activity.
        startActivity(intent); // starts intent.
    }

>>>>>>> origin/master
    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        mListView.setAdapter(adapter);
    }
<<<<<<< HEAD
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


=======
>>>>>>> origin/master
}
