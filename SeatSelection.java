package com.example.project;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SeatSelection extends AppCompatActivity {

    Button btncardpay;
    private Button btnRFID;
    private DBHelper DB;
    private String username;
    private String busId, from, to, date;
    private EditText editTextTextPersonName, add_money_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        btncardpay = findViewById(R.id.btncardpay);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        add_money_input = findViewById(R.id.add_money_input);
        btnRFID = findViewById(R.id.btnrfid);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        if (username == null) {
            Log.e("SeatSelection", "Username is null");
            // Handle the error (e.g., show a message to the user or redirect to login)
            finish(); // Close the activity or redirect to another activity
            return;
        }


        
        btncardpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeatSelection.this, payment_activity.class);
                startActivity(i);
            }
        });

        btnRFID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seatNumber = editTextTextPersonName.getText().toString();
                String addMoney = add_money_input.getText().toString();

                if (seatNumber.isEmpty() || addMoney.isEmpty()) {
                    Toast.makeText(SeatSelection.this, "Please enter both seat number and money", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("SeatSelection", "RFID button clicked. Username: " + username);
                // Generate a new customer ID

                // Get user data
                Cursor cursor = DB.getUserData(username);
                String customerId = null;
                if (cursor.moveToFirst()) {
                    customerId = cursor.getString(cursor.getColumnIndex("customer_id"));
                }
                cursor.close();

                // If no customer ID exists, generate a new one
                if (customerId == null || customerId.isEmpty()) {
                    customerId = DB.generateCustomerId();
                    DB.updateCustomerId(username, customerId);
                }
                cursor = DB.getUserData(username);
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndex("fullname"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));

                    Intent intentFromList = getIntent();
                    busId = intentFromList.getStringExtra("BUS_ID");
                    from = intentFromList.getStringExtra("FROM");
                    to = intentFromList.getStringExtra("TO");
                    date = intentFromList.getStringExtra("DATE");

                    // Pass data to RFIDActivity
                    Intent intent = new Intent(SeatSelection.this, RFIDActivity.class);
                    intent.putExtra("NAME", name);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("CUSTOMER_ID", customerId);
                    intent.putExtra("BUS_ID", busId);
                    intent.putExtra("FROM", from);
                    intent.putExtra("TO", to);
                    intent.putExtra("DATE", date);
                    intent.putExtra("SEAT_NUMBER", seatNumber);
                    intent.putExtra("ADD_MONEY", addMoney);
                    startActivity(intent);
                }else {
                    Log.e("SeatSelection", "No user data found for username: " + username);
                }
                cursor.close();
            }
        });

    }

    public void cardpay(View view) {
        Intent intent = new Intent(SeatSelection.this, payment_activity.class);
        startActivity(intent);
    }
}
