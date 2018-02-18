package com.example.marius.passwordapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import cryptopack.PasswordCrypto;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassEditText;
    EditText newPassEditText;
    EditText newPassEditTextConfirm;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassEditText=(EditText)findViewById(R.id.oldPassEditText);
        newPassEditText=(EditText)findViewById(R.id.masterPassEditTextChange);
        newPassEditTextConfirm=(EditText)findViewById(R.id.masterPassEditTextConfirmChange);

        button=(Button)findViewById(R.id.ch_pass_button);
    }

    /**
     * Action Listener for the button on this page, initiates password change
     * @param view the View
     */
    public void changePass(View view) {
        String input=oldPassEditText.getText().toString();
        SharedPreferences sharedPreferences=getSharedPreferences("PassAppSharedPref", Context.MODE_PRIVATE);

        byte[] salt= Base64.decode(sharedPreferences.getString("salt",""),Base64.DEFAULT);
        byte[] encryptedPassword=Base64.decode(sharedPreferences.getString("encryptedPass",""),Base64.DEFAULT);

        try {
            if(PasswordCrypto.authenticate(input,encryptedPassword,salt)){
                goOn();
            } else{
                Toast.makeText(this,"Invalid password",Toast.LENGTH_LONG).show();
            }
        } catch (NoSuchAlgorithmException e) {
            //TODO show in monitor
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to break down larger methods and improve readability
     */
    private void goOn(){
        String newPass=newPassEditText.getText().toString();
        String newConfirmPass=newPassEditTextConfirm.getText().toString();

        if(!newPass.equals(newConfirmPass)){
            Toast.makeText(this,"Inputs do not match",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            byte[] salt= PasswordCrypto.generateSalt();
            byte[] encryptedPass=PasswordCrypto.getEncryptedPassword(newPass,salt);

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

        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}
