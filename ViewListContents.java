package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewListContents extends AppCompatActivity {

    DBHelper myDB;
    User user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlistcontents_layout);

        ListView listView = (ListView) findViewById(R.id.listView);
        myDB = new DBHelper(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.viewbuses();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()) {

                String busId = data.getString(0);
                String from = data.getString(1);
                String to = data.getString(2);
                String date = data.getString(3);
                String seats = data.getString(4);

                theList.add("ID: " + busId + "    FROM: " + from + "    TO: " + to + "    DATE: " + date + "    SEATS: " + seats);
                theList.add("");


                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String busDetails = theList.get(position);
                        String[] busDetailsArray = busDetails.split("\\s+");

                        // Extract details from the split array
                        String busId = busDetailsArray[1];
                        String from = busDetailsArray[3];
                        String to = busDetailsArray[5];
                        String date = busDetailsArray[7];
                        if (position % 2 == 0) {
                            Intent intent = new Intent(view.getContext(), SeatSelection.class);
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("BUS_ID", busId);
                            intent.putExtra("FROM", from);
                            intent.putExtra("TO", to);
                            intent.putExtra("DATE", date);
                            intent.putExtra("USERNAME", username);
                            startActivity(intent);
                        }
                    }
                });
            }
        }


    }
}