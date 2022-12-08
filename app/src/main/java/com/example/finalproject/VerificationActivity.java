package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalproject.databinding.ActivityVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {
    private ActivityVerificationBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth = FirebaseAuth.getInstance();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_verification);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        TextView msg= binding.tvMSGcode;
        int passcode = sh.getInt("code", 0);
        String code = "" + passcode;
        String email = sh.getString("email","");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register");

        if (email == null){
            email = "not email import";
        }

        binding.tvMSGcode.setText("The code has been send to your email \n ("+email+") \n Please insert the code at the below:");
        binding.tvNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber1.getText().toString().length()==1)
                    binding.tvNumber2.requestFocus();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber2.getText().toString().length()==1)
                    binding.tvNumber3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber3.getText().toString().length()==1)
                    binding.tvNumber4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber4.getText().toString().length()==1)
                    binding.tvNumber5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber5.getText().toString().length()==1)
                    binding.tvNumber6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvNumber6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.tvNumber6.getText().toString().length()==1)
                    binding.btnVerify.performClick();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btnVerify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int code1 = Integer.parseInt(binding.tvNumber1.getText().toString());
                int code2 = Integer.parseInt(binding.tvNumber2.getText().toString());
                int code3 = Integer.parseInt(binding.tvNumber3.getText().toString());
                int code4 = Integer.parseInt(binding.tvNumber4.getText().toString());
                int code5 = Integer.parseInt(binding.tvNumber5.getText().toString());
                int code6 = Integer.parseInt(binding.tvNumber6.getText().toString());
                String verifyCode = String.format("%d%d%d%d%d%d",code1,code2,code3,code4,code5,code6);
                if(verifyCode.equals(code)) {

                    String emaill = sh.getString("email", "");
                    String uname = sh.getString("username", "");
                    String pass = sh.getString("password", "");
                    registerUser(emaill, pass, uname);


                }
                else{
                    Toast.makeText(VerificationActivity.this, "Wrong Passcode", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
    private void registerUser(String emaill, final String pass, final String uname) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", uname);
                    hashMap.put("onlineStatus", "online");
                    hashMap.put("typingTo", "noOne");
                    hashMap.put("image", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);
                    Toast.makeText(VerificationActivity.this, "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(VerificationActivity.this, DashboardActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(VerificationActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(VerificationActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}