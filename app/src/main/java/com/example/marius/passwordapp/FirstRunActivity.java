package com.example.marius.passwordapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import cryptopack.PasswordCrypto;

public class FirstRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
    }

    /**
     * Action Listener attached to the button from the GUI, specifies the functionality: register and store (securely) the password
     * @param view the View
     */
    public void signIn(View view){
        EditText editText=(EditText)findViewById(R.id.masterPassEditText);
        if(editText.getText().toString().equals("")){
            Toast.makeText(this,"You must enter a master password",Toast.LENGTH_LONG).show();
            return;
        }

        EditText editText1=(EditText)findViewById(R.id.masterPassEditTextConfirm);
        if(!editText1.getText().toString().equals(editText.getText().toString())){
            Toast.makeText(this,"Inputs do not match",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            byte[] salt= PasswordCrypto.generateSalt();
            byte[] encryptedPass=PasswordCrypto.getEncryptedPassword(editText.getText().toString(),salt);

            SharedPreferences sharedPref=getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPref.edit();

            editor.putString("salt",Base64.encodeToString(salt,Base64.DEFAULT));
            editor.putString("encryptedPass",Base64.encodeToString(encryptedPass,Base64.DEFAULT));
            editor.apply();
        } catch (NoSuchAlgorithmException e) {
            //TODO show in monitor
        } catch (InvalidKeySpecException e) {
            //TODO show in monitor
        }

        File file=new File("data");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent toLoginActivity=new Intent(this,LoginActivity.class);
        startActivity(toLoginActivity);
    }
}
