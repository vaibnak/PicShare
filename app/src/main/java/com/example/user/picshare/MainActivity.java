package com.example.user.picshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
    EditText editText1;
    EditText editText2;
    Button sgin;
    Button sgup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout backgroundRelativeLayout = (RelativeLayout)findViewById(R.id.backgroundRelativeLayout);
        backgroundRelativeLayout.setOnClickListener(this);
        editText1 = (EditText)findViewById(R.id.email);
        editText2 = (EditText)findViewById(R.id.pass);
        editText2.setOnKeyListener(this);
        sgin = (Button)findViewById(R.id.button1);
        sgup = (Button)findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signup(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void signup(View view) {
        String email = editText1.getText().toString();
        String pass = editText2.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "registration succesfull", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "registration unsuccesfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signin(View view) {
        String email = editText1.getText().toString();
        String pass = editText2.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "signin succesfull", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "signin Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
