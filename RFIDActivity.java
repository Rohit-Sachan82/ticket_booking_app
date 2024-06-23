package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class RFIDActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, customerIdTextView, busDetailsTextView, seatNumberTextView, addMoneyTextView;
    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        customerIdTextView = findViewById(R.id.customerIdTextView);
        busDetailsTextView = findViewById(R.id.busDetailsTextView);
        seatNumberTextView = findViewById(R.id.seatNumberTextView);
        addMoneyTextView = findViewById(R.id.addMoneyTextView);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        // Retrieve customer details from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String email = intent.getStringExtra("EMAIL");
        String customerId = intent.getStringExtra("CUSTOMER_ID");
        String busId = intent.getStringExtra("BUS_ID");
        String from = intent.getStringExtra("FROM");
        String to = intent.getStringExtra("TO");
        String date = intent.getStringExtra("DATE");
        String seatNumber = intent.getStringExtra("SEAT_NUMBER");
        String addMoney = intent.getStringExtra("ADD_MONEY");

        if (name != null && email != null && customerId != null && busId != null && from != null && to != null && date != null) {
            // Display customer details
            nameTextView.setText("Name: " + name);
            emailTextView.setText("Email: " + email);
            customerIdTextView.setText("RFID NO: " + customerId);
            seatNumberTextView.setText("Seat Number: " + seatNumber);
            addMoneyTextView.setText("Available Amount: " + addMoney);
            busDetailsTextView.setText("Bus ID: " + busId + "\nFrom: " + from + "\nTo: " + to + "\nDate: " + date);
            Log.d("RFIDActivity", "Received data - Name: " + name + ", Email: " + email + ", RFID NO: " + customerId+ ", Seat Number: " + seatNumber + ", Added Money: " + addMoney);
            // Generate QR code
            String qrData = generateQrData(name, email, customerId, seatNumber, addMoney, busId, from, to, date, "Confirmed");
            generateQrCode(qrData);
        } else {
            Log.e("RFIDActivity", "Intent data is missing");
        }

    }
    private String generateQrData(String name, String email, String customerId, String seatNumber, String addMoney, String busId, String fromLocation, String toLocation, String journeyDate, String ticketStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("customerId", customerId);
            jsonObject.put("seatNumber", seatNumber);
            jsonObject.put("addMoney", addMoney);
            jsonObject.put("busId", busId);
            jsonObject.put("fromLocation", fromLocation);
            jsonObject.put("toLocation", toLocation);
            jsonObject.put("journeyDate", journeyDate);
            jsonObject.put("ticketStatus", ticketStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void generateQrCode(String customerId) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(customerId, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("RFIDActivity", "QR code generation failed", e);
        }
    }
}
