package com.tusharmalik.hackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnLogin2,btnCreate2,btnShowPassword,btnHidePassword;
    EditText edMobNum2,edPassword2,edmail;
    private FirebaseAuth mAuth2;
    ProgressDialog progressDialog1;
    String mobNumberLogin;
    public static String[] mobilenumber = new String[1];
    public static String[] mailid = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreate2 = findViewById(R.id.btnCreate2);
        btnLogin2 = findViewById(R.id.btnLogin2);
        edMobNum2 =  findViewById(R.id.edMobNum2);
        edPassword2 = findViewById(R.id.edPassword2);
        edmail=findViewById(R.id.edmail);

        mAuth2 = FirebaseAuth.getInstance();

        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("Logging In");
        progressDialog1.setMessage("Please wait while we check your credentials.");

        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edMobNum2.getText().toString().isEmpty() && !edPassword2.getText().toString().isEmpty()){
                    progressDialog1.show();
                    mAuth2.signInWithEmailAndPassword(edMobNum2.getText().toString().concat("@techno.com"),edPassword2.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog1.dismiss();
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        mobilenumber[0] =edMobNum2.getText().toString();
                                        mailid[0]=edmail.getText().toString();
                                        Intent intent = new Intent(MainActivity.this,Home.class);

                                        intent.putExtra("epuzzle", "Hello Tushar!");

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity.this, "Enter Email and Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });


    }
}
