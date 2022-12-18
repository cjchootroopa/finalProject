package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email, password , compassword, name;
    private Button mRegister;
    private TextView existaccount;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Create Account");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        email = findViewById(R.id.register_email);
        name = findViewById(R.id.register_name);
        password = findViewById(R.id.register_password);
        compassword = findViewById(R.id.comfirmpasswordlog);
        mRegister = findViewById(R.id.register_button);
        existaccount = findViewById(R.id.homepage);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register");

        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean error = false;
                String emaill = email.getText().toString().trim();
                String uname = name.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String conPass= compassword.getText().toString().trim();
                if (uname.isEmpty()) {
                    name.setError("Name could not be empty");
                    name.setFocusable(true);
                    error = true;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);
                    error = true;
                }
//                if (pass.length() < 6) {
//                    password.setError("Length Must be greater than 6 character");
//                    password.setFocusable(true);
//                    error = true;
//                }
                if(!checkString(pass) || pass.length() < 6 || pass.length() >16 ){
                    password.setError("The password must have atleast one captial, lower letter and number and  greater than 6 but smaller than 16");
                    password.setFocusable(true);
                    error = true;
                }
                if(!pass.equals(conPass)){
                    compassword.setError("The password does not match");
                    compassword.setFocusable(true);
                    error = true;
                }
                if(error ==false){
                       myEdit.putString("username", uname);
                       myEdit.putString("email", emaill);
                       myEdit.putString("password", pass);
                       myEdit.commit();
                       sendEmail();
                       startActivity(new Intent(RegistrationActivity.this, VerificationActivity.class));
                       //registerUser(emaill, pass, uname);
                   }


            }
        });
        existaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });


    }

    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    private void sendEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        //Getting content for email
        int max = 999999;
        int min = 100000;
        int range = max - min + 1;
        int code = (int)Math.floor(Math.random()*(max-min+1)+min);
        email = findViewById(R.id.register_email);
        String emails = email.getText().toString();
        String subject = String.format("Account Verification Code");
        String message = String.format("Here is Attach ur account verification Code \n Verification Code :"+code+" \n enjoy our app~");
        myEdit.putInt("code",code);
        myEdit.commit();
        //Creating SendMail object
        SendMail sm = new SendMail( this,emails, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void onClick(View view) {
        sendEmail();
    }

}