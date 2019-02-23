package com.example.user.picshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
    EditText editText1;
    EditText editText2;
    EditText editText3;
    Button sgin;
    Button sgup;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.user.picshare", Context.MODE_PRIVATE);
        String storedname = (String)sharedPreferences.getString("name", "unknown");
        if(storedname != "unknown"){
            Intent intent = new Intent(getApplicationContext(), showUsers.class);
            intent.putExtra("name", storedname);
            startActivity(intent);
        }
        RelativeLayout backgroundRelativeLayout = (RelativeLayout)findViewById(R.id.backgroundRelativeLayout);
        backgroundRelativeLayout.setOnClickListener(this);
        editText1 = (EditText)findViewById(R.id.email);
        editText2 = (EditText)findViewById(R.id.pass);
        editText3 = (EditText)findViewById(R.id.name);
        editText2.setOnKeyListener(this);
        sgin = (Button)findViewById(R.id.button1);
        sgup = (Button)findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
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

    public void putinDatabase(String name, String email){
        String id = databaseReference.push().getKey();
        users item = new users(name,email);
        databaseReference.child(id).setValue(item);
        Intent intent = new Intent(getApplicationContext(), showUsers.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void signup(View view) {
       final String email = editText1.getText().toString();
        String pass = editText2.getText().toString();
       final String name = editText3.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            putinDatabase(name, email);
                            sharedPreferences.edit().putString("name", name).apply();
                            sharedPreferences.edit().putString("email",email).apply();
                            Toast.makeText(getApplicationContext(), "registration succesfull", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(), "registration unsuccesfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signin(View view) {
        final String email = editText1.getText().toString();
        String pass = editText2.getText().toString();
        final String name = editText3.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sharedPreferences.edit().putString("name", name).apply();
                            sharedPreferences.edit().putString("email",email).apply();
                            Toast.makeText(getApplicationContext(), "signin succesfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), showUsers.class);
                            intent.putExtra("name", name);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "signin Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
