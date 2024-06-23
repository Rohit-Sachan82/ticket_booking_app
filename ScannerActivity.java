package com.example.project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ScannerActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private CompoundBarcodeView barcodeView;
    private DBHelper DB;
    private TextView nameTextView, emailTextView, customerIdTextView, busDetailsTextView, seatNumberTextView, addMoneyTextView, ticketStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        barcodeView = findViewById(R.id.barcode_scanner);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        customerIdTextView = findViewById(R.id.customerIdTextView);
        busDetailsTextView = findViewById(R.id.busDetailsTextView);
        seatNumberTextView = findViewById(R.id.seatNumberTextView);
        addMoneyTextView = findViewById(R.id.addMoneyTextView);
        ticketStatusTextView = findViewById(R.id.ticketStatusTextView);

        DB = new DBHelper(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initializeScanner();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void initializeScanner() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                handleScannedResult(result.getText());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Handle result points if needed
            }
        });
    }

    private void handleScannedResult(String scannedText) {
        // Assuming scannedText contains the customer ID
        try {
            JSONObject jsonObject = new JSONObject(scannedText);
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String customerId = jsonObject.getString("customerId");
            String busId = jsonObject.getString("busId");
            String fromLocation = jsonObject.getString("fromLocation");
            String toLocation = jsonObject.getString("toLocation");
            String journeyDate = jsonObject.getString("journeyDate");
            String seatNumber = jsonObject.getString("seatNumber");
            String addMoney = jsonObject.getString("addMoney");
            String ticketStatus = jsonObject.getString("ticketStatus");

            // Display customer details
            nameTextView.setText("Name: " + name);
            emailTextView.setText("Email: " + email);
            customerIdTextView.setText("Customer ID: " + customerId);
            busDetailsTextView.setText("Bus ID: " + busId + "\nFrom: " + fromLocation + "\nTo: " + toLocation + "\nDate: " + journeyDate);
            seatNumberTextView.setText("Seat Number: " + seatNumber);
            addMoneyTextView.setText("Available Amount: " + addMoney);
            ticketStatusTextView.setText("Ticket Status: " + ticketStatus);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ScannerActivity.this, "Invalid QR Code", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to use the scanner", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
