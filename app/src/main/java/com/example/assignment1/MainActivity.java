package com.example.assignment1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {
    Button[] arrayOfButtons;
    TextView numberInput;
    private String text;
    private int length = 0;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String ENTERED_PHONE_NUMBER = "PHONE_NUM";
    /**
     * a function used to make a call
     */
    public void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        length = numberInput.getText().length();
        if(length > 0) {
            intent.setData(Uri.parse("tel:"+numberInput.getText().toString()));
            startActivity(intent);
        }
    }

    /**
     * used to complete actions after permissions are requested and accepted
     * @param requestCode the request code
     * @param permissions an array of permissions
     * @param grantResults an array of grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if ((grantResults.length > 0 )&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    System.out.println("Error: Permission not accepted.");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * used to save the state of the phone number when the screen is rotated
     * @param state the current state
     */
    @Override
    protected void onSaveInstanceState(Bundle state){
        if(!numberInput.getText().toString().equals("")) {
            state.putString(ENTERED_PHONE_NUMBER, numberInput.getText().toString());
        }
    }

    /**
     * called when the app is created
     * @param savedInstanceState the current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberInput = findViewById(R.id.numberInput);
        if(savedInstanceState != null){ // if a phone number was entered previously, remember it
            numberInput.setText(savedInstanceState.getString(ENTERED_PHONE_NUMBER));
        }
        arrayOfButtons = new Button[14];
        for(int i =0; i<arrayOfButtons.length; i++){ //find all the buttons
            String buttonID = "button"+i;
            int id = getResources().getIdentifier(buttonID, "id", getPackageName());
            arrayOfButtons[i] = findViewById(id);
        }
        for(int i =0; i<10; i++){ //set on click listeners for the digits
            final int num =i;
            arrayOfButtons[i].setOnClickListener(v ->numberInput.append(Integer.toString(num)));
        }
        //set on click listener for the delete button
        arrayOfButtons[10].setOnClickListener(view ->{
            text = numberInput.getText().toString();
            length = numberInput.getText().length();
            if(length >0) {
                numberInput.setText(text.substring(0, text.length() - 1));
            }
        });
        //set on click listeners for the special characters
        arrayOfButtons[11].setOnClickListener(v -> numberInput.append("*"));
        arrayOfButtons[12].setOnClickListener(v -> numberInput.append("#"));
        //set on click listener for the call button
        arrayOfButtons[13].setOnClickListener(v -> {
           if((Build.VERSION.SDK_INT >=23)&&(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)){
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }else{
               makeCall();
           }
        });
    }
}
