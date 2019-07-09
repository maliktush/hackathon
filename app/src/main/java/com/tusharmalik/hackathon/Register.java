package com.tusharmalik.hackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth2;
    private String mVerificationId2;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks2; // to check whether verification has been completed or failed
    private PhoneAuthProvider.ForceResendingToken mResendToken2; // to re verify the phone number
    EditText edNewMobNum2,edCode2,edName2,edNewPassword2,edConfirmNewPassword2,edOccupation2;
    Button btnCreateNew2,btnVerify2,btnSendOTP2;
    TextView tvResendCode2,tvLogin2;
    LinearLayout registerLayout2,verifyLayout2,sendLayout2,successLayout2;
    public static final String TAG = "RegisterActivity";
    DatabaseReference mDatabase2;
    ProgressDialog progressDialog2;
    String mobNumber = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edNewMobNum2 = findViewById(R.id.edNewMobNum2);
        edCode2 = findViewById(R.id.edCode2);
        edName2 = findViewById(R.id.edName2);
        edNewPassword2= findViewById(R.id.edNewPassword2);
        edConfirmNewPassword2 = findViewById(R.id.edConfirmNewPassword2);
        edOccupation2 = findViewById(R.id.edOccupation2);

        btnCreateNew2 = findViewById(R.id.btnCreateNew2);
        btnVerify2 = findViewById(R.id.btnVerify2);
        btnSendOTP2 = findViewById(R.id.btnSendOTP2);

        tvResendCode2 = findViewById(R.id.tvResendCode2);
        tvLogin2 = findViewById(R.id.tvLogin2);

        registerLayout2 = findViewById(R.id.registerLayout2);
        verifyLayout2 = findViewById(R.id.verifyLayout2);
        sendLayout2 = findViewById(R.id.sendLayout2);
        successLayout2 = findViewById(R.id.successLayout2);

        verifyLayout2.setVisibility(View.GONE);
        disableView(registerLayout2);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setTitle("Creating Account");
        progressDialog2.setMessage("Please wait this may take a while.");

        //mobNumber = mobNumber.concat(edNewMobNum.getText().toString());

        mAuth2 = FirebaseAuth.getInstance();

        mCallbacks2 = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(RegisterActivity.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Register.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Log.d(TAG, "onVerificationFailed: " + "Invalid Credential" + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "onVerificationFailed: " + "SMS Quota exceeded");
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Toast.makeText(Register.this, "Verification code has been sent to your Number!", Toast.LENGTH_SHORT).show();
                mVerificationId2 = verificationId;
                mResendToken2 = forceResendingToken;
                sendLayout2.setVisibility(View.GONE);
                verifyLayout2.setVisibility(View.VISIBLE);
            }
        };

        tvLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });

        tvResendCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        edNewMobNum2.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        Register.this,               // Activity (for callback binding)
                        mCallbacks2,         // OnVerificationStateChangedCallbacks
                        mResendToken2);
                Toast.makeText(Register.this, "Verification code has been sent to your Number!", Toast.LENGTH_SHORT).show();
            }

        });

        btnCreateNew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edName2.getText().toString().isEmpty() && !edNewPassword2.getText().toString().isEmpty() && !edConfirmNewPassword2.getText().toString().isEmpty() && !edOccupation2.getText().toString().isEmpty()){
                    progressDialog2.show();
                    mAuth2.createUserWithEmailAndPassword(edNewMobNum2.getText().toString().concat("@techno.com"),edNewPassword2.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog2.dismiss();
                                    if (task.isSuccessful()) {
                                        FirebaseUser user2 = mAuth2.getCurrentUser();
                                        mDatabase2= FirebaseDatabase.getInstance().getReference().child("Users").child(user2.getUid());
                                        HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("Name", edName2.getText().toString());
                                        userMap.put("Occupation",edOccupation2.getText().toString());

                                        mDatabase2.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Register.this, "Registration Successfull!!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Register.this,Home.class));
                                                }
                                                else{
                                                    Toast.makeText(Register.this, "Cannot create your account. Please check the form and try again", Toast.LENGTH_LONG).show();
                                                    Log.e(TAG, "onComplete: " + task.getException());
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Register.this,task.getException().getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if(!edNewPassword2.getText().toString().equals(edConfirmNewPassword2.getText().toString())){
                    Toast.makeText(Register.this, "Password Don't Match!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Register.this, "All fields are necessary!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSendOTP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edNewMobNum2.getText().toString().isEmpty()){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            edNewMobNum2.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            Register.this,
                            mCallbacks2
                    );
                }
                else{
                    Toast.makeText(Register.this, "Enter the Mobile Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVerify2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edCode2.getText().toString().isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId2, edCode2.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
                else{
                    Toast.makeText(Register.this, "Enter Verification Code!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth2.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            verifyLayout2.setVisibility(View.GONE);
                            successLayout2.setVisibility(View.VISIBLE);
                            enableView(registerLayout2);

                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Register.this, "Invalid Code!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void disableView(LinearLayout layout){
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(false);
        }
    }

    public void enableView(LinearLayout layout){
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(true);
        }
    }
}
